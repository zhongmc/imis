package com.ynet.imis.service.utils;

import java.util.List;
import java.util.Properties;

import javax.transaction.Transactional;
import javax.transaction.Transactional.TxType;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.ynet.imis.domain.LockEntity;
import com.ynet.imis.domain.budget.CostGroup;
import com.ynet.imis.domain.budget.CostItem;
import com.ynet.imis.domain.menu.Menu;
import com.ynet.imis.domain.menu.User;
import com.ynet.imis.domain.org.Department;
import com.ynet.imis.repository.LockRepository;
import com.ynet.imis.repository.budget.CostGroupRepository;
import com.ynet.imis.repository.budget.CostItemRepository;
import com.ynet.imis.repository.menu.MenuRepository;
import com.ynet.imis.repository.menu.UserRepository;
import com.ynet.imis.repository.org.DepartmentRepository;
import com.ynet.imis.utils.ImisUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@Transactional
public class InitServiceImpl implements InitService {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private LockRepository lockDao;

    @Autowired
    private UserRepository userDao;

    @Autowired
    private DepartmentRepository departmentDao;

    @Autowired
    private MenuRepository menuDao;

    @Autowired
    private CostGroupRepository costGroupDao;

    @Autowired
    private CostItemRepository costItemDao;

    @Override
    @Transactional
    public int doInitialize(String userName, String password, MultipartFile file) {

        logger.info("User " + userName + " Required to initialize the system....");

        if (isSystemInitialized())
            return -1;

        try {
            boolean ret = isUserPasswordValid(userName, password);

            if (!ret) {
                logger.info("invalid init username and password " + userName + " " + password);
                return -2;
            }

            saveLock(1, 1); // initializing...

            ObjectMapper mapper = new ObjectMapper();
            // 转换为格式化的json
            mapper.enable(SerializationFeature.INDENT_OUTPUT);

            // 如果json中有新增的字段并且是实体类类中不存在的，不报错
            mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            InitData initData = mapper.readValue(file.getInputStream(), InitData.class);
            for (User user : initData.getUsers()) {
                User aUser = addUser(user);
                logger.info("add user: " + ImisUtils.objectJsonStr(aUser));
            }

            for (Department dep : initData.getDepartments()) {
                Department aDep = addDepartment(dep);
                logger.info("add department:" + ImisUtils.objectJsonStr(aDep));
            }

            for (Menu menu : initData.getMenus()) {
                Menu aMenu = menuDao.save(menu);
                logger.info("add menu:" + ImisUtils.objectJsonStr(aMenu));
            }

            for (CostGroup group : initData.getCostDefs()) {
                CostGroup aGroup = addCostGroup(group);
                logger.info("add cost group: " + ImisUtils.objectJsonStr(aGroup));
            }

            saveLock(1, 2); // initialized

        } catch (Exception e) {
            logger.error("failed to initialize ", e);
            return -3;
        }
        return 0;
    }

    private CostGroup addCostGroup(CostGroup costGroup) {
        CostGroup group = costGroupDao.save(costGroup);

        List<CostItem> costItems = group.getCostItems();
        for (CostItem item : costItems) {
            item.setCostGroup(group);
        }

        costItems = costItemDao.saveAll(costItems);
        group.setCostItems(costItems);
        return group;

    }

    private Department addDepartment(Department dep) {
        return departmentDao.save(dep);
    }

    private User addUser(User user) {

        if (user == null)
            return null;

        // if (user.getRoles() != null) {
        // List<Role> roles = roleDao.saveAll(user.getRoles());
        // user.setRoles(roles);
        // }
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String encodedPass = encoder.encode(user.getPassword());
        user.setPassword(encodedPass);
        return userDao.save(user);
    }

    private boolean isUserPasswordValid(String username, String password) throws Exception {

        if (username == null || username.isEmpty())
            return false;

        Properties properties = new Properties();
        properties.load(new ClassPathResource("application.properties").getInputStream());

        String uName = properties.getProperty("imis.init.username");
        String pass = properties.getProperty("imis.init.password");

        if (uName.equals(username) && pass.equals(password))
            return true;
        return false;

    }

    @Override
    public boolean isSystemInitialized() {

        LockEntity aLock = lockDao.findByLockId(1);
        if (aLock == null)
            return false;

        return true;
    }

    @Transactional(TxType.REQUIRES_NEW)
    private LockEntity saveLock(int lockId, int lockValue) {
        LockEntity lock = lockDao.findByLockId(lockId);
        if (lock == null)
            lock = new LockEntity(lockId, lockValue);
        else
            lock.setLockValue(lockValue);
        lockDao.save(lock);
        return lock;
    }

}