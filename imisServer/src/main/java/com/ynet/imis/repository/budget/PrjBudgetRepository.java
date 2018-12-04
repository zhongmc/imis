/**
* PrjBudgetRepository.java
* @author ZHONGMC
* @description 
* @created Wed Oct 10 2018 10:52:05 GMT+0800 (中国标准时间)
* @copyright YNET
* @last-modified Fri Nov 30 2018 14:20:04 GMT+0800 (中国标准时间)
*/

package com.ynet.imis.repository.budget;

import java.math.BigDecimal;
import java.util.List;

import com.ynet.imis.domain.budget.PrjBudget;

import org.apache.ibatis.annotations.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface PrjBudgetRepository extends JpaRepository<PrjBudget, Long>, JpaSpecificationExecutor<PrjBudget> {

    @Query("select a from PrjBudget a where a.prjId=?1")
    public PrjBudget getProjectBudgetByPrjId(Long prjId);

    @Query("select a from PrjBudget a where a.depId=?1")
    public List<PrjBudget> getDepPrjBudgets(Long depId);

    @Modifying
    @Query("update PrjBudget a set a.avgManMonthCost= :amount where a.id=:id")
    public void updateManmonthCoat(@Param(value = "id") Long id, @Param(value = "amount") BigDecimal amount);
}