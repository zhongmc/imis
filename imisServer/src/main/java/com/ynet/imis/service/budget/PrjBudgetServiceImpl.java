/**
* BudgetServiceImpl.java
* @author ZHONGMC
* @description 
* @created Wed Oct 10 2018 11:12:21 GMT+0800 (中国标准时间)
* @copyright YNET
* @last-modified Thu Nov 08 2018 14:48:51 GMT+0800 (中国标准时间)
*/

package com.ynet.imis.service.budget;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
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

        // update prj cost confirm year
        int begYear, endYear, begMonth, endMonth;
        Calendar cal = Calendar.getInstance();

        int year = cal.get(Calendar.YEAR);
        // prjRightsConfirm.getExpectDate();

        cal.setTime(prjRightsConfirm.getBegDate());
        begYear = cal.get(Calendar.YEAR);
        begMonth = cal.get(Calendar.MONTH);

        cal.setTime(prjRightsConfirm.getEndDate());
        endYear = cal.get(Calendar.YEAR);
        endMonth = cal.get(Calendar.MONTH);
        int beg = begYear * 12 + begMonth;
        int end = endYear * 12 + endMonth;

        prjMonthBudgetDao.updatePrjCostConfirmYear(prjRightsConfirm.getPrjId(), year, beg, end);
        PrjRightsConfirm prjConfirm = prjRIghtsConfirmDao.save(prjRightsConfirm);

        return prjConfirm;
    }

    @Override
    public int deletePrjRightsConfirm(Long id) {

        PrjRightsConfirm prjConfirm = prjRIghtsConfirmDao.findById(id).get();
        if (prjConfirm == null)
            return 0;
        int begYear, endYear, begMonth, endMonth;
        Calendar cal = Calendar.getInstance();

        cal.setTime(prjConfirm.getBegDate());
        begYear = cal.get(Calendar.YEAR);
        begMonth = cal.get(Calendar.MONTH);

        cal.setTime(prjConfirm.getEndDate());
        endYear = cal.get(Calendar.YEAR);
        endMonth = cal.get(Calendar.MONTH);
        int beg = begYear * 12 + begMonth;
        int end = endYear * 12 + endMonth;

        prjMonthBudgetDao.updatePrjCostConfirmYear(prjConfirm.getPrjId(), 0, beg, end);

        prjRIghtsConfirmDao.deleteById(id);
        return 1;
    }

    @Override
    public int savePrjMonthBudgets(PrjBudget prjBudget) {
        prjBudgetDao.updateManmonthCoat(prjBudget.getId(), prjBudget.getAvgManMonthCost());
        prjMonthBudgetDao.saveAll(prjBudget.getMonthBudgets());
        return 1;
    }

    // 根据部门id 取所有的项目预算，某年的
    @Override
    public List<PrjMonthBudget> getPrjMonthBudgetsByDepId(List<Long> depIds, int year) {
        return prjMonthBudgetDao.getPrjMonthBudgetsByDepId(depIds, year);
    }

    @Override // 根据部门id 取所有的当年确权项目的预算
    public List<PrjMonthBudget> getConfirmedPrjMonthBudgetsByDepId(List<Long> depIds, int year) {
        return prjMonthBudgetDao.getConfirmedPrjMonthBudgetsByDepId(depIds, year);
    }

    // 根据部门id 取所有的项目确权
    @Override
    public List<PrjRightsConfirm> getPrjRightsConfirmByDepId(List<Long> depIds, int year) {
        return prjRIghtsConfirmDao.getPrjRightsConfirmByDepId(depIds, year);

    }

    // 根据部门id 取所有的项目现金收入
    @Override
    public List<PrjIncomeForecast> getPrjIncomesByDepId(List<Long> depIds, int year) {
        return prjIncomeForecastDao.getPrjIncomesByDepId(depIds, year);

    }

    // 根据prjId 取所有的预算
    @Override
    public List<PrjMonthBudget> getPrjMonthBudgetsByPrjId(List<Long> prjIds, int year) {
        return prjMonthBudgetDao.getPrjMonthBudgetsByPrjId(prjIds, year);
    }

    @Override
    public PrjIncomeForecast confirmPrjIncome(Long incomeId, Date date, BigDecimal amount) {
        PrjIncomeForecast income = this.prjIncomeForecastDao.getOne(incomeId);
        if (income == null)
            return null;
        income.setRealDate(date);
        income.setRealAmount(amount);
        prjIncomeForecastDao.save(income);
        return income;
    }

    @Override
    public PrjRightsConfirm confirmPrjRight(Long rightId, Date date, BigDecimal amount) {

        PrjRightsConfirm confirm = this.prjRIghtsConfirmDao.getOne(rightId);

        if (confirm == null)
            return null;

        confirm.setRealDate(date);
        confirm.setRealAmount(amount);
        prjRIghtsConfirmDao.save(confirm);
        return confirm;
    }

    @Override
    public void deleteAll() {
        prjIncomeForecastDao.deleteAllInBatch();
        prjRIghtsConfirmDao.deleteAllInBatch();
        prjMonthBudgetDao.deleteAllInBatch();
        prjBudgetDao.deleteAllInBatch();
    }

}