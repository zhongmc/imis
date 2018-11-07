package com.ynet.imis.service.budget;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import com.ynet.imis.domain.budget.CostBudgetInfo;
import com.ynet.imis.domain.budget.DepCommBudget;
import com.ynet.imis.domain.budget.PrjBudget;
import com.ynet.imis.repository.budget.CostBudgetInfoRepository;
import com.ynet.imis.repository.budget.DepCommBudgetRepository;
import com.ynet.imis.repository.budget.PrjBudgetRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
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
    public List<CostBudgetInfo> getCostBudgetInfosByDepId(Long depId, Long typeId, int year) {
        List<CostBudgetInfo> infos = costBudgetInfoDao.getDepartmentCostBudgetInfo(depId, typeId, year);
        return infos;
    }

    @Override
    public List<CostBudgetInfo> getCostCollectionByDepId(Long depId, int year) {
        List<CostBudgetInfo> infos = costBudgetInfoDao.getDepartmentCostCollection(depId, year);
        return infos;

    }

    @Override
    public List<CostBudgetInfo> getCostCollectionByItemDepId(List<Long> depId, Long itemId, int year) {
        List<CostBudgetInfo> infos = costBudgetInfoDao.getDepartmentCostCollectionByItem(depId, itemId, year);
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

    @Override
    public Page<Map<String, Object>> getDepPrjBudgetByPage(int page, int size, Long depId, int year) {

        Specification<PrjBudget> spec = new Specification<PrjBudget>() {
            private static final long serialVersionUID = 3933287087564315019L;

            @Override
            public Predicate toPredicate(Root<PrjBudget> root, CriteriaQuery<?> query, CriteriaBuilder cb) {

                Predicate predicate = null;

                if (depId != null && depId.longValue() != -1) {
                    predicate = cb.equal(root.<Long>get("depId"), depId);
                    // if (predicate == null)
                    // predicate = pred;
                    // else
                    // predicate = cb.and(predicate, pred);
                }

                return predicate;
            }

        };

        // PageRequest pageReq = PageRequest.of(page, size, sort);
        Sort sort = Sort.by("id");
        PageRequest pageReq = PageRequest.of(page, size, sort);
        Page<PrjBudget> prjBudgets = prjBudgetDao.findAll(spec, pageReq);

        return prjBudgets.map(new Function<PrjBudget, Map<String, Object>>() {

            @Override
            public Map<String, Object> apply(PrjBudget prjBudget) {
                return prjBudget.getMapedObject(year);

            }
        });

    }

    @Override
    public void deleteAll() {
        costBudgetInfoDao.deleteAllInBatch();
    }

}