/**
* CostItemRepository.java
* @author ZHONGMC
* @description 
* @created Wed Oct 10 2018 10:53:23 GMT+0800 (中国标准时间)
* @copyright YNET
* @last-modified Wed Oct 10 2018 11:09:36 GMT+0800 (中国标准时间)
*/

package com.ynet.imis.repository.budget;

import com.ynet.imis.domain.budget.CostItem;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CostItemRepository extends JpaRepository<CostItem, Long>{

}