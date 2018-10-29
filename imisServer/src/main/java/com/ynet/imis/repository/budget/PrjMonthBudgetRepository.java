/**
* PrjMonthBudgetRepository.java
* @author ZHONGMC
* @description 
* @created Wed Oct 10 2018 10:51:16 GMT+0800 (中国标准时间)
* @copyright YNET
* @last-modified Thu Oct 25 2018 17:06:10 GMT+0800 (中国标准时间)
*/

package com.ynet.imis.repository.budget;

import java.util.List;

import com.ynet.imis.domain.budget.PrjMonthBudget;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface PrjMonthBudgetRepository extends JpaRepository<PrjMonthBudget, Long> {

    @Query("select a from PrjMonthBudget a where a.depId in ?1 and a.year = ?2")
    List<PrjMonthBudget> getPrjMonthBudgetsByDepId(List<Long> depIds, int year);

    @Query("select a from PrjMonthBudget a where a.prjId in ?1 and a.year = ?2")
    List<PrjMonthBudget> getPrjMonthBudgetsByPrjId(List<Long> prjIds, int year);
}