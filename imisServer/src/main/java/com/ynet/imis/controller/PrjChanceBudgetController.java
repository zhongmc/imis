/**
* PrjChanceBudgetController.java
* @author ZHONGMC
* @description 
* @created Thu Nov 29 2018 14:25:44 GMT+0800 (中国标准时间)
* @copyright YNET
* @last-modified Fri Dec 07 2018 14:55:00 GMT+0800 (中国标准时间)
*/

package com.ynet.imis.controller;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.ynet.imis.domain.budget.PrjChanceBudget;
import com.ynet.imis.domain.budget.PrjChanceCommBudget;
import com.ynet.imis.domain.budget.PrjChanceMonthBudget;
import com.ynet.imis.domain.budget.PrjChanceRightsConfirm;
import com.ynet.imis.domain.project.ProjectChance;
import com.ynet.imis.service.budget.BudgetAdminService;
import com.ynet.imis.service.budget.PrjChanceBudgetService;
import com.ynet.imis.service.project.ProjectChanceService;
import com.ynet.imis.utils.ImisUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/budget")
public class PrjChanceBudgetController {

    @Autowired
    PrjChanceBudgetService prjChanceBudgetService;

    @Autowired
    BudgetAdminService budgetAdminService;

    @Autowired
    private ProjectChanceService projectChanceService;

    private Logger logger = LoggerFactory.getLogger(getClass());

    @RequestMapping(value = "/prjChance/{prjChanceId}", method = RequestMethod.GET)
    public PrjChanceBudget getProjectChanceBudgetByPrjChanceId(@PathVariable Long prjChanceId) {

        logger.info("Get project chance budget for prj:" + prjChanceId);

        PrjChanceBudget aPrjBudget = prjChanceBudgetService.getProjectChanceBudgetByPrjId(prjChanceId);
        if (aPrjBudget != null)
            return aPrjBudget;

        logger.info("Create new budgets for prj chance: " + prjChanceId);

        ProjectChance projectChance = projectChanceService.getProjectChanceById(prjChanceId);
        Long depId = projectChance.getDepId();

        Date begDate = projectChance.getBeginDate();
        Date endDate = projectChance.getEndDate();

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(begDate);

        Calendar endCal = Calendar.getInstance();
        endCal.setTime(endDate);

        PrjChanceBudget prjChanceBudget = new PrjChanceBudget();
        prjChanceBudget.setProjectChance(projectChance);
        prjChanceBudget.setContractAmount(projectChance.getContractAmount());
        // prjBudget.setAvgManMonthCost(new BigDecimal(12000));
        prjChanceBudget.setPrjChanceName(projectChance.getName());
        prjChanceBudget.setDepId(depId);
        prjChanceBudget.setChance(projectChance.getChance());

        while (calendar.before(endCal)) {
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            calendar.add(Calendar.MONTH, 1);

            PrjChanceMonthBudget pMB = new PrjChanceMonthBudget();
            pMB.setYear(year);
            pMB.setMonth((short) month);
            pMB.setPrjChanceBudget(prjChanceBudget);
            pMB.setDepId(depId);
            pMB.setPrjChanceId(prjChanceId);

            prjChanceBudget.addPrjChanceMonthBudget(pMB);

        }
        return prjChanceBudget;

    }

    @RequestMapping(value = "/prjChance", method = RequestMethod.POST)
    public ResponseBean addProjectChanceBudget(@RequestBody PrjChanceBudget prjChanceBudget) {

        logger.info("add project Chance budget: " + ImisUtils.objectJsonStr(prjChanceBudget));
        int ret = prjChanceBudgetService.saveProjectChanceBudget(prjChanceBudget);
        if (ret != 0) {
            return new ResponseBean("success", "添加成功!", prjChanceBudget.getId());
        }
        return new ResponseBean("error", "添加失败!");
    }

    //////// confirm rights confirm
    @RequestMapping(value = "/prjChance/confirm/{prjChanceId}")
    public List<PrjChanceRightsConfirm> getPrjChanceRightsConfirms(@PathVariable Long prjChanceId) {
        List<PrjChanceRightsConfirm> confirms = prjChanceBudgetService.getPrjChanceRightsConfirms(prjChanceId);
        logger.info("prj chance confirms: " + ImisUtils.objectJsonStr(confirms));
        return confirms;
    }

    @RequestMapping(value = "/prjChance/confirm", method = RequestMethod.POST)
    public ResponseBean addPrjChanceRightsConfirm(PrjChanceRightsConfirm prjChanceRightsConfirm) {
        logger.info("add project Chance rights confirm: " + ImisUtils.objectJsonStr(prjChanceRightsConfirm));
        PrjChanceRightsConfirm pcb = prjChanceBudgetService.savePrjChanceRightsConfirm(prjChanceRightsConfirm);
        if (pcb != null) {
            return new ResponseBean("success", "添加成功!", pcb);
        }
        return new ResponseBean("error", "添加失败!");
    }

    // 确认确权
    @RequestMapping(value = "/prjChance/confirm/done", method = RequestMethod.POST)
    public ResponseBean doPrjChanceRightsConfirm(Long id, Date endDate, BigDecimal amount) {
        logger.info("do project  Chance rights confirm: " + id + " ed:" + endDate + " amount: " + amount);
        PrjChanceRightsConfirm pcb = prjChanceBudgetService.confirmPrjChanceRight(id, endDate, amount); // savePrjRightsConfirm(prjRightsConfirm);
        logger.info(ImisUtils.objectJsonStr(pcb));
        if (pcb != null) {
            return new ResponseBean("success", "确认成功!", pcb);
        }
        return new ResponseBean("error", "确认失败!");
    }

    @RequestMapping(value = "/prjChance/confirm", method = RequestMethod.PUT)
    public ResponseBean updatePrjChanceRightsConfirm(PrjChanceRightsConfirm prjChanceRightsConfirm) {
        logger.info("update project Chance PrjRightsConfirm : " + ImisUtils.objectJsonStr(prjChanceRightsConfirm));

        // delete to update the month budgets
        prjChanceBudgetService.deletePrjChanceRightsConfirm(prjChanceRightsConfirm.getId());

        PrjChanceRightsConfirm pcb = prjChanceBudgetService.savePrjChanceRightsConfirm(prjChanceRightsConfirm);
        if (pcb != null) {
            return new ResponseBean("success", "修改成功!", pcb);
        }
        return new ResponseBean("error", "修改失败!");
    }

    @RequestMapping(value = "/prjChance/confirm/{id}", method = RequestMethod.DELETE)
    public ResponseBean deletePrjChanceRightsConfirm(@PathVariable Long id) {
        logger.info("delete project chance rights confirm: " + id);
        int ret = prjChanceBudgetService.deletePrjChanceRightsConfirm(id);

        if (ret == 1) {
            return new ResponseBean("success", "删除成功!");
        }
        return new ResponseBean("error", "删除失败!");
    }

    //// prj chance comm costs

    //////// confirm rights confirm
    @RequestMapping(value = "/prjChance/cost/{prjChanceId}")
    public List<PrjChanceCommBudget> getPrjChanceCommBudgets(@PathVariable Long prjChanceId) {
        List<PrjChanceCommBudget> commBudgets = prjChanceBudgetService.getPrjChanceCommBudgets(prjChanceId);
        return commBudgets;
    }

    @RequestMapping(value = "/prjChance/cost", method = RequestMethod.POST)
    public ResponseBean addPrjChanceCommBudget(PrjChanceCommBudget prjChanceCommBudget) {
        logger.info("add project Chance comm budget: " + ImisUtils.objectJsonStr(prjChanceCommBudget));
        PrjChanceCommBudget pcb = prjChanceBudgetService.savePrjChanceCommBudget(prjChanceCommBudget);
        if (pcb != null) {
            return new ResponseBean("success", "添加成功!", pcb);
        }
        return new ResponseBean("error", "添加失败!");
    }

    @RequestMapping(value = "/prjChance/cost", method = RequestMethod.PUT)
    public ResponseBean updatePrjChanceCommBudget(PrjChanceCommBudget prjChanceCommBudget) {
        logger.info("update project Chance PrjRightsConfirm : " + ImisUtils.objectJsonStr(prjChanceCommBudget));

        PrjChanceCommBudget pcb = prjChanceBudgetService.savePrjChanceCommBudget(prjChanceCommBudget);
        if (pcb != null) {
            return new ResponseBean("success", "修改成功!", pcb);
        }
        return new ResponseBean("error", "修改失败!");
    }

    @RequestMapping(value = "/prjChance/cost/{id}", method = RequestMethod.DELETE)
    public ResponseBean deletePrjChanceCommBudget(@PathVariable Long id) {
        logger.info("delete project Chance comm budget: " + id);
        int ret = prjChanceBudgetService.deletePrjChanceCommBudget(id);

        if (ret == 1) {
            return new ResponseBean("success", "删除成功!");
        }
        return new ResponseBean("error", "删除失败!");
    }

}