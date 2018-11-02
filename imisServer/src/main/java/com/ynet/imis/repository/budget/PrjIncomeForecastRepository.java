/**
* PrjIncomeForecastRepository.java
* @author ZHONGMC
* @description 
* @created Thu Oct 18 2018 15:01:49 GMT+0800 (中国标准时间)
* @copyright YNET
* @last-modified Thu Nov 01 2018 11:10:22 GMT+0800 (中国标准时间)
*/

package com.ynet.imis.repository.budget;

import java.util.List;

import com.ynet.imis.domain.budget.PrjIncomeForecast;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface PrjIncomeForecastRepository extends JpaRepository<PrjIncomeForecast, Long> {

    @Query("select a from PrjIncomeForecast a where a.prjId=?1")
    public List<PrjIncomeForecast> getAllPrjIncomeForecastsByPrjId(Long prjId);

    @Query("select a from PrjIncomeForecast a where a.depId in ?1 and year(a.expectDate) = ?2")
    public List<PrjIncomeForecast> getPrjIncomesByDepId(List<Long> depIds, int year);
}