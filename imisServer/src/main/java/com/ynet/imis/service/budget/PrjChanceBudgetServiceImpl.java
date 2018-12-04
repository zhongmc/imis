/**
* PrjChanceBudgetServiceImpl.java
* @author ZHONGMC
* @description 
* @created Thu Nov 29 2018 14:37:05 GMT+0800 (中国标准时间)
* @copyright YNET
* @last-modified Mon Dec 03 2018 19:25:45 GMT+0800 (中国标准时间)
*/

package com.ynet.imis.service.budget;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.ynet.imis.domain.budget.PrjChanceBudget;
import com.ynet.imis.domain.budget.PrjChanceMonthBudget;
import com.ynet.imis.domain.budget.PrjChanceRightsConfirm;
import com.ynet.imis.repository.budget.PrjChanceBudgetRepository;
import com.ynet.imis.repository.budget.PrjChanceMonthBudgetRepository;
import com.ynet.imis.repository.budget.PrjChanceRightsConfirmRepository;
import com.ynet.imis.repository.project.ProjectChanceRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class PrjChanceBudgetServiceImpl implements PrjChanceBudgetService {

    @Autowired
    private PrjChanceBudgetRepository prjChanceBudgetDao;

    @Autowired
    private ProjectChanceRepository prjChanceDao;

    @Autowired
    private PrjChanceRightsConfirmRepository prjChanceRightsConfirmDao;

    @Autowired
    private PrjChanceMonthBudgetRepository prjChanceMonthBudgetDao;

    @Override
    public PrjChanceBudget getProjectChanceBudgetByPrjId(Long prjId) {
        return prjChanceBudgetDao.getProjectChanceBudgetByPrjId(prjId);
    }

    @Override
    public int saveProjectChanceBudget(PrjChanceBudget prjChanceBudget) {

        prjChanceDao.updatePrjChanceChance(prjChanceBudget.getPrjChanceId(), prjChanceBudget.getChance());
        prjChanceBudgetDao.save(prjChanceBudget);

        return 1;
    }

    @Override
    public List<PrjChanceRightsConfirm> getPrjChanceRightsConfirms(Long prjId) {
        return prjChanceRightsConfirmDao.getAllPrjChanceRightsConfirmsByPrjChanceId(prjId);
    }

    @Override
    public PrjChanceRightsConfirm savePrjChanceRightsConfirm(PrjChanceRightsConfirm prjChanceRightsConfirm) {

        // update prj cost confirm year
        int begYear, endYear, begMonth, endMonth;
        Calendar cal = Calendar.getInstance();

        int year = cal.get(Calendar.YEAR);
        // prjRightsConfirm.getExpectDate();

        cal.setTime(prjChanceRightsConfirm.getBegDate());
        begYear = cal.get(Calendar.YEAR);
        begMonth = cal.get(Calendar.MONTH);

        cal.setTime(prjChanceRightsConfirm.getEndDate());
        endYear = cal.get(Calendar.YEAR);
        endMonth = cal.get(Calendar.MONTH);
        int beg = begYear * 12 + begMonth;
        int end = endYear * 12 + endMonth;

        prjChanceMonthBudgetDao.updatePrjChanceCostConfirmYear(prjChanceRightsConfirm.getPrjChanceId(), year, beg, end);
        PrjChanceRightsConfirm prjChanceConfirm = prjChanceRightsConfirmDao.save(prjChanceRightsConfirm);

        return prjChanceConfirm;
    }

    @Override
    public int deletePrjChanceRightsConfirm(Long id) {

        PrjChanceRightsConfirm prjChanceConfirm = prjChanceRightsConfirmDao.findById(id).get();
        if (prjChanceConfirm == null)
            return 0;
        int begYear, endYear, begMonth, endMonth;
        Calendar cal = Calendar.getInstance();

        cal.setTime(prjChanceConfirm.getBegDate());
        begYear = cal.get(Calendar.YEAR);
        begMonth = cal.get(Calendar.MONTH);

        cal.setTime(prjChanceConfirm.getEndDate());
        endYear = cal.get(Calendar.YEAR);
        endMonth = cal.get(Calendar.MONTH);
        int beg = begYear * 12 + begMonth;
        int end = endYear * 12 + endMonth;

        prjChanceMonthBudgetDao.updatePrjChanceCostConfirmYear(prjChanceConfirm.getPrjChanceId(), 0, beg, end);

        prjChanceRightsConfirmDao.deleteById(id);
        return 1;
    }

    @Override
    public int savePrjChanceMonthBudgets(PrjChanceBudget prjChanceBudget) {
        prjChanceBudgetDao.updateManmonthCoat(prjChanceBudget.getId(), prjChanceBudget.getAvgManMonthCost());
        prjChanceMonthBudgetDao.saveAll(prjChanceBudget.getMonthBudgets());
        return 1;
    }

    // 根据部门id 取所有的项目机会预算
    public List<PrjChanceBudget> getPrjChanceBudgetByDepIds(List<Long> depIds) {
        return prjChanceBudgetDao.getNormalBudgetsByDepIds(depIds);
    }

    // 根据部门id 取所有的项目机会预算，某年的
    @Override
    public List<PrjChanceMonthBudget> getPrjChanceMonthBudgetsByDepId(List<Long> depIds, int year) {
        return prjChanceMonthBudgetDao.getPrjChanceMonthBudgetsByDepId(depIds, year);
    }

    @Override // 根据部门id 取所有的当年确权项目的预算
    public List<PrjChanceMonthBudget> getConfirmedPrjChanceMonthBudgetsByDepId(List<Long> depIds, int year) {
        return prjChanceMonthBudgetDao.getConfirmedPrjChanceMonthBudgetsByDepId(depIds, year);
    }

    // 根据部门id 取所有的项目确权
    @Override
    public List<PrjChanceRightsConfirm> getPrjChanceRightsConfirmByDepId(List<Long> depIds, int year) {
        return prjChanceRightsConfirmDao.getPrjChanceRightsConfirmByDepId(depIds, year);

    }

    // 根据prjId 取所有的预算
    @Override
    public List<PrjChanceMonthBudget> getPrjChanceMonthBudgetsByPrjId(List<Long> prjIds, int year) {
        return prjChanceMonthBudgetDao.getPrjChanceMonthBudgetsByPrjId(prjIds, year);
    }

    @Override
    public PrjChanceRightsConfirm confirmPrjChanceRight(Long rightId, Date date, BigDecimal amount) {

        PrjChanceRightsConfirm confirm = this.prjChanceRightsConfirmDao.getOne(rightId);

        if (confirm == null)
            return null;

        confirm.setRealDate(date);
        confirm.setRealAmount(amount);
        prjChanceRightsConfirmDao.save(confirm);
        return confirm;
    }

    @Override
    public void deleteAll() {
        prjChanceRightsConfirmDao.deleteAllInBatch();
        prjChanceMonthBudgetDao.deleteAllInBatch();
        prjChanceBudgetDao.deleteAllInBatch();
    }

}