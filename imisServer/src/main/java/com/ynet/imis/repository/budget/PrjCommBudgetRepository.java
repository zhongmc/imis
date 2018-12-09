/**
* PrjCommBudgetRepository.java
* @author ZHONGMC
* @description 
* @created Wed Oct 10 2018 10:51:42 GMT+0800 (中国标准时间)
* @copyright YNET
* @last-modified Sun Dec 09 2018 17:25:19 GMT+0800 (中国标准时间)
*/

package com.ynet.imis.repository.budget;

import java.util.List;

import com.ynet.imis.domain.budget.PrjCommBudget;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface PrjCommBudgetRepository extends JpaRepository<PrjCommBudget, Long> {

    @Query("select a from PrjCommBudget a where a.prjId=?1")
    public List<PrjCommBudget> getAllPrjCommBudgetsByPrjId(Long prjId);

    @Modifying
    @Query("delete from PrjCommBudget a where a.prjId =?1")
    public void deleteByPrjId(Long id);

    @Query("select a from PrjCommBudget a where a.depId in ?1 and year(a.expectDate) = ?2")
    public List<PrjCommBudget> getPrjCommBudgetsByDepId(List<Long> depIds, int year);
}