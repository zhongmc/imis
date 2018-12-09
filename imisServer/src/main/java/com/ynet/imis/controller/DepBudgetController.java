package com.ynet.imis.controller;

import java.math.BigDecimal;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Calendar;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.mail.internet.MimeUtility;
import javax.servlet.http.HttpServletRequest;

import com.ynet.imis.bean.AmountCollection;
import com.ynet.imis.bean.CostCollectionItem;
import com.ynet.imis.domain.budget.BudgetType;
import com.ynet.imis.domain.budget.CostBudgetInfo;
import com.ynet.imis.domain.budget.CostGroup;
import com.ynet.imis.domain.budget.CostItem;
import com.ynet.imis.domain.budget.DepCommBudget;
import com.ynet.imis.domain.budget.PrjBudget;
import com.ynet.imis.domain.budget.PrjChanceBudget;
import com.ynet.imis.domain.budget.PrjChanceCommBudget;
import com.ynet.imis.domain.budget.PrjChanceMonthBudget;
import com.ynet.imis.domain.budget.PrjChanceRightsConfirm;
import com.ynet.imis.domain.budget.PrjCommBudget;
import com.ynet.imis.domain.budget.PrjIncomeForecast;
import com.ynet.imis.domain.budget.PrjMonthBudget;
import com.ynet.imis.domain.budget.PrjRightsConfirm;
import com.ynet.imis.domain.project.Project;
import com.ynet.imis.domain.project.ProjectChance;
import com.ynet.imis.service.budget.BudgetAdminService;
import com.ynet.imis.service.budget.DepBudgetService;
import com.ynet.imis.service.budget.PrjBudgetService;
import com.ynet.imis.service.budget.PrjChanceBudgetService;
import com.ynet.imis.service.org.DepartmentService;
import com.ynet.imis.service.project.ProjectChanceService;
import com.ynet.imis.service.project.ProjectService;
import com.ynet.imis.utils.ImisUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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
    private ProjectService projectService;

    @Autowired
    private PrjChanceBudgetService prjChanceBudgetService;

    @Autowired
    private ProjectChanceService projectChanceService;

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
                info.setCostGroupId(costItem.getGroupId());

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

        // logger.info(ImisUtils.objectJsonStr(pagedPrjBudgets));
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

    @RequestMapping(value = "/dep/prjChance/paged", method = RequestMethod.GET)
    public Page<Map<String, Object>> getPagedDepProjectChanceBudgets(@RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "10") Integer size, Long depId) {

        int year = Calendar.getInstance().get(Calendar.YEAR);

        Page<Map<String, Object>> pagedPrjBudgets = depBudgetService.getDepPrjChanceBudgetByPage(page, size, depId,
                year);

        // logger.info(ImisUtils.objectJsonStr(pagedPrjBudgets));
        return pagedPrjBudgets;
    }

    @RequestMapping(value = "/dep/prjChance", method = RequestMethod.POST)
    public ResponseBean updateDepPrjChanceBudgets(@RequestBody PrjChanceBudget prjChanceBudget) {
        logger.info("update dep project chance budget: \n" + ImisUtils.objectJsonStr(prjChanceBudget));

        // List<PrjMonthBudget> prjMonthBudgets = prjBudget.getMonthBudgets();
        int ret = prjChanceBudgetService.savePrjChanceMonthBudgets(prjChanceBudget);

        if (ret == 1) {
            return new ResponseBean("success", "更新成功!");
        }
        return new ResponseBean("error", "更新失败!");

    }

    // 部门预算汇总表下载
    @RequestMapping(value = "/budgetTable/export/{depId}", method = RequestMethod.GET)
    public ResponseEntity<byte[]> exportDepBudgetTable(HttpServletRequest request, @PathVariable Long depId) {

        List<CostCollectionItem> costCollect = getCostBudgetInfosByDepId(depId);
        List<CostCollectionItem> budgetTable = this.getBudgetTableDepId(depId);

        Map<String, List<CostCollectionItem>> costCollMap = new HashMap<String, List<CostCollectionItem>>();
        costCollMap.put("部门预算表", budgetTable);
        costCollMap.put("部门费用汇总表", costCollect);

        try {

            String encodedFileName = null;

            String fileName = "部门预算表.xlsx";
            String userAgentString = request.getHeader("User-Agent");

            boolean knownBrowser = false;
            if (userAgentString.indexOf("Chrome") != -1)
                knownBrowser = true;
            else if (userAgentString.indexOf("Internet Exploer") != -1)
                knownBrowser = true;
            else if (userAgentString.indexOf("Safari") != -1)
                knownBrowser = true;

            if (knownBrowser) {

                encodedFileName = URLEncoder.encode(fileName, "utf-8").replaceAll("\\+", "%20");

            } else {

                encodedFileName = MimeUtility.encodeWord(fileName);

            }

            HttpHeaders headers = new HttpHeaders();
            headers.setContentDispositionFormData("attachment", encodedFileName);
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);

            byte[] body = ImisUtils.exportCostCollection2Excel(costCollMap);

            return new ResponseEntity<byte[]>(body, headers, HttpStatus.OK);

        } catch (Exception e) {
            logger.error("failed to export!", e);

            return new ResponseEntity<byte[]>(("Internal error:" + e).getBytes(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        // return PoiUtils.exportEmp2Excel(empService.getAllEmployees());
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

        List<DepCommBudget> commBudgets = depBudgetService.getDepCommBudgetsOfYear(depIds, year); // (depId);
        CostCollectionItem citem = new CostCollectionItem("", "部门公共预算");
        for (int i = 0; i < commBudgets.size(); i++) {
            DepCommBudget dCb = commBudgets.get(i);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(dCb.getExpectDate());
            citem.addAmount(calendar.get(Calendar.MONTH), dCb.getAmount());
            sumItem.addAmount(calendar.get(Calendar.MONTH), dCb.getAmount()); // 统计
        }

        items.add(citem);
        // logger.info(" dep cost collection: " + ImisUtils.objectJsonStr(items));
        return items;
    }

    // 多部门损益对比表
    @RequestMapping(value = "/dep/chart", method = RequestMethod.POST)
    public List<AmountCollection> getDepCharts(Long[] depIds) {

        logger.info("load department chart for: " + ImisUtils.objectJsonStr(depIds));

        List<AmountCollection> amcs = new ArrayList<AmountCollection>();
        if (depIds == null || depIds.length == 0)
            return amcs;
        int size = depIds.length;
        List<CostCollectionItem> items = getBudgetTableDepId(depIds[0]);
        for (int i = 0; i < items.size(); i++) {
            AmountCollection amc = AmountCollection.CopyFrom(items.get(i), size);
            amcs.add(amc);
        }

        for (int k = 1; k < depIds.length; k++) {

            items = getBudgetTableDepId(depIds[k]);

            for (int i = 0; i < items.size(); i++) {
                AmountCollection amc = amcs.get(i);
                amc.setAmount(k, items.get(i).getSum());
            }

        }

        logger.info("Load Ok: " + ImisUtils.objectJsonStr(amcs));
        return amcs;
    }

    // 多部门损益对比表下载
    @RequestMapping(value = "/dep/chartExport", method = RequestMethod.GET)
    public ResponseEntity<byte[]> exportDepChartTable(HttpServletRequest request, Long[] depIds) {

        List<AmountCollection> amcs = getDepCharts(depIds);
        String depNames[] = new String[depIds.length];
        for (int i = 0; i < depIds.length; i++) {
            depNames[i] = departmentService.getDepartmentName(depIds[i]);
        }

        try {

            String encodedFileName = null;

            String fileName = "部门预算损益对比表.xlsx";
            String userAgentString = request.getHeader("User-Agent");

            boolean knownBrowser = false;
            if (userAgentString.indexOf("Chrome") != -1)
                knownBrowser = true;
            else if (userAgentString.indexOf("Internet Exploer") != -1)
                knownBrowser = true;
            else if (userAgentString.indexOf("Safari") != -1)
                knownBrowser = true;

            if (knownBrowser) {

                encodedFileName = URLEncoder.encode(fileName, "utf-8").replaceAll("\\+", "%20");

            } else {

                encodedFileName = MimeUtility.encodeWord(fileName);

            }

            HttpHeaders headers = new HttpHeaders();
            headers.setContentDispositionFormData("attachment", encodedFileName);
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);

            byte[] body = ImisUtils.exportDepBudgetChart2Excel(depNames, amcs);

            return new ResponseEntity<byte[]>(body, headers, HttpStatus.OK);

        } catch (Exception e) {
            logger.error("failed to export!", e);

            return new ResponseEntity<byte[]>(("Internal error:" + e).getBytes(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        // return PoiUtils.exportEmp2Excel(empService.getAllEmployees());
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
        // 合同收入、主营业务收入、增值税
        CostCollectionItem items2[] = this.loadDeparmentPrjRightConfirms(depIds, year); // shoud have prjIds return
        // new CostCollectionItem("", "主营业务收入");

        // 外采统计合同、金额、税
        CostCollectionItem[] items3 = loadDeparmentPrjCommBudgets(depIds, year);

        // 增值税
        // CostCollectionItem item3 = item2.multiply(BigDecimal.valueOf(0.06));
        // item3.setName("增值税");

        // 主营业务成本
        CostCollectionItem itms[] = this.loadConfirmedDeparmentPrjCosts(depIds, year);
        CostCollectionItem itms1[] = this.loadDeparmentPrjCosts(depIds, year);
        CostCollectionItem item4 = itms[0];
        // 主营业务递延成本
        CostCollectionItem item41 = itms[2];

        // 毛利
        CostCollectionItem item5 = items2[1].subtract(items3[0]); // 收入 - 外采
        item5 = item5.subtract(item4); // - 主营成本
        item5 = item5.subtract(item41); // - 主营递延
        item5 = item5.add(items3[2]); // + 税抵扣
        item5.setName("毛利");

        // 毛利率
        CostCollectionItem item6 = item5.divide(items2[1]);
        item6.setName("毛利率");

        // 经营费用
        CostCollectionItem item7 = this.loadDepartmentCosts(depIds, year);

        // 其它费用(公共预算)
        CostCollectionItem item8 = this.loadDepartmentCommonCosts(depIds, year); // new CostCollectionItem("", "其它费用");

        // 经营利润
        CostCollectionItem item9 = item5.subtract(item7.add(item8));
        item9.setName("经营利润");

        // // 净利润
        // CostCollectionItem item10 = new CostCollectionItem("", "净利润");

        // 净利润率
        CostCollectionItem item10 = item9.divide(items2[1]);
        item10.setName("净利润率");

        // 现金流
        CostCollectionItem item11 = this.loadDeparmentPrjIncomes(depIds, year);

        // items.add(item1);
        items.add(items2[0]);
        items.add(items2[1]);
        items.add(items2[2]);

        items.add(items3[0]);
        items.add(items3[1]);
        items.add(items3[2]);

        items.add(item4);
        items.add(item41);
        items.add(itms[4]);

        items.add(item5);
        items.add(item6);
        items.add(item7);
        items.add(item8);
        items.add(item9);
        items.add(item10);

        items.add(itms[1]); // 人月
        items.add(itms[3]); // 递延人月

        items.add(itms1[1]); //
        items.add(itms1[0]); //

        items.add(item11); // 现金

        ////////// 机会情况统计

        // CostCollectionItem item = new CostCollectionItem("", "机会合同额");
        // CostCollectionItem item1 = new CostCollectionItem("", "概率后机会合同额");
        // CostCollectionItem item2 = new CostCollectionItem("", "今年机会投入");
        // CostCollectionItem item3 = new CostCollectionItem("", "今年机会投入人月");
        // CostCollectionItem item4 = new CostCollectionItem("", "概率后今年机会投入");
        // CostCollectionItem item5 = new CostCollectionItem("", "概率后今年机会投入人月");
        // CostCollectionItem item6 = new CostCollectionItem("", "确权机会主营业务成本");
        // CostCollectionItem item7 = new CostCollectionItem("", "确权机会主营业务人月数");
        // CostCollectionItem item8 = new CostCollectionItem("", "概率后确权机会主营业务成本");
        // CostCollectionItem item9 = new CostCollectionItem("", "概率后确权机会主营业务人月数");

        CostCollectionItem[] chanceItems = this.loadDeparmentPrjChanceInfo(depIds, year);
        for (int i = 0; i < chanceItems.length; i++)
            items.add(chanceItems[i]);

        // CostCollectionItem item1 = new CostCollectionItem("", "可确权机会合同额");
        // CostCollectionItem item11 = new CostCollectionItem("", "概率后可确权机会合同额");
        // CostCollectionItem item12 = new CostCollectionItem("", "概率后可确权机会收入");
        // CostCollectionItem item13 = new CostCollectionItem("", "概率后可确权机会增值税");

        // CostCollectionItem item2 = new CostCollectionItem("", "外采合同金额");
        // CostCollectionItem item21 = new CostCollectionItem("", "概率后外采合同金额");
        // CostCollectionItem item22 = new CostCollectionItem("", "概率后外采金额");
        // CostCollectionItem item23 = new CostCollectionItem("", "概率后外采增值税抵扣");

        CostCollectionItem[] cits = prjChanceRightsAndCommBudgets(depIds, year); // this.loadDeparmentPrjChanceRightConfirms(depIds,
                                                                                 // year);
        for (int i = 0; i < cits.length; i++)
            items.add(cits[i]);

        // 含机会的主营业务收入
        CostCollectionItem item01 = items2[1].add(cits[2]);
        item01.setName("含机会的主营业务收入");
        // 含机会的外包成本
        CostCollectionItem item02 = items3[1].add(cits[5]);
        item02.setName("含机会的外包成本");
        // 含机会的外包抵扣
        CostCollectionItem item03 = items3[2].add(cits[7]);
        item03.setName("含机会的增值税抵扣");
        // 含机会的主营业务成本
        CostCollectionItem item04 = item4.add(item41);
        item04 = item04.add(chanceItems[8]);
        item04.setName("含机会的主营业务成本");
        // 含机会的毛利
        CostCollectionItem item05 = item01.subtract(item04);
        item05 = item05.subtract(item02);
        item05 = item05.add(item03);
        item05.setName("含机会的毛利");
        // 含机会的毛利率
        CostCollectionItem item06 = item05.divide(item01);
        item06.setName("含机会的毛利率");
        // 含机会的纯利
        CostCollectionItem item07 = item05.subtract(item7);
        item07 = item07.subtract(item8);
        item07.setName("含机会的纯利");

        // 含机会的纯利率
        CostCollectionItem item08 = item07.divide(item01);
        item08.setName("含机会的纯利率");

        items.add(item01);
        items.add(item02);
        items.add(item03);
        items.add(item04);
        items.add(item05);
        items.add(item06);
        items.add(item07);
        items.add(item08);

        return items;
    }

    /**
     * 经营费用汇总
     * 
     * @param depIds
     * @param year
     * @return
     */
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
     * 部门公共预算汇总
     */
    private CostCollectionItem loadDepartmentCommonCosts(List<Long> depIds, int year) {

        List<DepCommBudget> commBudgets = depBudgetService.getDepCommBudgetsOfYear(depIds, year); // (depId);
        CostCollectionItem citem = new CostCollectionItem("", "部门公共预算");
        for (int i = 0; i < commBudgets.size(); i++) {
            DepCommBudget dCb = commBudgets.get(i);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(dCb.getExpectDate());
            citem.addAmount(calendar.get(Calendar.MONTH), dCb.getAmount());
        }
        return citem;
    }

    /***
     * 机会统计
     */
    private CostCollectionItem[] loadDeparmentPrjChanceInfo(List<Long> depIds, int year) {

        CostCollectionItem item = new CostCollectionItem("", "机会合同额");
        CostCollectionItem item1 = new CostCollectionItem("", "概率后机会合同额");

        CostCollectionItem item2 = new CostCollectionItem("", "今年机会投入");
        CostCollectionItem item3 = new CostCollectionItem("", "今年机会投入人月");

        CostCollectionItem item4 = new CostCollectionItem("", "概率后今年机会投入");
        CostCollectionItem item5 = new CostCollectionItem("", "概率后今年机会投入人月");

        CostCollectionItem item6 = new CostCollectionItem("", "确权机会主营业务成本");
        CostCollectionItem item7 = new CostCollectionItem("", "确权机会主营业务人月数");

        CostCollectionItem item8 = new CostCollectionItem("", "概率后确权机会主营业务成本");
        CostCollectionItem item9 = new CostCollectionItem("", "概率后确权机会主营业务人月数");

        List<PrjChanceBudget> prjs = prjChanceBudgetService.getPrjChanceBudgetByDepIds(depIds);
        for (PrjChanceBudget prjBudget : prjs) {
            double chance = (double) prjBudget.getChance() / 100.0;

            BigDecimal bgChance = new BigDecimal(chance);

            item.addAmount(11, prjBudget.getContractAmount());
            item1.addAmount(11, prjBudget.getContractAmount().multiply(bgChance));

            for (PrjChanceMonthBudget monthBudget : prjBudget.getMonthBudgets()) {
                if (monthBudget.getYear() == year) {
                    item2.addAmount(monthBudget.getMonth(), monthBudget.getAmount());
                    item3.addAmount(monthBudget.getMonth(), new BigDecimal(monthBudget.getManMonth()));
                    item4.addAmount(monthBudget.getMonth(), monthBudget.getAmount().multiply(bgChance));
                    item5.addAmount(monthBudget.getMonth(), new BigDecimal(monthBudget.getManMonth() * chance));

                    if (monthBudget.getConfirmYear() == year) {
                        item6.addAmount(monthBudget.getMonth(), monthBudget.getAmount());
                        item7.addAmount(monthBudget.getMonth(), new BigDecimal(monthBudget.getManMonth()));
                        item8.addAmount(monthBudget.getMonth(), monthBudget.getAmount().multiply(bgChance));
                        item9.addAmount(monthBudget.getMonth(), new BigDecimal(monthBudget.getManMonth() * chance));
                    }
                }

            }

        }

        CostCollectionItem[] items = new CostCollectionItem[10];
        items[0] = item;
        items[1] = item1;
        items[2] = item2;
        items[3] = item3;
        items[4] = item4;
        items[5] = item5;

        items[6] = item6;
        items[7] = item7;
        items[8] = item8;
        items[9] = item9;

        return items;

    }

    /***
     * 确权机会收入统计
     */
    /*
     * private CostCollectionItem[] loadDeparmentPrjChanceRightConfirms(List<Long>
     * depIds, int year) {
     * 
     * CostCollectionItem item = new CostCollectionItem("", "可确权机会合同额");
     * CostCollectionItem item1 = new CostCollectionItem("", "概率后可确权机会合同额");
     * CostCollectionItem item2 = new CostCollectionItem("", "概率后可确权机会收入");
     * CostCollectionItem item3 = new CostCollectionItem("", "概率后可确权机会增值税");
     * 
     * List<PrjChanceRightsConfirm> confirms =
     * prjChanceBudgetService.getPrjChanceRightsConfirmByDepId(depIds, year);
     * 
     * Calendar cal = Calendar.getInstance(); for (PrjChanceRightsConfirm confirm :
     * confirms) { cal.setTime(confirm.getExpectDate()); int month =
     * cal.get(Calendar.MONTH); item.addAmount(month, confirm.getAmount());
     * ProjectChance prj = confirm.getProjectChance(); BigDecimal bgChance = new
     * BigDecimal(prj.getChance() / 100.0); item1.addAmount(month,
     * confirm.getAmount().multiply(bgChance)); }
     * 
     * CostCollectionItem[] items = new CostCollectionItem[2]; items[0] = item;
     * items[1] = item1; return items; }
     */
    /***
     * // 机会收入及外采统计
     */
    private CostCollectionItem[] prjChanceRightsAndCommBudgets(List<Long> depIds, int year) {

        // ArrayList<Long> prjIds = new ArrayList<Long>();

        Map<Long, BigDecimal> prjTaxRates = new HashMap<Long, BigDecimal>();
        Map<Long, BigDecimal> prjChances = new HashMap<Long, BigDecimal>();

        CostCollectionItem item1 = new CostCollectionItem("", "可确权机会合同额");
        CostCollectionItem item11 = new CostCollectionItem("", "概率后可确权机会合同额");
        CostCollectionItem item12 = new CostCollectionItem("", "概率后可确权机会收入");
        CostCollectionItem item13 = new CostCollectionItem("", "概率后可确权机会增值税");

        List<PrjChanceRightsConfirm> confirms = prjChanceBudgetService.getPrjChanceRightsConfirmByDepId(depIds, year);

        Calendar cal = Calendar.getInstance();
        for (PrjChanceRightsConfirm confirm : confirms) {
            cal.setTime(confirm.getExpectDate());
            int month = cal.get(Calendar.MONTH);
            item1.addAmount(month, confirm.getAmount());

            BigDecimal rate = new BigDecimal(1.06);
            BigDecimal chance = prjChances.get(confirm.getPrjChanceId());

            if (chance == null) {
                ProjectChance prj = this.projectChanceService.getProjectChanceById(confirm.getPrjChanceId());
                if (prj == null) {
                    logger.error("project chance with id: " + confirm.getPrjChanceId()
                            + " not found! when load confirm: " + confirm.getId());
                } else {
                    rate = new BigDecimal(1.0 + prj.getTaxRate());
                    chance = new BigDecimal(prj.getChance() / 100.00);
                    prjTaxRates.put(prj.getId(), rate);
                    prjChances.put(prj.getId(), chance);
                }
            }

            // ProjectChance prj = confirm.getProjectChance();
            // BigDecimal bgChance = new BigDecimal(prj.getChance() / 100.0);
            BigDecimal chancedAmount = confirm.getAmount().multiply(chance);
            BigDecimal income = chancedAmount.divide(rate, 3);
            BigDecimal tax = chancedAmount.subtract(income);

            item11.addAmount(month, chancedAmount);
            item12.addAmount(month, income);
            item13.addAmount(month, tax);

        }

        CostCollectionItem item2 = new CostCollectionItem("", "外采合同金额");
        CostCollectionItem item21 = new CostCollectionItem("", "概率后外采合同金额");
        CostCollectionItem item22 = new CostCollectionItem("", "概率后外采金额");
        CostCollectionItem item23 = new CostCollectionItem("", "概率后外采增值税抵扣");

        List<PrjChanceCommBudget> commBudgets = prjChanceBudgetService.getPrjChanceCommBudgetsByDepId(depIds, year);

        for (int i = 0; i < commBudgets.size(); i++) {
            PrjChanceCommBudget commBudget = commBudgets.get(i);
            Date date = commBudget.getExpectDate();
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            int month = calendar.get(Calendar.MONTH);
            BigDecimal amount = commBudget.getAmount();
            item2.addAmount(month, amount);

            BigDecimal rate = prjTaxRates.get(commBudget.getPrjChanceId());
            BigDecimal chance = prjChances.get(commBudget.getPrjChanceId());

            if (rate == null) {
                ProjectChance prj = this.projectChanceService.getProjectChanceById(commBudget.getPrjChanceId());
                if (prj == null) {
                    logger.error("project chance with id: " + commBudget.getPrjChanceId()
                            + " not found! when load commBudget: " + commBudget.getId());
                } else {
                    rate = new BigDecimal(1.0 + prj.getTaxRate());
                    chance = new BigDecimal(prj.getChance() / 100.00);
                    prjTaxRates.put(prj.getId(), rate);
                    prjChances.put(prj.getId(), chance);
                }
            }

            BigDecimal chanceAmount = amount.multiply(chance);
            item21.addAmount(month, chanceAmount);

            BigDecimal income = chanceAmount.divide(rate, 3);
            BigDecimal tax = chanceAmount.subtract(income);
            item22.addAmount(month, income);
            item23.addAmount(month, tax);

        }

        CostCollectionItem items[] = new CostCollectionItem[8];
        items[0] = item1;
        items[1] = item11;
        items[2] = item12;
        items[3] = item13;

        items[4] = item2;
        items[5] = item21;
        items[6] = item22;
        items[7] = item23;

        return items;
    }

    /**
     * 当年确权项目成本汇总
     * 
     * @param depIds
     * @param year
     * @return
     */
    private CostCollectionItem[] loadConfirmedDeparmentPrjCosts(List<Long> depIds, int year) {
        CostCollectionItem item = new CostCollectionItem("", "当年主营业务成本");
        CostCollectionItem item1 = new CostCollectionItem("", "主营业务人月数");
        CostCollectionItem item2 = new CostCollectionItem("", "上年递延成本");
        CostCollectionItem item3 = new CostCollectionItem("", "上年递延人月数");
        CostCollectionItem item4;// = new CostCollectionItem("", "主营业务成本");

        List<PrjMonthBudget> monthBudgets = prjBudgetService.getConfirmedPrjMonthBudgetsByDepId(depIds, year);

        // logger.info(ImisUtils.objectJsonStr(monthBudgets));

        for (int i = 0; i < monthBudgets.size(); i++) {
            PrjMonthBudget budget = monthBudgets.get(i);
            if (budget.getYear() != year) {
                if (budget.getRealManMonth() <= 0) {
                    item2.addAmount(budget.getMonth(), budget.getAmount());
                    item3.addAmount(budget.getMonth(), BigDecimal.valueOf(budget.getManMonth()));
                } else {
                    item2.addAmount(budget.getMonth(), budget.getRealAmount());
                    item3.addAmount(budget.getMonth(), BigDecimal.valueOf(budget.getRealManMonth()));

                }

            } else {

                if (budget.getRealManMonth() <= 0) {
                    item.addAmount(budget.getMonth(), budget.getAmount());
                    item1.addAmount(budget.getMonth(), BigDecimal.valueOf(budget.getManMonth()));

                } else {
                    item.addAmount(budget.getMonth(), budget.getRealAmount());
                    item1.addAmount(budget.getMonth(), BigDecimal.valueOf(budget.getRealManMonth()));

                }
            }
        }
        item4 = item.add(item2);
        item4.setName("主营业务成本");
        CostCollectionItem items[] = new CostCollectionItem[5];
        items[0] = item;
        items[1] = item1;
        items[2] = item2;
        items[3] = item3;
        items[4] = item4;

        return items;
    }

    /**
     * 当年项目（主营业务）投入
     * 
     * @param depIds
     * @param year
     * @return
     */
    private CostCollectionItem[] loadDeparmentPrjCosts(List<Long> depIds, int year) {
        CostCollectionItem item = new CostCollectionItem("", "今年主营业务投入");
        CostCollectionItem item1 = new CostCollectionItem("", "今年主营业务投入人月");

        List<PrjMonthBudget> monthBudgets = prjBudgetService.getPrjMonthBudgetsByDepId(depIds, year);
        // logger.info(ImisUtils.objectJsonStr(monthBudgets));

        for (int i = 0; i < monthBudgets.size(); i++) {
            PrjMonthBudget budget = monthBudgets.get(i);
            if (budget.getRealManMonth() <= 0) {
                item.addAmount(budget.getMonth(), budget.getAmount());
                item1.addAmount(budget.getMonth(), BigDecimal.valueOf(budget.getManMonth()));
            } else {
                item.addAmount(budget.getMonth(), budget.getRealAmount());
                item1.addAmount(budget.getMonth(), BigDecimal.valueOf(budget.getRealManMonth()));

            }
        }

        CostCollectionItem items[] = new CostCollectionItem[2];
        items[0] = item;
        items[1] = item1;

        return items;
    }

    /***
     * // 确权收入统计
     */
    private CostCollectionItem[] loadDeparmentPrjRightConfirms(List<Long> depIds, int year) {

        CostCollectionItem item = new CostCollectionItem("", "合同收入");
        CostCollectionItem item1 = new CostCollectionItem("", "主营业务收入");
        CostCollectionItem item2 = new CostCollectionItem("", "增值税");

        List<PrjRightsConfirm> confirms = prjBudgetService.getPrjRightsConfirmByDepId(depIds, year);

        // ArrayList<Long> prjIds = new ArrayList<Long>();

        Map<Long, Double> prjTaxRates = new HashMap<Long, Double>();

        for (int i = 0; i < confirms.size(); i++) {
            PrjRightsConfirm confirm = confirms.get(i);
            Date date = confirm.getExpectDate();
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            int month = calendar.get(Calendar.MONTH);
            BigDecimal amount = confirm.getAmount();
            item.addAmount(month, amount);

            Double rate = prjTaxRates.get(confirm.getPrjId());
            if (rate == null) {
                Project prj = this.projectService.getProjectById(confirm.getPrjId());
                if (prj == null) {
                    logger.error("project with id: " + confirm.getPrjId() + " not found! when load confirm: "
                            + confirm.getId());
                } else {
                    rate = new Double(prj.getTaxRate());
                    prjTaxRates.put(prj.getId(), rate);
                }
            }

            BigDecimal income = amount.divide(new BigDecimal(1.0 + rate.doubleValue()), 3);
            BigDecimal tax = amount.subtract(income);
            item1.addAmount(month, income);
            item2.addAmount(month, tax);

        }

        CostCollectionItem items[] = new CostCollectionItem[3];
        items[0] = item;
        items[1] = item1;
        items[2] = item2;

        return items;
    }

    /***
     * // 外采统计
     */
    private CostCollectionItem[] loadDeparmentPrjCommBudgets(List<Long> depIds, int year) {

        CostCollectionItem item = new CostCollectionItem("", "外采合同金额");
        CostCollectionItem item1 = new CostCollectionItem("", "外采金额");
        CostCollectionItem item2 = new CostCollectionItem("", "增值税抵扣");

        List<PrjCommBudget> commBudgets = prjBudgetService.getPrjCommBudgetsByDepId(depIds, year);

        // ArrayList<Long> prjIds = new ArrayList<Long>();

        Map<Long, Double> prjTaxRates = new HashMap<Long, Double>();

        for (int i = 0; i < commBudgets.size(); i++) {
            PrjCommBudget commBudget = commBudgets.get(i);
            Date date = commBudget.getExpectDate();
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            int month = calendar.get(Calendar.MONTH);
            BigDecimal amount = commBudget.getAmount();
            item.addAmount(month, amount);

            Double rate = prjTaxRates.get(commBudget.getPrjId());
            if (rate == null) {
                Project prj = this.projectService.getProjectById(commBudget.getPrjId());
                if (prj == null) {
                    logger.error("project with id: " + commBudget.getPrjId() + " not found! when load commBudget: "
                            + commBudget.getId());
                } else {
                    rate = new Double(prj.getTaxRate());
                    prjTaxRates.put(prj.getId(), rate);
                }
            }

            BigDecimal income = amount.divide(new BigDecimal(1.0 + rate.doubleValue()), 3);
            BigDecimal tax = amount.subtract(income);
            item1.addAmount(month, income);
            item2.addAmount(month, tax);

        }

        CostCollectionItem items[] = new CostCollectionItem[3];
        items[0] = item;
        items[1] = item1;
        items[2] = item2;

        return items;
    }

    /**
     * 现金流汇总
     */
    private CostCollectionItem loadDeparmentPrjIncomes(List<Long> depIds, int year) {

        CostCollectionItem item = new CostCollectionItem("", "现金收入");
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