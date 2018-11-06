package com.ynet.imis.service.project;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;

import com.ynet.imis.domain.budget.PrjBudget;
import com.ynet.imis.domain.budget.PrjIncomeForecast;
import com.ynet.imis.domain.budget.PrjMonthBudget;
import com.ynet.imis.domain.budget.PrjRightsConfirm;
import com.ynet.imis.domain.org.Custom;
import com.ynet.imis.domain.project.Project;
import com.ynet.imis.domain.project.Project.PrjClass;
import com.ynet.imis.domain.project.Project.PrjType;
import com.ynet.imis.repository.budget.PrjBudgetRepository;
import com.ynet.imis.repository.budget.PrjIncomeForecastRepository;
import com.ynet.imis.repository.budget.PrjRightsConfirmRepository;
import com.ynet.imis.repository.org.CustomRepository;
import com.ynet.imis.repository.project.ProjectRepository;
import com.ynet.imis.utils.ImisUtils;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@Transactional
public class ProjectServiceImpl implements ProjectService {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private ProjectRepository projectDao;

    @Autowired
    private CustomRepository customDao;

    @Autowired
    private PrjBudgetRepository prjBudgetDao;

    @Autowired
    private PrjIncomeForecastRepository prjIncomeDao;
    @Autowired
    private PrjRightsConfirmRepository prjConfirmDao;

    // private PrjI prjIncomeDao
    @Override
    public Project addProject(Project project) {

        return projectDao.save(project);
    }

    @Override
    public Project getProjectById(Long id) {
        return projectDao.findById(id).get();
    }

    @Override
    public int deleteProjectById(Long id) {
        projectDao.deleteById(id);
        return 1;
    }

    @Override
    public int updateProject(Project project) {
        projectDao.save(project);
        return 1;
    }

    @Override
    public Page<Project> getProjectByPage(int page, int size, String keywords, Long customId, Long departmentId,
            Long prjType, Long prjClass, String beginDateScope, List<Long> depIds) {

        Specification<Project> spec = new Specification<Project>() {
            private static final long serialVersionUID = 3933287087564315019L;

            @Override
            public Predicate toPredicate(Root<Project> root, CriteriaQuery<?> query, CriteriaBuilder cb) {

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
        Page<Project> pageOfPrj = projectDao.findAll(spec, pageReq);

        return pageOfPrj;
    }

}