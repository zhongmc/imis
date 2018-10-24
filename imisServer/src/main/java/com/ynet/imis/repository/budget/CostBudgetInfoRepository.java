package com.ynet.imis.repository.budget;

import java.util.List;

import com.ynet.imis.domain.budget.CostBudgetInfo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CostBudgetInfoRepository extends JpaRepository<CostBudgetInfo, Long> {

    @Query("select a from CostBudgetInfo a where a.depId=?1 and a.budgetTypeId=?2 and a.year=?3")
    List<CostBudgetInfo> getDepartmentCostBudgetInfo(Long depId, Long typeId, int year);

    @Query("select a from CostBudgetInfo a where a.depId=?1 and a.year=?2")
    List<CostBudgetInfo> getDepartmentCostCollection(Long depId, int year);

    @Query("select a from CostBudgetInfo a where a.depId in ?1 and a.costItemId=?2 and a.year=?3")
    List<CostBudgetInfo> getDepartmentCostCollectionByItem(List<Long> depId, Long itemId, int year);
}