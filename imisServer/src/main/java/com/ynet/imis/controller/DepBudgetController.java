package com.ynet.imis.controller;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import com.ynet.imis.bean.CostCollectionItem;
import com.ynet.imis.domain.budget.CostBudgetInfo;
import com.ynet.imis.domain.budget.CostGroup;
import com.ynet.imis.domain.budget.CostItem;
import com.ynet.imis.domain.budget.DepCommBudget;
import com.ynet.imis.domain.budget.PrjBudget;
import com.ynet.imis.domain.budget.PrjMonthBudget;
import com.ynet.imis.service.budget.BudgetAdminService;
import com.ynet.imis.service.budget.DepBudgetService;
import com.ynet.imis.service.budget.PrjBudgetService;
import com.ynet.imis.service.org.DepartmentService;
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
public class DepBudgetController {

    @Autowired
    private DepBudgetService depBudgetService;

    @Autowired
    private BudgetAdminService budgetAdminService;

    @Autowired
    private PrjBudgetService prjBudgetService;

    @Autowired
    private DepartmentService departmentService;

    private Logger logger = LoggerFactory.getLogger(getClass());

    @RequestMapping(value = "/dep/{depId}/{typeId}", method = RequestMethod.GET)
    public List<CostBudgetInfo> getCostBudgetInfosByDepId(@PathVariable Long depId, @PathVariable Long typeId) {

        logger.info("Get department cost budget info :" + depId + " of type: " + typeId);

        int year = Calendar.getInstance().get(Calendar.YEAR);

        List<CostBudgetInfo> costBudgetInfos = depBudgetService.getCostBudgetInfosByDepId(depId, typeId, year);

        if (costBudgetInfos != null && costBudgetInfos.size() != 0)
            return costBudgetInfos;

        logger.info("Create cost budget info dep: " + depId);

        List<CostItem> costItems = budgetAdminService.getCostItems();

        List<CostBudgetInfo> infos = new ArrayList<CostBudgetInfo>();

        for (int i = 0; i < costItems.size(); i++) {
            CostItem costItem = costItems.get(i);
            for (int k = 0; k < 12; k++) {
                CostBudgetInfo info = new CostBudgetInfo();
                info.setYear(year);
                info.setMonth((short) k);
                info.setDepId(depId);
                info.setCostItemId(costItem.getId());
                info.setBudgetTypeId(typeId);
                infos.add(info);
            }
        }

        depBudgetService.addAllCostBudgetInfos(infos);

        return infos;
    }

    // 提交预算编辑
    @RequestMapping(value = "/dep", method = RequestMethod.POST)
    public ResponseBean addProjectBudget(@RequestBody List<CostBudgetInfo> infos) {

        logger.info("add dep cost info : " + ImisUtils.objectJsonStr(infos));
        int ret = depBudgetService.addAllCostBudgetInfos(infos);
        if (ret != 0) {
            return new ResponseBean("success", "添加成功!");
        }
        return new ResponseBean("error", "添加失败!");
    }

    /// 部门公共费用
    //// commBudget cost
    @RequestMapping(value = "/dep/cost/{depId}", method = RequestMethod.GET)
    public List<DepCommBudget> getDepCommBudgets(@PathVariable Long depId) {

        return depBudgetService.getDepCommBudgets(depId);
    }

    @RequestMapping(value = "/dep/cost", method = RequestMethod.POST)
    public ResponseBean addDepCommBudget(DepCommBudget depCommBudget) {
        logger.info("add dep comm budget: " + ImisUtils.objectJsonStr(depCommBudget));
        DepCommBudget pcb = depBudgetService.saveDepCommBudget(depCommBudget);
        if (pcb != null) {
            return new ResponseBean("success", "添加成功!", pcb);
        }
        return new ResponseBean("error", "添加失败!");
    }

    @RequestMapping(value = "/dep/cost", method = RequestMethod.PUT)
    public ResponseBean updatePrjCommBudget(DepCommBudget depCommBudget) {
        logger.info("update dep comm budget: " + ImisUtils.objectJsonStr(depCommBudget));
        DepCommBudget pcb = depBudgetService.saveDepCommBudget(depCommBudget);
        if (pcb != null) {
            return new ResponseBean("success", "修改成功!", pcb);
        }
        return new ResponseBean("error", "修改失败!");
    }

    @RequestMapping(value = "/dep/cost/{id}", method = RequestMethod.DELETE)
    public ResponseBean deletePrjCommBudget(@PathVariable Long id) {
        logger.info("delete project comm budget: " + id);
        int ret = depBudgetService.deleteDepCommBudget(id);

        if (ret == 1) {
            return new ResponseBean("success", "删除成功!");
        }
        return new ResponseBean("error", "删除失败!");
    }

    ///// projects
    @RequestMapping(value = "/dep/prj/{depId}", method = RequestMethod.GET)
    public List<Object> getDepPrjBudgets(@PathVariable Long depId) {

        return depBudgetService.getDepPrjBudgets(depId);
    }

    @RequestMapping(value = "/dep/prj", method = RequestMethod.POST)
    public ResponseBean updateDepPrjBudgets(@RequestBody PrjBudget prjBudget) {
        logger.info("update dep project budget: \n" + ImisUtils.objectJsonStr(prjBudget));

        // List<PrjMonthBudget> prjMonthBudgets = prjBudget.getMonthBudgets();
        int ret = prjBudgetService.savePrjMonthBudgets(prjBudget);

        if (ret == 1) {
            return new ResponseBean("success", "更新成功!");
        }
        return new ResponseBean("error", "更新失败!");

    }

    ///// department collection table

    // 部门费用汇总
    @RequestMapping(value = "/dep/costColl/{depId}", method = RequestMethod.GET)
    public List<CostCollectionItem> getCostBudgetInfosByDepId(@PathVariable Long depId) {

        logger.info("Get department cost budget collection :" + depId);
        int year = Calendar.getInstance().get(Calendar.YEAR);

        List<CostCollectionItem> items = new ArrayList<CostCollectionItem>();

        // List<CostBudgetInfo> costBudgetInfos =
        // depBudgetService.getCostCollectionByDepId(depId, year);

        // if (costBudgetInfos == null || costBudgetInfos.size() == 0)
        // return items;

        List<CostItem> costItems = budgetAdminService.getCostItems();
        // List<CostGroup> costGroups = budgetAdminService.getCostGroups();

        CostCollectionItem sumItem = new CostCollectionItem("", "汇总");

        List<Long> depIds = departmentService.listAppDepartmentsIdByPid(depId);

        logger.info("Dep ids: " + ImisUtils.objectJsonStr(depIds));
        for (int i = 0; i < costItems.size(); i++) {
            CostItem costItem = costItems.get(i);
            CostGroup costGroup = costItem.getGroup();
            CostCollectionItem item = new CostCollectionItem(costGroup.getName(), costItem.getName());
            items.add(item);
            List<CostBudgetInfo> costs = depBudgetService.getCostCollectionByItemDepId(depIds, costItem.getId(), year);

            for (int k = 0; k < costs.size(); k++) {
                CostBudgetInfo cost = costs.get(k);
                item.addAmount(cost.getMonth(), cost.getAmount());
                if (costItem.getBeAmount()) {
                    sumItem.addAmount(cost.getMonth(), cost.getAmount()); // 统计
                }
            }
        }

        items.add(sumItem);

        logger.info(" dep cost collection: " + ImisUtils.objectJsonStr(items));
        return items;
    }

}