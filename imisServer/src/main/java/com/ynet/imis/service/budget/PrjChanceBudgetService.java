/**
* PrjChanceBudgetService.java
* @author ZHONGMC
* @description 
* @created Thu Nov 29 2018 14:37:05 GMT+0800 (中国标准时间)
* @copyright YNET
* @last-modified Mon Dec 03 2018 19:25:36 GMT+0800 (中国标准时间)
*/

package com.ynet.imis.service.budget;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.ynet.imis.domain.budget.PrjChanceBudget;
import com.ynet.imis.domain.budget.PrjChanceMonthBudget;
import com.ynet.imis.domain.budget.PrjChanceRightsConfirm;

public interface PrjChanceBudgetService {

    /**
     * 项目预算信息
     */
    public PrjChanceBudget getProjectChanceBudgetByPrjId(Long prjId);

    public List<PrjChanceBudget> getPrjChanceBudgetByDepIds(List<Long> depIds);

    public int saveProjectChanceBudget(PrjChanceBudget prjChanceBudget);

    // 来自部门预算页面，需要同时更新prjBudget里的avg manmonth cost 和 prjMonthBudget; 这里的month budget
    // 不全，不能直接save
    public int savePrjChanceMonthBudgets(PrjChanceBudget prjChanceBudget);

    public List<PrjChanceRightsConfirm> getPrjChanceRightsConfirms(Long prjId);

    public PrjChanceRightsConfirm savePrjChanceRightsConfirm(PrjChanceRightsConfirm prjChanceRightsConfirm);

    public int deletePrjChanceRightsConfirm(Long id);

    // 根据部门id 取所有的项目预算
    public List<PrjChanceMonthBudget> getPrjChanceMonthBudgetsByDepId(List<Long> depIds, int year);

    // 根据部门id 取所有的当年确权项目的预算
    public List<PrjChanceMonthBudget> getConfirmedPrjChanceMonthBudgetsByDepId(List<Long> depIds, int year);

    // 根据部门id 取所有的项目确权
    public List<PrjChanceRightsConfirm> getPrjChanceRightsConfirmByDepId(List<Long> depIds, int year);

    // 根据prjId 取所有的预算
    public List<PrjChanceMonthBudget> getPrjChanceMonthBudgetsByPrjId(List<Long> prjIds, int year);

    public PrjChanceRightsConfirm confirmPrjChanceRight(Long rightId, Date date, BigDecimal amount);

    // 清理数据，导入错误时用？
    public void deleteAll();

}