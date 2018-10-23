package com.ynet.imis.service.budget;

import java.util.List;
import java.util.Map;

import com.ynet.imis.domain.budget.BudgetType;
import com.ynet.imis.domain.budget.CostGroup;
import com.ynet.imis.domain.budget.CostItem;

public interface BudgetAdminService {

    public Map<String, Object> getMyBudgetSettings();

    public BudgetType addBudgetType(BudgetType bt);

    public int updateBudgetType(BudgetType bt);

    public int deleteBudgetTypeById(Long id);

    public BudgetType getBudgetTypeById(Long id);

    public CostGroup addCostGroup(CostGroup costGroup);

    public int updateCostGroup(CostGroup costGroup);

    public int deleteCostGroupById(Long id);

    public CostGroup getCostGroupById(Long id);

    public CostItem addCostItem(CostItem costItem);

    public int updateCostItem(CostItem costItem);

    public int deleteCostItemById(Long id);

    public CostItem getCostItemById(Long id);

    public List<CostItem> getCostItems();

}