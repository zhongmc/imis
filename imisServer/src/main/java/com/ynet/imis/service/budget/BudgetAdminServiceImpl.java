package com.ynet.imis.service.budget;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ynet.imis.domain.budget.BudgetType;
import com.ynet.imis.domain.budget.CostGroup;
import com.ynet.imis.domain.budget.CostItem;
import com.ynet.imis.repository.budget.BudgetTypeRepository;
import com.ynet.imis.repository.budget.CostGroupRepository;
import com.ynet.imis.repository.budget.CostItemRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class BudgetAdminServiceImpl implements BudgetAdminService {

    @Autowired
    private BudgetTypeRepository budgetTypeDao;

    @Autowired
    private CostGroupRepository costGroupDao;

    @Autowired
    private CostItemRepository costItemDao;

    @Override
    public Map<String, Object> getMyBudgetSettings() {

        List<CostGroup> costGroups = costGroupDao.findAll();
        List<CostItem> costItems = costItemDao.findAll();
        List<BudgetType> budgetTypes = budgetTypeDao.findAll();

        Map<String, Object> retMap = new HashMap<String, Object>();

        retMap.put("costGroups", costGroups);
        retMap.put("costItems", costItems);
        retMap.put("budgetTypes", budgetTypes);

        return retMap;
    }

    @Override
    public List<CostGroup> getCostGroups() {
        return costGroupDao.findAll();
    }

    @Override
    public BudgetType addBudgetType(BudgetType bt) {
        return budgetTypeDao.save(bt);
    }

    @Override
    public int deleteBudgetTypeById(Long id) {
        budgetTypeDao.deleteById(id);
        return 1;
    }

    @Override
    public BudgetType getBudgetTypeById(Long id) {
        return budgetTypeDao.findById(id).get();
    }

    @Override
    public CostGroup addCostGroup(CostGroup costGroup) {
        return costGroupDao.save(costGroup);
    }

    @Override
    public int deleteCostGroupById(Long id) {
        costGroupDao.deleteById(id);
        return 1;
    }

    @Override
    public CostGroup getCostGroupById(Long id) {
        return costGroupDao.findById(id).get();
    }

    @Override
    public CostItem addCostItem(CostItem costItem) {
        return costItemDao.save(costItem);
    }

    @Override
    public int deleteCostItemById(Long id) {
        costItemDao.deleteById(id);
        return 1;
    }

    @Override
    public CostItem getCostItemById(Long id) {
        return costItemDao.findById(id).get();
    }

    @Override
    public List<CostItem> getCostItems() {
        return costItemDao.findAll();
    }

    @Override
    public int updateBudgetType(BudgetType bt) {
        budgetTypeDao.save(bt);
        return 1;
    }

    @Override
    public int updateCostGroup(CostGroup costGroup) {
        costGroupDao.save(costGroup);
        return 1;
    }

    @Override
    public int updateCostItem(CostItem costItem) {
        costItemDao.save(costItem);
        return 1;
    }

}
