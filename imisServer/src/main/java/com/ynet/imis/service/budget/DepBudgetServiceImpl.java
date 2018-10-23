package com.ynet.imis.service.budget;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import com.ynet.imis.domain.budget.CostBudgetInfo;
import com.ynet.imis.domain.budget.DepCommBudget;
import com.ynet.imis.domain.budget.PrjBudget;

import com.ynet.imis.repository.budget.CostBudgetInfoRepository;
import com.ynet.imis.repository.budget.DepCommBudgetRepository;
import com.ynet.imis.repository.budget.PrjBudgetRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class DepBudgetServiceImpl implements DepBudgetService {

    @Autowired
    CostBudgetInfoRepository costBudgetInfoDao;

    @Autowired
    DepCommBudgetRepository depCommBudgetDao;

    @Autowired
    PrjBudgetRepository prjBudgetDao;

    @Override
    public CostBudgetInfo addCostBudgetInfo(CostBudgetInfo costBudgetInfo) {
        return costBudgetInfoDao.save(costBudgetInfo);
    }

    @Override
    public int addAllCostBudgetInfos(List<CostBudgetInfo> costBudgetInfos) {
        costBudgetInfoDao.saveAll(costBudgetInfos);
        return 1;
    }

    @Override
    public List<CostBudgetInfo> getCostBudgetInfosByDepId(Long depId, Long typeId) {
        int year = Calendar.getInstance().get(Calendar.YEAR);
        List<CostBudgetInfo> infos = costBudgetInfoDao.getDepartmentCostBudgetInfo(depId, typeId, year);
        return infos;
    }

    @Override
    public List<DepCommBudget> getDepCommBudgets(Long depId) {
        return depCommBudgetDao.getAllDepCommBudgetsByDepId(depId);
    }

    @Override
    public DepCommBudget saveDepCommBudget(DepCommBudget depCommBudget) {
        return depCommBudgetDao.save(depCommBudget);
    }

    @Override
    public int deleteDepCommBudget(Long id) {
        depCommBudgetDao.deleteById(id);
        return 1;
    }

    @Override
    public List<Object> getDepPrjBudgets(Long depId) {

        List<Object> cprjBudgets = new ArrayList<Object>();
        List<PrjBudget> prjBudgets = prjBudgetDao.getDepPrjBudgets(depId);

        if (prjBudgets.size() == 0)
            return cprjBudgets;

        int year = Calendar.getInstance().get(Calendar.YEAR);

        for (PrjBudget prjBudget : prjBudgets) {

            Map<String, Object> aPrjBudget = prjBudget.getMapedObject(year);
            cprjBudgets.add(aPrjBudget);
        }

        return cprjBudgets;
    }

}