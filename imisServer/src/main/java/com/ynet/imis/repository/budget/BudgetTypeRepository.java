/**
* BudgetTypeRepository.java
* @author ZHONGMC
* @description 
* @created Wed Oct 10 2018 10:50:32 GMT+0800 (中国标准时间)
* @copyright YNET
* @last-modified Wed Oct 10 2018 11:08:40 GMT+0800 (中国标准时间)
*/

package com.ynet.imis.repository.budget;

import com.ynet.imis.domain.budget.BudgetType;

import org.springframework.data.jpa.repository.JpaRepository;

public interface BudgetTypeRepository extends JpaRepository<BudgetType, Long> {

}