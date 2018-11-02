/**
* PrjBudgetController.java
* @author ZHONGMC
* @description 
* @created Wed Oct 10 2018 10:38:38 GMT+0800 (中国标准时间)
* @copyright YNET
* @last-modified Thu Nov 01 2018 11:12:39 GMT+0800 (中国标准时间)
*/

package com.ynet.imis.controller;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.ynet.imis.controller.ResponseBean;

import com.ynet.imis.domain.budget.PrjBudget;
import com.ynet.imis.domain.budget.PrjCommBudget;
import com.ynet.imis.domain.budget.PrjIncomeForecast;
import com.ynet.imis.domain.budget.PrjMonthBudget;
import com.ynet.imis.domain.budget.PrjRightsConfirm;
import com.ynet.imis.domain.project.Project;
import com.ynet.imis.service.budget.BudgetAdminService;
import com.ynet.imis.service.budget.PrjBudgetService;
import com.ynet.imis.service.project.ProjectService;
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
public class PrjBudgetController {

    @Autowired
    PrjBudgetService prjBudgetService;

    @Autowired
    BudgetAdminService budgetAdminService;

    @Autowired
    private ProjectService projectService;

    private Logger logger = LoggerFactory.getLogger(getClass());

    @RequestMapping(value = "/project/{prjId}", method = RequestMethod.GET)
    public PrjBudget getProjectBudgetByPrjId(@PathVariable Long prjId) {

        logger.info("Get project budget for prj:" + prjId);

        PrjBudget aPrjBudget = prjBudgetService.getProjectBudgetByPrjId(prjId);
        if (aPrjBudget != null)
            return aPrjBudget;

        logger.info("Create new budgets for prj: " + prjId);

        Project project = projectService.getProjectById(prjId);
        Long depId = project.getDepId();

        Date begDate = project.getBeginDate();
        Date endDate = project.getEndDate();

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(begDate);

        Calendar endCal = Calendar.getInstance();
        endCal.setTime(endDate);

        PrjBudget prjBudget = new PrjBudget();
        prjBudget.setProject(project);
        // prjBudget.setAvgManMonthCost(new BigDecimal(12000));
        prjBudget.setPrjNo(project.getPrjNo());
        prjBudget.setPrjName(project.getName());
        prjBudget.setDepId(depId);

        while (calendar.before(endCal)) {
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            calendar.add(Calendar.MONTH, 1);

            PrjMonthBudget pMB = new PrjMonthBudget();
            pMB.setYear(year);
            pMB.setMonth((short) month);
            pMB.setPrjBudget(prjBudget);
            pMB.setDepId(depId);
            pMB.setPrjId(prjId);

            prjBudget.addPrjMonthBudget(pMB);

        }
        return prjBudget;

    }

    @RequestMapping(value = "/project", method = RequestMethod.POST)
    public ResponseBean addProjectBudget(@RequestBody PrjBudget prjBudget) {

        logger.info("add project budget: " + ImisUtils.objectJsonStr(prjBudget));
        int ret = prjBudgetService.saveProjectBudget(prjBudget);
        if (ret != 0) {
            return new ResponseBean("success", "添加成功!", prjBudget.getId());
        }
        return new ResponseBean("error", "添加失败!");
    }

    //// commBudget cost
    @RequestMapping(value = "/project/cost/{prjId}", method = RequestMethod.GET)
    public List<PrjCommBudget> getPrjCommBudgets(@PathVariable Long prjId) {

        return prjBudgetService.getProjectCommBudgets(prjId);
    }

    @RequestMapping(value = "/project/cost", method = RequestMethod.POST)
    public ResponseBean addPrjCommBudget(PrjCommBudget prjCommBudget) {
        logger.info("add project comm budget: " + ImisUtils.objectJsonStr(prjCommBudget));
        PrjCommBudget pcb = prjBudgetService.savePrjCommBudget(prjCommBudget);
        if (pcb != null) {
            return new ResponseBean("success", "添加成功!", pcb);
        }
        return new ResponseBean("error", "添加失败!");
    }

    @RequestMapping(value = "/project/cost", method = RequestMethod.PUT)
    public ResponseBean updatePrjCommBudget(PrjCommBudget prjCommBudget) {
        logger.info("update project comm budget: " + ImisUtils.objectJsonStr(prjCommBudget));
        PrjCommBudget pcb = prjBudgetService.savePrjCommBudget(prjCommBudget);
        if (pcb != null) {
            return new ResponseBean("success", "修改成功!", pcb);
        }
        return new ResponseBean("error", "修改失败!");
    }

    @RequestMapping(value = "/project/cost/{id}", method = RequestMethod.DELETE)
    public ResponseBean deletePrjCommBudget(@PathVariable Long id) {
        logger.info("delete project comm budget: " + id);
        int ret = prjBudgetService.deletePrjCommBudget(id);

        if (ret == 1) {
            return new ResponseBean("success", "删除成功!");
        }
        return new ResponseBean("error", "删除失败!");
    }

    //////// confirm rights confirm
    @RequestMapping(value = "/project/confirm/{prjId}")
    public List<PrjRightsConfirm> getPrjRightsConfirms(@PathVariable Long prjId) {

        return prjBudgetService.getPrjRightsConfirms(prjId);
    }

    @RequestMapping(value = "/project/confirm", method = RequestMethod.POST)
    public ResponseBean addPrjRightsConfirm(PrjRightsConfirm prjRightsConfirm) {
        logger.info("add project rights confirm: " + ImisUtils.objectJsonStr(prjRightsConfirm));
        PrjRightsConfirm pcb = prjBudgetService.savePrjRightsConfirm(prjRightsConfirm);
        if (pcb != null) {
            return new ResponseBean("success", "添加成功!", pcb);
        }
        return new ResponseBean("error", "添加失败!");
    }

    // 确认确权
    @RequestMapping(value = "/project/confirm/done", method = RequestMethod.POST)
    public ResponseBean doPrjRightsConfirm(Long id, Date endDate, BigDecimal amount) {
        logger.info("do project rights confirm: " + id + " ed:" + endDate + " amount: " + amount);
        PrjRightsConfirm pcb = prjBudgetService.confirmPrjRight(id, endDate, amount); // savePrjRightsConfirm(prjRightsConfirm);
        logger.info(ImisUtils.objectJsonStr(pcb));
        if (pcb != null) {
            return new ResponseBean("success", "确认成功!", pcb);
        }
        return new ResponseBean("error", "确认失败!");
    }

    // 确认收入
    @RequestMapping(value = "/project/income/done", method = RequestMethod.POST)
    public ResponseBean doPrjIncome(Long id, Date endDate, BigDecimal amount) {
        logger.info("do project income confirm: " + id + " ed:" + endDate + " amount: " + amount);
        PrjIncomeForecast pcb = prjBudgetService.confirmPrjIncome(id, endDate, amount); // savePrjRightsConfirm(prjRightsConfirm);
        logger.info(ImisUtils.objectJsonStr(pcb));
        if (pcb != null) {
            return new ResponseBean("success", "确认成功!", pcb);
        }
        return new ResponseBean("error", "确认失败!");
    }

    @RequestMapping(value = "/project/confirm", method = RequestMethod.PUT)
    public ResponseBean updatePrjRightsConfirm(PrjRightsConfirm prjRightsConfirm) {
        logger.info("update project PrjRightsConfirm : " + ImisUtils.objectJsonStr(prjRightsConfirm));
        PrjRightsConfirm pcb = prjBudgetService.savePrjRightsConfirm(prjRightsConfirm);
        if (pcb != null) {
            return new ResponseBean("success", "修改成功!", pcb);
        }
        return new ResponseBean("error", "修改失败!");
    }

    @RequestMapping(value = "/project/confirm/{id}", method = RequestMethod.DELETE)
    public ResponseBean deletePrjRightsConfirm(@PathVariable Long id) {
        logger.info("delete project rights confirm: " + id);
        int ret = prjBudgetService.deletePrjRightsConfirm(id);

        if (ret == 1) {
            return new ResponseBean("success", "删除成功!");
        }
        return new ResponseBean("error", "删除失败!");
    }

    ///////// income
    @RequestMapping(value = "/project/income/{prjId}")
    public List<PrjIncomeForecast> getPrjIncomeForecasts(@PathVariable Long prjId) {

        return prjBudgetService.getPrjIncomeForecasts(prjId);
    }

    @RequestMapping(value = "/project/income", method = RequestMethod.POST)
    public ResponseBean addPrjIncomeForecast(PrjIncomeForecast prjIncomeForecast) {
        logger.info("add project income forecast: " + ImisUtils.objectJsonStr(prjIncomeForecast));
        PrjIncomeForecast pcb = prjBudgetService.savePrjIncomeForecast(prjIncomeForecast);
        if (pcb != null) {
            return new ResponseBean("success", "添加成功!", pcb);
        }
        return new ResponseBean("error", "添加失败!");
    }

    @RequestMapping(value = "/project/income", method = RequestMethod.PUT)
    public ResponseBean updatePrjIncomeForecast(PrjIncomeForecast prjIncomeForecast) {
        logger.info("update project Prj income forecast : " + ImisUtils.objectJsonStr(prjIncomeForecast));
        PrjIncomeForecast pcb = prjBudgetService.savePrjIncomeForecast(prjIncomeForecast);
        if (pcb != null) {
            return new ResponseBean("success", "添加成功!", pcb);
        }
        return new ResponseBean("error", "添加失败!");
    }

    @RequestMapping(value = "/project/income/{id}", method = RequestMethod.DELETE)
    public ResponseBean deletePrjIncomeForecast(@PathVariable Long id) {
        logger.info("delete PrjIncomeForecast: " + id);
        int ret = prjBudgetService.deletePrjIncomeForecast(id);

        if (ret == 1) {
            return new ResponseBean("success", "删除成功!");
        }
        return new ResponseBean("error", "删除失败!");
    }

}