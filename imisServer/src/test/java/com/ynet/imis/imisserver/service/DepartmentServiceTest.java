package com.ynet.imis.imisserver.service;

import java.util.List;

import com.ynet.imis.domain.org.Department;
import com.ynet.imis.imisserver.ImisApplicationTests;
import com.ynet.imis.service.org.DepartmentService;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class DepartmentServiceTest extends ImisApplicationTests {

    @Autowired
    DepartmentService depService;

    @Test
    public void whenLoadDepartmentTreeSuccess() throws Exception {

        String zhStr = "a中文显示有问题b";
        System.out.println(zhStr);
        System.out.write(zhStr.getBytes("gb2312"));
        System.out.println();
        System.out.write(zhStr.getBytes("GBK"));
        System.out.println();

        System.out.println("======== Department Tree 中文显示有问题？===========================");
        List<Department> depTree = depService.getDepartmentTree();
        outputObject(depTree);

        System.out.println("======== My Dep tree ========================");
        Department dep = depTree.get(0).getChilds().get(2);
        Department dep1 = depService.getDepartmentTreeByPid(dep.getId());
        outputObject(dep1);

        List<Department> deps = depService.listAllDepartmentByPid(dep.getId());
        outputObject(deps);

        System.out.println("============== all deps list =============== ");
        List<Department> allDeps = depService.listAllDepartmentByPid(null);
        outputObject(allDeps);

    }

}