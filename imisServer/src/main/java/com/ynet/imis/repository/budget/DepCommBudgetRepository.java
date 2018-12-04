/**
* DepCommBudgetRepository.java
* @author ZHONGMC
* @description 
* @created Wed Oct 10 2018 10:52:50 GMT+0800 (中国标准时间)
* @copyright YNET
* @last-modified Fri Nov 30 2018 14:58:18 GMT+0800 (中国标准时间)
*/

package com.ynet.imis.repository.budget;

import java.util.List;

import com.ynet.imis.domain.budget.DepCommBudget;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface DepCommBudgetRepository extends JpaRepository<DepCommBudget, Long> {

    @Query("select a from DepCommBudget a where a.depId=?1")
    public List<DepCommBudget> getAllDepCommBudgetsByDepId(Long depId);

    @Query("select a from DepCommBudget a where a.depId in ?1 and year(a.expectDate) = ?2")
    public List<DepCommBudget> getDepCommBudgetsOfYear(List<Long> depIds, int year);
}