/**
* PrjIncomeForecastRepository.java
* @author ZHONGMC
* @description 
* @created Thu Oct 18 2018 15:01:49 GMT+0800 (中国标准时间)
* @copyright YNET
* @last-modified Thu Oct 25 2018 17:06:26 GMT+0800 (中国标准时间)
*/

package com.ynet.imis.repository.budget;

import java.util.List;

import com.ynet.imis.domain.budget.PrjIncomeForecast;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface PrjIncomeForecastRepository extends JpaRepository<PrjIncomeForecast, Long> {

    @Query("select a from PrjIncomeForecast a where a.prjId=?1")
    public List<PrjIncomeForecast> getAllPrjIncomeForecastsByPrjId(Long prjId);

    @Query("select a from PrjIncomeForecast a where a.depId in ?1 and year(a.bgDate) = ?2")
    public List<PrjIncomeForecast> getPrjIncomesByDepId(List<Long> depIds, int year);
}