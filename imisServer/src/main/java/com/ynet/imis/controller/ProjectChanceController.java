package com.ynet.imis.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ynet.imis.controller.ResponseBean;
import com.ynet.imis.domain.menu.User;
import com.ynet.imis.domain.project.ProjectChance;
import com.ynet.imis.service.org.DepartmentService;
import com.ynet.imis.service.project.ProjectChanceService;
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
@RequestMapping(value = "/project/chance")
public class ProjectChanceController {

    @Autowired
    private ProjectChanceService projectChanceService;

    @Autowired
    private DepartmentService departmentService;

    private Logger logger = LoggerFactory.getLogger(getClass());

    @RequestMapping(value = "/prjChance", method = RequestMethod.POST)
    public ResponseBean addProjectChance(ProjectChance projectChance) {

        logger.info("add project chance: " + ImisUtils.objectJsonStr(projectChance));

        ProjectChance aPrjChance = projectChanceService.addProjectChance(projectChance);
        if (aPrjChance != null) {
            return new ResponseBean("success", "添加成功!", aPrjChance);
        }
        return new ResponseBean("error", "添加失败!");
    }

    @RequestMapping(value = "/prjChance", method = RequestMethod.PUT)
    public ResponseBean updateProjectChance(ProjectChance projectChance) {

        logger.info("update project Chance: " + ImisUtils.objectJsonStr(projectChance));

        if (projectChanceService.updateProjectChance(projectChance) == 1) {
            return new ResponseBean("success", "更新成功!");
        }
        return new ResponseBean("error", "更新失败!");
    }

    @RequestMapping(value = "/prjChance/{ids}", method = RequestMethod.DELETE)
    public ResponseBean deletePrjChanceById(@PathVariable Long ids) {
        if (projectChanceService.deleteProjectChanceById(ids) == 1) {
            return new ResponseBean("success", "删除成功!");
        }
        return new ResponseBean("error", "删除失败!");
    }

    @RequestMapping(value = "/close", method = RequestMethod.POST)
    public ResponseBean closePrjChanceById(Long id) {

        logger.info("Close prj chance: " + id);

        if (projectChanceService.closeProjectChanceById(id) == 1) {
            return new ResponseBean("success", "关闭机会成功!");
        }
        return new ResponseBean("error", "关闭机会失败!");
    }

    @RequestMapping(value = "/toPrj", method = RequestMethod.POST)
    public ResponseBean transferPrjChanceToPrjById(Long id) {
        if (projectChanceService.transferProjectChanceToProjectById(id) == 1) {
            return new ResponseBean("success", "机会转项目成功!");
        }
        return new ResponseBean("error", "机会转项目失败!");
    }

    // url = url + "&customId=" + this.advSearchData.customId;
    // url = url + "&departmentId=" + this.advSearchData.departmentId;
    // url = url + "&prjTypeId=" + this.advSearchData.prjTypeId;
    // url = url + "&prjClassId=" + this.advSearchData.prjClassId;
    // url = url + "&beginDateScope=" + this.advSearchData.beginDateScope;

    @RequestMapping(value = "/prjChances", method = RequestMethod.GET)
    public Map<String, Object> getProjectChanceByPage(@RequestParam(defaultValue = "0") Integer page,
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

        Page<ProjectChance> pageProjectChance = projectChanceService.getProjectChanceByPage(page, size, keyword,
                customId, departmentId, prjType, prjClass, beginDateScope, depIds);

        logger.info("load my project chances: " + ImisUtils.objectJsonStr(pageProjectChance.getContent()));
        map.put("prjs", pageProjectChance.getContent());
        map.put("count", pageProjectChance.getTotalElements());
        return map;
    }

}