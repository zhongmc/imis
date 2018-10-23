package com.ynet.imis.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ynet.imis.controller.ResponseBean;
import com.ynet.imis.domain.menu.User;
import com.ynet.imis.domain.project.Project;
import com.ynet.imis.service.org.DepartmentService;
import com.ynet.imis.service.project.ProjectService;
import com.ynet.imis.utils.ImisUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/project/basic")
public class ProjectController {

    @Autowired
    private ProjectService projectService;

    @Autowired
    private DepartmentService departmentService;

    private Logger logger = LoggerFactory.getLogger(getClass());

    @RequestMapping(value = "/prj", method = RequestMethod.POST)
    public ResponseBean addProject(Project project) {

        logger.info("add project: " + ImisUtils.objectJsonStr(project));

        Project aPrj = projectService.addProject(project);
        if (aPrj != null) {
            return new ResponseBean("success", "添加成功!", aPrj);
        }
        return new ResponseBean("error", "添加失败!");
    }

    @RequestMapping(value = "/prj", method = RequestMethod.PUT)
    public ResponseBean updateProject(Project project) {

        logger.info("update project: " + ImisUtils.objectJsonStr(project));

        if (projectService.updateProject(project) == 1) {
            return new ResponseBean("success", "更新成功!");
        }
        return new ResponseBean("error", "更新失败!");
    }

    @RequestMapping(value = "/prj/{ids}", method = RequestMethod.DELETE)
    public ResponseBean deleteEmpById(@PathVariable Long ids) {
        if (projectService.deleteProjectById(ids) == 1) {
            return new ResponseBean("success", "删除成功!");
        }
        return new ResponseBean("error", "删除失败!");
    }

    // url = url + "&customId=" + this.advSearchData.customId;
    // url = url + "&departmentId=" + this.advSearchData.departmentId;
    // url = url + "&prjTypeId=" + this.advSearchData.prjTypeId;
    // url = url + "&prjClassId=" + this.advSearchData.prjClassId;
    // url = url + "&beginDateScope=" + this.advSearchData.beginDateScope;

    @RequestMapping(value = "/prjs", method = RequestMethod.GET)
    public Map<String, Object> getProjectByPage(@RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "10") Integer size, @RequestParam(defaultValue = "") String keyword,
            Long customId, Long departmentId, Long prjType, Long prjClass, String beginDateScope) {
        Map<String, Object> map = new HashMap<>();

        List<Long> depIds = null;

        if (departmentId != null) //
        {
            depIds = departmentService.listAppDepartmentsIdByPid(departmentId);
            departmentId = null;
        } else { // only allow the owned department's projects
            User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            if (!user.isSupervisor()) {
                depIds = departmentService.listAppDepartmentsIdByPid(user.getDepId());
            }
        }

        Page<Project> pageProject = projectService.getProjectByPage(page, size, keyword, customId, departmentId,
                prjType, prjClass, beginDateScope, depIds);

        logger.info("load my projects: " + ImisUtils.objectJsonStr(pageProject.getContent()));
        map.put("prjs", pageProject.getContent());
        map.put("count", pageProject.getTotalElements());
        return map;
    }

}