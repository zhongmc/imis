/**
* PrjChanceBudgetRepository.java
* @author ZHONGMC
* @description 
* @created Thu Nov 29 2018 13:59:14 GMT+0800 (中国标准时间)
* @copyright YNET
* @last-modified Tue Dec 04 2018 08:43:59 GMT+0800 (中国标准时间)
*/

package com.ynet.imis.repository.budget;

import java.math.BigDecimal;
import java.util.List;

import com.ynet.imis.domain.budget.PrjChanceBudget;

import org.apache.ibatis.annotations.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface PrjChanceBudgetRepository
                extends JpaRepository<PrjChanceBudget, Long>, JpaSpecificationExecutor<PrjChanceBudget> {

        @Query("select a from PrjChanceBudget a where a.prjChanceId=?1")
        public PrjChanceBudget getProjectChanceBudgetByPrjId(Long prjId);

        @Query("select a from PrjChanceBudget a where a.depId=?1")
        public List<PrjChanceBudget> getDepPrjChanceBudgets(Long depId);

        @Modifying
        @Query("update PrjChanceBudget a set a.avgManMonthCost= :amount where a.id=:id")
        public void updateManmonthCoat(@Param(value = "id") Long id, @Param(value = "amount") BigDecimal amount);

        @Modifying
        @Query("update PrjChanceBudget a set a.contractAmount=:contractAmount, a.chance=:chance where a.prjChanceId = :prjChanceId")
        public void updatePrjChanceChance(@Param(value = "prjChanceId") Long prjChanceId,
                        @Param(value = "contractAmount") BigDecimal contractAmount,
                        @Param(value = "chance") short chance);

        @Query("select a from PrjChanceBudget a where a.depId in ?1")
        public List<PrjChanceBudget> getNormalBudgetsByDepIds(List<Long> depIds);

        @Modifying
        @Query("delete from PrjChanceBudget a where a.prjChanceId=?1")
        public void deleteAllByPrjId(Long prjChanceId);
}