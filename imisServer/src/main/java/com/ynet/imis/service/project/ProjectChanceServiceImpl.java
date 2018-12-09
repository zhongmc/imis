package com.ynet.imis.service.project;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;

import com.ynet.imis.domain.budget.PrjBudget;
import com.ynet.imis.domain.budget.PrjChanceBudget;
import com.ynet.imis.domain.budget.PrjChanceCommBudget;
import com.ynet.imis.domain.budget.PrjChanceRightsConfirm;
import com.ynet.imis.domain.budget.PrjCommBudget;
import com.ynet.imis.domain.budget.PrjRightsConfirm;
import com.ynet.imis.domain.project.Project;
import com.ynet.imis.domain.project.ProjectChance;
import com.ynet.imis.domain.project.ProjectChance.ChanceState;
import com.ynet.imis.repository.budget.PrjBudgetRepository;
import com.ynet.imis.repository.budget.PrjChanceBudgetRepository;
import com.ynet.imis.repository.budget.PrjChanceCommBudgetRepository;
import com.ynet.imis.repository.budget.PrjChanceMonthBudgetRepository;
import com.ynet.imis.repository.budget.PrjChanceRightsConfirmRepository;
import com.ynet.imis.repository.budget.PrjCommBudgetRepository;
import com.ynet.imis.repository.budget.PrjMonthBudgetRepository;
import com.ynet.imis.repository.budget.PrjRightsConfirmRepository;
import com.ynet.imis.repository.project.ProjectChanceRepository;
import com.ynet.imis.repository.project.ProjectRepository;
import com.ynet.imis.utils.ImisUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class ProjectChanceServiceImpl implements ProjectChanceService {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private ProjectChanceRepository projectChanceDao;

    @Autowired
    private PrjChanceBudgetRepository prjChanceBudgetDao;
    @Autowired
    private PrjChanceMonthBudgetRepository prjChanceMonthBudgetDao;

    @Autowired
    private PrjChanceRightsConfirmRepository prjChanceConformDao;

    @Autowired
    private ProjectRepository projectDao;
    @Autowired
    private PrjBudgetRepository prjBudgetDao;

    @Autowired
    private PrjRightsConfirmRepository prjRightsConfirmDao;

    @Autowired
    private PrjCommBudgetRepository prjCommBudgetDao;

    @Autowired
    private PrjChanceCommBudgetRepository prjChanceCommBudgetDao;

    // private PrjMonthBudgetRepository prjMonthBudgetDao;

    @Override
    public ProjectChance addProjectChance(ProjectChance projectChance) {

        prjChanceBudgetDao.updatePrjChanceChance(projectChance.getId(), projectChance.getContractAmount(),
                projectChance.getChance());
        return projectChanceDao.save(projectChance);
    }

    @Override
    public int updateProjectChance(ProjectChance projectChance) {
        prjChanceBudgetDao.updatePrjChanceChance(projectChance.getId(), projectChance.getContractAmount(),
                projectChance.getChance());
        projectChanceDao.save(projectChance);
        return 1;
    }

    @Override
    public ProjectChance getProjectChanceById(Long id) {
        return projectChanceDao.findById(id).get();
    }

    @Override
    public int deleteProjectChanceById(Long id) {

        logger.info("delete project chance:" + id);

        projectChanceDao.deleteById(id);
        prjChanceBudgetDao.deleteAllByPrjId(id);
        prjChanceMonthBudgetDao.deleteAllByPrjId(id);
        prjChanceCommBudgetDao.deleteByPrjChanceId(id);

        // PrjChanceBudget prjChanceBudget =
        // prjChanceBudgetDao.getProjectChanceBudgetByPrjId(id);
        // if (prjChanceBudget != null) {
        // prjChanceBudgetDao.delete(prjChanceBudget);
        // }
        return 1;
    }

    @Override
    public Page<ProjectChance> getProjectChanceByPage(int page, int size, String keywords, Long customId,
            Long departmentId, Long prjType, Long prjClass, String beginDateScope, List<Long> depIds) {

        Specification<ProjectChance> spec = new Specification<ProjectChance>() {
            private static final long serialVersionUID = 3933287087564315019L;

            @Override
            public Predicate toPredicate(Root<ProjectChance> root, CriteriaQuery<?> query, CriteriaBuilder cb) {

                Predicate predicate = null;
                if (keywords != null && keywords.length() > 0) {
                    predicate = cb.like(root.<String>get("name"), "%" + keywords + "%");
                }

                if (customId != null && customId.longValue() != -1) {
                    Predicate pred = cb.equal(root.<Long>get("customId"), customId);
                    if (predicate == null)
                        predicate = pred;
                    else
                        predicate = cb.and(predicate, pred);
                }

                if (customId != null && customId.longValue() != -1) {
                    Predicate pred = cb.equal(root.<Long>get("customId"), customId);
                    if (predicate == null)
                        predicate = pred;
                    else
                        predicate = cb.and(predicate, pred);
                }
                if (departmentId != null && departmentId.longValue() != -1) {

                    Predicate pred = cb.equal(root.<Long>get("depId"), departmentId);
                    if (predicate == null)
                        predicate = pred;
                    else
                        predicate = cb.and(predicate, pred);
                } else if (depIds != null) {
                    Path<Long> path = root.get("depId");
                    CriteriaBuilder.In<Object> in = cb.in(path);
                    for (Long id : depIds)
                        in.value(id);

                    Predicate pred = cb.and(in);

                    if (predicate == null)
                        predicate = pred;
                    else
                        predicate = cb.and(predicate, pred);
                }

                return predicate;
            }
        };

        Sort sort = Sort.by("id");
        PageRequest pageReq = PageRequest.of(page, size, sort);
        Page<ProjectChance> pageOfPrj = projectChanceDao.findAll(spec, pageReq);

        return pageOfPrj;
    }

    @Override
    public void deleteAll() {
        projectChanceDao.deleteAllInBatch();
    }

    // 关闭机会
    public int closeProjectChanceById(Long id) {

        logger.info("do close the prj chance ...");
        projectChanceDao.updateChanceState(id, ChanceState.CLOSED);
        prjChanceBudgetDao.deleteAllByPrjId(id);
        prjChanceMonthBudgetDao.deleteAllByPrjId(id);

        return 1;
    }

    // 转为项目
    public int transferProjectChanceToProjectById(Long id) {

        Project prj = new Project();
        PrjBudget prjBudget = new PrjBudget();

        ProjectChance prjChance = projectChanceDao.getOne(id);
        PrjChanceBudget prjChanceBudget = prjChanceBudgetDao.getProjectChanceBudgetByPrjId(id);
        List<PrjChanceRightsConfirm> prjChanceConfirms = prjChanceConformDao
                .getAllPrjChanceRightsConfirmsByPrjChanceId(id);

        List<PrjChanceCommBudget> prjChanceCommBudgets = prjChanceCommBudgetDao.getAllPrjChanceCommBudgetsByPrjId(id);

        logger.info("transfer prj chance: " + prjChance.getName() + " to project......");

        prj.copyFrom(prjChance);

        prj = projectDao.save(prj);

        prjBudget.setProject(prj);
        prjBudget.copyFrom(prjChanceBudget);

        prjBudget = prjBudgetDao.save(prjBudget);

        logger.info("Saved prj : " + ImisUtils.objectJsonStr(prj));

        logger.info("Saved prjBudget: " + ImisUtils.objectJsonStr(prjBudget));

        if (prjChanceConfirms != null && prjChanceConfirms.size() > 0) {
            List<PrjRightsConfirm> prjConfirms = new ArrayList<PrjRightsConfirm>();
            for (PrjChanceRightsConfirm pcrc : prjChanceConfirms) {
                PrjRightsConfirm prjRightsConfirm = new PrjRightsConfirm();
                prjRightsConfirm.copyFrom(pcrc);
                prjRightsConfirm.setPrjId(prj.getId()); // setProject(prj);
                prjConfirms.add(prjRightsConfirm);
            }
            prjRightsConfirmDao.saveAll(prjConfirms);
        }
        if (prjChanceCommBudgets != null && prjChanceCommBudgets.size() > 0) {
            List<PrjCommBudget> prjCommBudgets = new ArrayList<PrjCommBudget>();

            for (PrjChanceCommBudget prjChanceCommBudget : prjChanceCommBudgets) {
                PrjCommBudget prjCommBudget = new PrjCommBudget();
                prjCommBudget.copyFrom(prjChanceCommBudget);
                prjCommBudget.setPrjId(prj.getId());
                prjCommBudgets.add(prjCommBudget);

            }
            prjCommBudgetDao.saveAll(prjCommBudgets);
        }
        projectChanceDao.updateChanceState(id, ChanceState.TO_PRJ);
        prjChanceBudgetDao.deleteAllByPrjId(id);
        prjChanceMonthBudgetDao.deleteAllByPrjId(id);
        return 1;
    }

}