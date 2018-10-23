package com.ynet.imis.imisserver.service;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import com.ynet.imis.domain.org.Custom;
import com.ynet.imis.domain.org.Department;
import com.ynet.imis.domain.project.Project;
import com.ynet.imis.domain.project.Project.PrjType;
import com.ynet.imis.imisserver.ImisApplicationTests;
import com.ynet.imis.service.org.CustomService;
import com.ynet.imis.service.org.DepartmentService;
import com.ynet.imis.service.project.ProjectService;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;

@Transactional
public class ProjectServiceTest extends ImisApplicationTests {

    @Autowired
    ProjectService projectService;

    @Autowired
    DepartmentService depService;

    @Autowired
    CustomService customService;

    @Test
    public void whenFindMyProjectsSuccess() throws Exception {

        List<Department> depTree = depService.getDepartmentTree();
        List<Custom> customs = customService.getCustomTree();

        System.out.println("Add  project 1");
        Project prj1 = new Project();
        prj1.setName("Project 1");
        prj1.setDepartment(depTree.get(0).getChilds().get(1));
        prj1.setCustom(customs.get(0));
        prj1.setPrjType(PrjType.TASKS);
        prj1.setPrjNo("PRJ-2018-01");

        prj1 = projectService.addProject(prj1);

        System.out.println("================prj1: ========");
        outputObject(prj1);

        Project prj2 = new Project();
        prj2.setName("Project 2");
        prj2.setDepartment(depTree.get(0).getChilds().get(2));
        prj2.setCustom(customs.get(1));
        prj2.setPrjType(PrjType.PROJECT);
        prj2.setPrjNo("PRJ-2018-02");

        prj2 = projectService.addProject(prj2);

        System.out.println("================prj2: ========");
        outputObject(prj2);

        Project prj3 = new Project();
        prj3.setName("Project 3");
        prj3.setDepartment(depTree.get(0).getChilds().get(1).getChilds().get(0));
        prj3.setCustom(customs.get(0));
        prj3.setPrjType(PrjType.PROJECT);
        prj3.setPrjNo("PRJ-2018-03");

        prj3 = projectService.addProject(prj3);

        System.out.println("================prj3: ========");
        outputObject(prj3);

        System.out.println("=========Found by key words=======");

        Page<Project> prjs = projectService.getProjectByPage(1, 10, "Project", null, null, null, null, null, null);
        outputObject(prjs);

        System.out.println("=========Found by custom Id =======");

        prjs = projectService.getProjectByPage(1, 10, null, customs.get(1).getId(), null, null, null, null, null);

        assertEquals("Find by department custom id: ", 1, prjs.getTotalElements());
        outputObject(prjs);

        System.out.println("=========Found by department Id =======");

        prjs = projectService.getProjectByPage(1, 10, null, null, depTree.get(0).getChilds().get(1).getId(), null, null,
                null, null);

        assertEquals("Find by department id: ", 1, prjs.getTotalElements());
        outputObject(prjs);

        List<Department> deps = depService.listAllDepartmentByPid(depTree.get(0).getId());

        List<Long> ids = new ArrayList<Long>();
        for (Department dep : deps)
            ids.add(dep.getId());

        System.out.println("=========Found by department Ids =======");

        prjs = projectService.getProjectByPage(1, 10, null, null, null, null, null, null, ids);

        assertEquals("find by department ids: ", 3, prjs.getTotalElements());

        outputObject(prjs);

    }
}