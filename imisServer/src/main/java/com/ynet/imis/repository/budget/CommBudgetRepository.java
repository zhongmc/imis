/**
* CommBudgetRepository.java
* @author ZHONGMC
* @description 
* @created Wed Oct 10 2018 10:54:20 GMT+0800 (中国标准时间)
* @copyright YNET
* @last-modified Wed Oct 10 2018 11:08:54 GMT+0800 (中国标准时间)
*/

package com.ynet.imis.repository.budget;

import com.ynet.imis.domain.budget.CommBudget;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CommBudgetRepository extends JpaRepository<CommBudget, Long>{

}