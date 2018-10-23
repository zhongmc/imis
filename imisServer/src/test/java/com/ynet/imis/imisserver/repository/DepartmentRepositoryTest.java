package com.ynet.imis.imisserver.repository;

import java.util.List;

import javax.transaction.Transactional;

import com.ynet.imis.domain.org.Department;
import com.ynet.imis.imisserver.ImisApplicationTests;
import com.ynet.imis.repository.org.DepartmentRepository;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class DepartmentRepositoryTest extends ImisApplicationTests {


    @Autowired
    private DepartmentRepository departmentDao;

    @Test
    @Transactional
    public void whenLoadDepartmentTreeSuccess() throws Exception{

        System.out.println("====================== department tree ");
        List<Department> deps = departmentDao.getDepartmentTree();
        outputObject(deps);

        System.out.println("====================a department ");
        Department aDep = deps.get(0).getChilds().get(1);
        outputObject( aDep );

        Department dep = departmentDao.findById(aDep.getId()).get();
        outputObject( dep );


    }
}