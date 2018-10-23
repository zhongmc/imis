/**
* BudgetServiceImpl.java
* @author ZHONGMC
* @description 
* @created Wed Oct 10 2018 11:12:21 GMT+0800 (中国标准时间)
* @copyright YNET
* @last-modified Mon Oct 22 2018 10:30:00 GMT+0800 (中国标准时间)
*/

package com.ynet.imis.service.budget;

import java.util.List;

import com.ynet.imis.domain.budget.PrjBudget;
import com.ynet.imis.domain.budget.PrjCommBudget;
import com.ynet.imis.domain.budget.PrjIncomeForecast;
import com.ynet.imis.domain.budget.PrjMonthBudget;
import com.ynet.imis.domain.budget.PrjRightsConfirm;
import com.ynet.imis.repository.budget.PrjBudgetRepository;
import com.ynet.imis.repository.budget.PrjCommBudgetRepository;
import com.ynet.imis.repository.budget.PrjIncomeForecastRepository;
import com.ynet.imis.repository.budget.PrjMonthBudgetRepository;
import com.ynet.imis.repository.budget.PrjRightsConfirmRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class PrjBudgetServiceImpl implements PrjBudgetService {

    @Autowired
    private PrjBudgetRepository prjBudgetDao;

    @Autowired
    private PrjCommBudgetRepository prjCommBudgetDao;
    @Autowired
    private PrjIncomeForecastRepository prjIncomeForecastDao;
    @Autowired
    private PrjRightsConfirmRepository prjRIghtsConfirmDao;

    @Autowired
    private PrjMonthBudgetRepository prjMonthBudgetDao;

    @Override
    public PrjBudget getProjectBudgetByPrjId(Long prjId) {
        return prjBudgetDao.getProjectBudgetByPrjId(prjId);
    }

    @Override
    public int saveProjectBudget(PrjBudget prjBudget) {
        prjBudgetDao.save(prjBudget);

        return 1;
    }

    @Override
    public List<PrjCommBudget> getProjectCommBudgets(Long prjId) {
        return prjCommBudgetDao.getAllPrjCommBudgetsByPrjId(prjId);
    }

    @Override
    public PrjCommBudget savePrjCommBudget(PrjCommBudget prjCommBudget) {
        return prjCommBudgetDao.save(prjCommBudget);
    }

    @Override
    public int deletePrjCommBudget(Long id) {
        prjCommBudgetDao.deleteById(id);
        return 1;
    }

    @Override
    public List<PrjIncomeForecast> getPrjIncomeForecasts(Long prjId) {
        return prjIncomeForecastDao.getAllPrjIncomeForecastsByPrjId(prjId);
    }

    @Override
    public PrjIncomeForecast savePrjIncomeForecast(PrjIncomeForecast prjIncomeForecast) {
        return prjIncomeForecastDao.save(prjIncomeForecast);
    }

    @Override
    public int deletePrjIncomeForecast(Long id) {
        prjIncomeForecastDao.deleteById(id);
        return 1;
    }

    @Override
    public List<PrjRightsConfirm> getPrjRightsConfirms(Long prjId) {
        return prjRIghtsConfirmDao.getAllPrjRightsConfirmsByPrjId(prjId);
    }

    @Override
    public PrjRightsConfirm savePrjRightsConfirm(PrjRightsConfirm prjRightsConfirm) {
        return prjRIghtsConfirmDao.save(prjRightsConfirm);
    }

    @Override
    public int deletePrjRightsConfirm(Long id) {
        prjRIghtsConfirmDao.deleteById(id);
        return 1;
    }

    @Override
    public int savePrjMonthBudgets(PrjBudget prjBudget) {
        prjBudgetDao.updateManmonthCoat(prjBudget.getId(), prjBudget.getAvgManMonthCost());
        prjMonthBudgetDao.saveAll(prjBudget.getMonthBudgets());
        return 1;
    }

}