/**
* PrjRightsConfirmRepository.java
* @author ZHONGMC
* @description 
* @created Thu Oct 18 2018 15:02:23 GMT+0800 (中国标准时间)
* @copyright YNET
* @last-modified Thu Nov 01 2018 11:11:02 GMT+0800 (中国标准时间)
*/

package com.ynet.imis.repository.budget;

import java.util.List;

import com.ynet.imis.domain.budget.PrjRightsConfirm;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface PrjRightsConfirmRepository extends JpaRepository<PrjRightsConfirm, Long> {

    @Query("select a from PrjRightsConfirm a where a.prjId=?1")
    public List<PrjRightsConfirm> getAllPrjRightsConfirmsByPrjId(Long prjId);

    @Query("select a from PrjRightsConfirm a where a.depId in ?1 and year(a.expectDate) = ?2")
    public List<PrjRightsConfirm> getPrjRightsConfirmByDepId(List<Long> depIds, int year);
}