package com.ynet.imis.service.org;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import com.ynet.imis.domain.org.Department;
import com.ynet.imis.repository.org.DepartmentRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
@Transactional
public class DepartmentServiceImpl implements DepartmentService {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    DepartmentRepository departmentDao;

    @Override
    public List<Department> getDepartmentTree() {

        return departmentDao.getDepartmentTree();

    }

    /**
     * 某个节点的部门树
     */
    @Override
    public Department getDepartmentTreeByPid(Long pId) {

        logger.info("get department tree for pid: " + pId);
        if (pId == null || pId.longValue() == -1) {

            List<Department> deps = departmentDao.getDepartmentTree();
            if (deps.size() > 0)
                return deps.get(0); //////
            logger.error("Department data is null when get department tree by pid!");
            return null;
        }

        return departmentDao.findById(pId).get();

    }

    /**
     * 某个父节点下的所有子节点列表
     */
    @Override
    public List<Department> listAllDepartmentByPid(Long pId) {

        if (pId == null || pId.longValue() == -1)
            return departmentDao.getAllDeps();

        List<Department> deps = new ArrayList<Department>();
        Department dep = departmentDao.findById(pId).get();
        if (dep == null)
            return deps;

        deps.add(dep.clone());

        listChilds(dep.getChilds(), deps);

        return deps;

    }

    private void listChilds(List<Department> childs, List<Department> toList) {
        if (childs.size() == 0)
            return;
        for (Department dep : childs) {
            toList.add(dep.clone());
            listChilds(dep.getChilds(), toList);
        }
    }

    protected List<Department> findDepartmentWithParentId(Long pId, List<Department> deps) {
        List<Department> adeps = new ArrayList<Department>();
        for (Department dep : deps) {
            if (dep.getParentId() == pId)
                adeps.add(dep);
        }
        return adeps;

    }

    protected Department findDepartment(Long id, List<Department> deps) {
        for (Department dep : deps) {
            if (dep.getId() == id)
                return dep;
        }
        return null;
    }

    @Override
    public Department getDepartmentById(Long id) {
        Optional<Department> optDep = departmentDao.findById(id);
        return optDep.get();
    }

    @Override
    public void updateDepartment(Department dep) {
        logger.info("update department:" + dep);
        departmentDao.save(dep);
    }

    @Override
    public Department addDepartment(Department dep) {
        logger.info("Add department:" + dep);
        Department retDep = departmentDao.save(dep);
        return retDep;
    }

    @Override
    public int deleteDepartmentById(Long id) {
        departmentDao.deleteById(id);
        return 1;
        // 需要关联删除用户、项目、预算
    }

    @Override
    public List<Department> ListAllDepartments() {
        return departmentDao.getAllDeps();
    }

    @Override
    public List<Long> listAppDepartmentsIdByPid(Long pid) {
        List<Department> deps = this.listAllDepartmentByPid(pid);
        List<Long> depIds = new ArrayList<Long>();
        for (Department dep : deps)
            depIds.add(dep.getId());
        return depIds;
    }

}