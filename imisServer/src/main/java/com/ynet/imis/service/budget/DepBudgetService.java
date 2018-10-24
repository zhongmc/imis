package com.ynet.imis.service.budget;

import java.util.List;

import com.ynet.imis.domain.budget.CostBudgetInfo;
import com.ynet.imis.domain.budget.DepCommBudget;

public interface DepBudgetService {

    public CostBudgetInfo addCostBudgetInfo(CostBudgetInfo costBudgetInfo);

    public int addAllCostBudgetInfos(List<CostBudgetInfo> costBudgetInfos);

    public List<CostBudgetInfo> getCostBudgetInfosByDepId(Long depId, Long typeId, int year);

    public List<CostBudgetInfo> getCostCollectionByDepId(Long depId, int year);

    public List<CostBudgetInfo> getCostCollectionByItemDepId(Long depId, Long itemId, int year);

    public List<DepCommBudget> getDepCommBudgets(Long depId);

    public DepCommBudget saveDepCommBudget(DepCommBudget depCommBudget);

    public int deleteDepCommBudget(Long id);

    public List<Object> getDepPrjBudgets(Long depId);

}