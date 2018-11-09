/**
* BudgetService.java
* @author ZHONGMC
* @description 
* @created Wed Oct 10 2018 11:11:50 GMT+0800 (中国标准时间)
* @copyright YNET
* @last-modified Thu Nov 08 2018 16:30:05 GMT+0800 (中国标准时间)
*/

package com.ynet.imis.service.budget;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.ynet.imis.domain.budget.PrjBudget;
import com.ynet.imis.domain.budget.PrjCommBudget;
import com.ynet.imis.domain.budget.PrjIncomeForecast;
import com.ynet.imis.domain.budget.PrjMonthBudget;
import com.ynet.imis.domain.budget.PrjRightsConfirm;

public interface PrjBudgetService {

    /**
     * 项目预算信息
     */
    public PrjBudget getProjectBudgetByPrjId(Long prjId);

    public int saveProjectBudget(PrjBudget prjBudget);

    // 来自部门预算页面，需要同时更新prjBudget里的avg manmonth cost 和 prjMonthBudget; 这里的month budget
    // 不全，不能直接save
    public int savePrjMonthBudgets(PrjBudget prjBudget);

    public List<PrjCommBudget> getProjectCommBudgets(Long prjId);

    public PrjCommBudget savePrjCommBudget(PrjCommBudget prjCommBudget);

    public int deletePrjCommBudget(Long id);

    public List<PrjIncomeForecast> getPrjIncomeForecasts(Long prjId);

    public PrjIncomeForecast savePrjIncomeForecast(PrjIncomeForecast prjIncomeForecast);

    public int deletePrjIncomeForecast(Long id);

    public List<PrjRightsConfirm> getPrjRightsConfirms(Long prjId);

    public PrjRightsConfirm savePrjRightsConfirm(PrjRightsConfirm prjRightsConfirm);

    public int deletePrjRightsConfirm(Long id);

    // 根据部门id 取所有的项目预算
    public List<PrjMonthBudget> getPrjMonthBudgetsByDepId(List<Long> depIds, int year);

    // 根据部门id 取所有的当年确权项目的预算
    public List<PrjMonthBudget> getConfirmedPrjMonthBudgetsByDepId(List<Long> depIds, int year);

    // 根据部门id 取所有的项目确权
    public List<PrjRightsConfirm> getPrjRightsConfirmByDepId(List<Long> depIds, int year);

    // 根据部门id 取所有的项目现金收入
    public List<PrjIncomeForecast> getPrjIncomesByDepId(List<Long> depIds, int year);

    // 根据prjId 取所有的预算
    public List<PrjMonthBudget> getPrjMonthBudgetsByPrjId(List<Long> prjIds, int year);

    public PrjIncomeForecast confirmPrjIncome(Long incomeId, Date date, BigDecimal amount);

    public PrjRightsConfirm confirmPrjRight(Long rightId, Date date, BigDecimal amount);

    // 清理数据，导入错误时用？
    public void deleteAll();

}