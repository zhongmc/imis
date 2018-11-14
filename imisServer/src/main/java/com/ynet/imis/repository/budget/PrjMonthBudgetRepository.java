/**
* PrjMonthBudgetRepository.java
* @author ZHONGMC
* @description 
* @created Wed Oct 10 2018 10:51:16 GMT+0800 (中国标准时间)
* @copyright YNET
* @last-modified Mon Nov 12 2018 10:27:51 GMT+0800 (中国标准时间)
*/

package com.ynet.imis.repository.budget;

import java.util.List;

import com.ynet.imis.domain.budget.PrjMonthBudget;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface PrjMonthBudgetRepository extends JpaRepository<PrjMonthBudget, Long> {

    @Query("select a from PrjMonthBudget a where a.depId in ?1 and a.year = ?2 order by a.id") // 某年发生的成本
    public List<PrjMonthBudget> getPrjMonthBudgetsByDepId(List<Long> depIds, int year);

    @Query("select a from PrjMonthBudget a where a.depId in ?1 and a.confirmYear = ?2 order by a.id") // 某年确权的成本

    public List<PrjMonthBudget> getConfirmedPrjMonthBudgetsByDepId(List<Long> depIds, int year);

    @Query("select a from PrjMonthBudget a where a.prjId in ?1 and a.year = ?2")
    public List<PrjMonthBudget> getPrjMonthBudgetsByPrjId(List<Long> prjIds, int year);

    @Modifying
    @Query("update PrjMonthBudget a set a.confirmYear = ?2 where a.prjId=?1 and (12*a.year+a.month)>=?3 and (12*a.year+a.month)<=?4")
    public void updatePrjCostConfirmYear(Long prjId, int year, int beg, int end);
}