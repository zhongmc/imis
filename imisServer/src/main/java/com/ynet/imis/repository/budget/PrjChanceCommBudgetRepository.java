package com.ynet.imis.repository.budget;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.Modifying;

import com.ynet.imis.domain.budget.PrjChanceCommBudget;

public interface PrjChanceCommBudgetRepository extends JpaRepository<PrjChanceCommBudget, Long> {

    @Query("select a from PrjChanceCommBudget a where a.prjChanceId=?1")
    public List<PrjChanceCommBudget> getAllPrjChanceCommBudgetsByPrjId(Long prjChanceId);

    @Modifying
    @Query("delete from PrjChanceCommBudget a where a.prjChanceId =?1")
    public void deleteByPrjChanceId(Long id);

    @Query("select a from PrjChanceCommBudget a where a.depId in ?1 and year(a.expectDate) = ?2")
    public List<PrjChanceCommBudget> getAllPrjChanceCommBudgetsByDepIds(List<Long> depIds, int year);
}