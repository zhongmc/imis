package com.ynet.imis.service.utils;

import java.util.Properties;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.multipart.MultipartFile;

public class InitServiceImpl implements InitService {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public int doInitialize(String userName, String password, MultipartFile file) {

        logger.info("User " + userName + " Required to initialize the system....");

        try {
            boolean ret = isUserPasswordValid(userName, password);

            if (!ret) {
                logger.info("invalid init username and password " + userName + " " + password);
                return -1;
            }

            ObjectMapper mapper = new ObjectMapper();
            // 转换为格式化的json
            mapper.enable(SerializationFeature.INDENT_OUTPUT);

            // 如果json中有新增的字段并且是实体类类中不存在的，不报错
            mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            InitData initData = mapper.readValue(file.getInputStream(), InitData.class);

        } catch (Exception e) {

        }
        return 0;
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

    // @Override
    // public boolean isSystemInitialized() {

    // LockEntity aLock = lockRepository.findByLockId(1);
    // if (aLock == null)
    // return false;

    // return true;
    // }

    // @Transactional(propagation = Propagation.REQUIRES_NEW)
    // private LockEntity insertLock() {
    // LockEntity lock = new LockEntity(1, 1);
    // lockRepository.save(lock);
    // return lock;
    // }

}