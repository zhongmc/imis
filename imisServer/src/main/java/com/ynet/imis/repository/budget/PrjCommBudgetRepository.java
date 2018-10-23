/**
* PrjCommBudgetRepository.java
* @author ZHONGMC
* @description 
* @created Wed Oct 10 2018 10:51:42 GMT+0800 (中国标准时间)
* @copyright YNET
* @last-modified Thu Oct 18 2018 15:02:35 GMT+0800 (中国标准时间)
*/

package com.ynet.imis.repository.budget;

import java.util.List;

import com.ynet.imis.domain.budget.PrjCommBudget;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface PrjCommBudgetRepository extends JpaRepository<PrjCommBudget, Long> {

    @Query("select a from PrjCommBudget a where a.prjId=?1")
    public List<PrjCommBudget> getAllPrjCommBudgetsByPrjId(Long prjId);
}