/**
* PrjChanceRightsConfirmRepository.java
* @author ZHONGMC
* @description 
* @created Thu Nov 29 2018 14:00:58 GMT+0800 (中国标准时间)
* @copyright YNET
* @last-modified Mon Dec 03 2018 20:03:41 GMT+0800 (中国标准时间)
*/

package com.ynet.imis.repository.budget;

import java.util.List;

import com.ynet.imis.domain.budget.PrjChanceRightsConfirm;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface PrjChanceRightsConfirmRepository extends JpaRepository<PrjChanceRightsConfirm, Long> {

    @Query("select a from PrjChanceRightsConfirm a where a.prjChanceId=?1")
    public List<PrjChanceRightsConfirm> getAllPrjChanceRightsConfirmsByPrjChanceId(Long prjChanceId);

    @Query("select a from PrjChanceRightsConfirm a where a.depId in ?1 and year(a.expectDate) = ?2")
    public List<PrjChanceRightsConfirm> getPrjChanceRightsConfirmByDepId(List<Long> depIds, int year);
}