package com.ynet.imis.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ynet.imis.bean.CostCollectionItem;
import com.ynet.imis.domain.budget.BudgetType;
import com.ynet.imis.domain.budget.CostBudgetInfo;
import com.ynet.imis.domain.budget.CostGroup;
import com.ynet.imis.domain.budget.CostItem;
import com.ynet.imis.domain.budget.DepCommBudget;
import com.ynet.imis.domain.budget.PrjBudget;
import com.ynet.imis.domain.budget.PrjIncomeForecast;
import com.ynet.imis.domain.budget.PrjMonthBudget;
import com.ynet.imis.domain.budget.PrjRightsConfirm;
import com.ynet.imis.service.budget.BudgetAdminService;
import com.ynet.imis.service.budget.DepBudgetService;
import com.ynet.imis.service.budget.PrjBudgetService;
import com.ynet.imis.service.org.DepartmentService;
import com.ynet.imis.utils.ImisUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
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

    @RequestMapping(value = "/dep/prj/paged", method = RequestMethod.GET)
    public Page<Map<String, Object>> getPagedDepProjectBudgets(@RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "10") Integer size, Long depId) {

        int year = Calendar.getInstance().get(Calendar.YEAR);

        Page<Map<String, Object>> pagedPrjBudgets = depBudgetService.getDepPrjBudgetByPage(page, size, depId, year);

        logger.info(ImisUtils.objectJsonStr(pagedPrjBudgets));
        return pagedPrjBudgets;
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

        List<BudgetType> budgetTypes = budgetAdminService.getBudgetTypes();

        CostCollectionItem sumItem = new CostCollectionItem("", "汇总");

        List<Long> depIds = departmentService.listAppDepartmentsIdByPid(depId);

        Map<Long, CostCollectionItem> typedItemsMap = new HashMap<Long, CostCollectionItem>();

        CostCollectionItem typedItems[] = new CostCollectionItem[budgetTypes.size()];
        for (int i = 0; i < budgetTypes.size(); i++) {
            BudgetType budgetType = budgetTypes.get(i);
            CostCollectionItem item = new CostCollectionItem("管理维度", budgetType.getName());
            typedItemsMap.put(budgetType.getId(), item);
            typedItems[i] = item;
        }

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
                    CostCollectionItem aitem = typedItemsMap.get(cost.getBudgetTypeId());
                    aitem.addAmount(cost.getMonth(), cost.getAmount());
                }

            }

        }

        items.add(sumItem);

        for (int i = 0; i < typedItems.length; i++)
            items.add(typedItems[i]);

        logger.info(" dep cost collection: " + ImisUtils.objectJsonStr(items));
        return items;
    }

    // 部门损益表
    @RequestMapping(value = "/dep/budgetTable/{depId}", method = RequestMethod.GET)
    public List<CostCollectionItem> getBudgetTableDepId(@PathVariable Long depId) {

        logger.info("Get department cost budget collection :" + depId);
        int year = Calendar.getInstance().get(Calendar.YEAR);
        List<Long> depIds = departmentService.listAppDepartmentsIdByPid(depId);

        List<CostCollectionItem> items = new ArrayList<CostCollectionItem>();

        // 合同收入
        // CostCollectionItem item1 = new CostCollectionItem("", "合同收入");
        // 主营业务收入
        CostCollectionItem item2 = this.loadDeparmentPrjRightConfirms(depIds, year); // shoud have prjIds return
        // new CostCollectionItem("", "主营业务收入");

        // 增值税
        CostCollectionItem item3 = item2.multiply(BigDecimal.valueOf(0.06));
        item3.setName("增值税");

        // 主营业务成本
        CostCollectionItem itms[] = this.loadDeparmentPrjCosts(depIds, year);
        CostCollectionItem item4 = itms[0];

        // 毛利
        CostCollectionItem item5 = item2.subtract(item3);
        item5 = item5.subtract(item4);
        item5.setName("毛利");

        // 毛利率
        CostCollectionItem item6 = item5.divide(item2);
        item6.setName("毛利率");

        // 经营费用
        CostCollectionItem item7 = this.loadDepartmentCosts(depIds, year);

        // 其它费用
        CostCollectionItem item8 = new CostCollectionItem("", "其它费用");

        // 经营利润
        CostCollectionItem item9 = item5.subtract(item7.add(item8));
        item9.setName("经营利润");

        // // 净利润
        // CostCollectionItem item10 = new CostCollectionItem("", "净利润");

        // 净利润率
        CostCollectionItem item10 = item9.divide(item2);
        item10.setName("净利润率");

        // 现金流
        CostCollectionItem item11 = this.loadDeparmentPrjIncomes(depIds, year);

        // items.add(item1);
        items.add(item2);
        items.add(item3);
        items.add(item4);
        items.add(item5);
        items.add(item6);
        items.add(item7);
        items.add(item8);
        items.add(item9);
        items.add(item10);

        items.add(itms[1]); // 人月
        items.add(item11); // 现金

        return items;
    }

    private CostCollectionItem loadDepartmentCosts(List<Long> depIds, int year) {
        List<CostItem> costItems = budgetAdminService.getCostItems();

        CostCollectionItem item = new CostCollectionItem("", "经营费用");
        for (int i = 0; i < costItems.size(); i++) {
            CostItem costItem = costItems.get(i);
            if (!costItem.getBeAmount())
                continue;

            List<CostBudgetInfo> costs = depBudgetService.getCostCollectionByItemDepId(depIds, costItem.getId(), year);

            for (int k = 0; k < costs.size(); k++) {
                CostBudgetInfo cost = costs.get(k);
                item.addAmount(cost.getMonth(), cost.getAmount()); // 统计
            }

        }

        return item;
    }

    /**
     * 项目成本汇总
     * 
     * @param depIds
     * @param year
     * @return
     */
    private CostCollectionItem[] loadDeparmentPrjCosts(List<Long> depIds, int year) {
        CostCollectionItem item = new CostCollectionItem("", "主营业务成本");
        CostCollectionItem item1 = new CostCollectionItem("", "主营业务人月数");

        List<PrjMonthBudget> monthBudgets = prjBudgetService.getPrjMonthBudgetsByDepId(depIds, year);
        for (int i = 0; i < monthBudgets.size(); i++) {
            PrjMonthBudget budget = monthBudgets.get(i);
            item.addAmount(budget.getMonth(), budget.getAmount());
            item1.addAmount(budget.getMonth(), BigDecimal.valueOf(budget.getManMonth()));
        }

        CostCollectionItem items[] = new CostCollectionItem[2];
        items[0] = item;
        items[1] = item1;
        return items;
    }

    /***
     * // 确权收入统计
     */
    private CostCollectionItem loadDeparmentPrjRightConfirms(List<Long> depIds, int year) {

        CostCollectionItem item = new CostCollectionItem("", "主营业务收入");
        List<PrjRightsConfirm> confirms = prjBudgetService.getPrjRightsConfirmByDepId(depIds, year);

        ArrayList<Long> prjIds = new ArrayList<Long>();

        for (int i = 0; i < confirms.size(); i++) {
            PrjRightsConfirm confirm = confirms.get(i);
            Date date = confirm.getExpectDate();
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            int month = calendar.get(Calendar.MONTH);
            BigDecimal amount = confirm.getAmount();
            item.addAmount(month, amount);

            Long prjId = confirm.getPrjId();
            if (!prjIds.contains(prjId))
                prjIds.add(prjId);
        }
        return item;
    }

    /**
     * 现金流汇总
     */
    private CostCollectionItem loadDeparmentPrjIncomes(List<Long> depIds, int year) {

        CostCollectionItem item = new CostCollectionItem("", "现金流");
        List<PrjIncomeForecast> incomes = prjBudgetService.getPrjIncomesByDepId(depIds, year);

        ArrayList<Long> prjIds = new ArrayList<Long>();

        for (int i = 0; i < incomes.size(); i++) {
            PrjIncomeForecast income = incomes.get(i);
            Date date = income.getExpectDate();
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            int month = calendar.get(Calendar.MONTH);
            BigDecimal amount = income.getAmount();
            item.addAmount(month, amount);

            Long prjId = income.getPrjId();
            if (!prjIds.contains(prjId))
                prjIds.add(prjId);
        }
        return item;
    }
}