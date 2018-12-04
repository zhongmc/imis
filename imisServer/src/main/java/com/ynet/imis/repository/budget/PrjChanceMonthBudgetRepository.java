/**
* PrjChanceMonthBudgetRepository.java
* @author ZHONGMC
* @description 
* @created Thu Nov 29 2018 14:00:35 GMT+0800 (中国标准时间)
* @copyright YNET
* @last-modified Tue Dec 04 2018 08:49:12 GMT+0800 (中国标准时间)
*/

package com.ynet.imis.repository.budget;

import java.util.List;

import com.ynet.imis.domain.budget.PrjChanceMonthBudget;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface PrjChanceMonthBudgetRepository extends JpaRepository<PrjChanceMonthBudget, Long> {

    @Query("select a from PrjChanceMonthBudget a where a.depId in ?1 and a.year = ?2 order by a.id") // 某年发生的成本
    public List<PrjChanceMonthBudget> getPrjChanceMonthBudgetsByDepId(List<Long> depIds, int year);

    @Query("select a from PrjChanceMonthBudget a where a.depId in ?1 and a.confirmYear = ?2 order by a.id") // 某年确权的成本

    public List<PrjChanceMonthBudget> getConfirmedPrjChanceMonthBudgetsByDepId(List<Long> depIds, int year);

    @Query("select a from PrjChanceMonthBudget a where a.prjChanceId in ?1 and a.year = ?2")
    public List<PrjChanceMonthBudget> getPrjChanceMonthBudgetsByPrjId(List<Long> prjChanceIds, int year);

    @Modifying
    @Query("update PrjChanceMonthBudget a set a.confirmYear = ?2 where a.prjChanceId=?1 and (12*a.year+a.month)>=?3 and (12*a.year+a.month)<=?4")
    public void updatePrjChanceCostConfirmYear(Long prjChanceId, int year, int beg, int end);

    @Modifying
    @Query("delete from PrjChanceMonthBudget a where a.prjChanceId=?1")
    public void deleteAllByPrjId(Long prjChanceId);

}