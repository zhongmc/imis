package com.ynet.imis.service.budget;

import java.util.List;
import java.util.Map;

import com.ynet.imis.domain.budget.CostBudgetInfo;
import com.ynet.imis.domain.budget.DepCommBudget;

import org.springframework.data.domain.Page;

public interface DepBudgetService {

    public CostBudgetInfo addCostBudgetInfo(CostBudgetInfo costBudgetInfo);

    public int addAllCostBudgetInfos(List<CostBudgetInfo> costBudgetInfos);

    public List<CostBudgetInfo> getCostBudgetInfosByDepId(Long depId, Long typeId, int year);

    public List<CostBudgetInfo> getCostCollectionByDepId(Long depId, int year);

    public List<CostBudgetInfo> getCostCollectionByItemDepId(List<Long> depId, Long itemId, int year);

    public List<DepCommBudget> getDepCommBudgets(Long depId);

    public DepCommBudget saveDepCommBudget(DepCommBudget depCommBudget);

    public int deleteDepCommBudget(Long id);

    public List<Object> getDepPrjBudgets(Long depId);

    Page<Map<String, Object>> getDepPrjBudgetByPage(int page, int size, Long depId, int year);

}