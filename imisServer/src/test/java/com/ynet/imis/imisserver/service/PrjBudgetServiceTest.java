package com.ynet.imis.imisserver.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.ynet.imis.domain.budget.PrjBudget;
import com.ynet.imis.domain.budget.PrjMonthBudget;
import com.ynet.imis.domain.project.Project;
import com.ynet.imis.imisserver.ImisApplicationTests;
import com.ynet.imis.repository.budget.PrjMonthBudgetRepository;
import com.ynet.imis.service.budget.PrjBudgetService;
import com.ynet.imis.service.project.ProjectService;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public class PrjBudgetServiceTest extends ImisApplicationTests {

    @Autowired
    private PrjBudgetService prjBudgetService;

    @Autowired
    private ProjectService prjService;

    @Autowired
    private PrjMonthBudgetRepository mbDao;

    @Test
    public void whenAddPrjBudgetSuccess() throws Exception {

        Page<Project> pgProjects = prjService.getProjectByPage(0, 2, null, null, null, null, null, null, null);

        Project project = pgProjects.getContent().get(0);
        Long depId = project.getDepId();

        Date bgDate = project.getBeginDate();
        Date endDate = project.getEndDate();

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(bgDate);

        Calendar endCal = Calendar.getInstance();
        endCal.setTime(endDate);

        PrjBudget prjBudget = new PrjBudget();
        prjBudget.setDepId(depId);
        prjBudget.setProject(project);
        prjBudget.setAvgManMonthCost(new BigDecimal(12000));
        prjBudget.setPrjNo(project.getPrjNo());
        prjBudget.setPrjName(project.getName());

        int count = 0;
        while (calendar.before(endCal)) {
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            calendar.add(Calendar.MONTH, 1);

            PrjMonthBudget pMB = new PrjMonthBudget();
            pMB.setYear(year);
            pMB.setMonth((short) month);
            pMB.setPrjBudget(prjBudget);
            pMB.setDepId(depId);

            prjBudget.addPrjMonthBudget(pMB);

            count++;
        }

        prjBudgetService.saveProjectBudget(prjBudget);
        outputObject(prjBudget);

        PrjBudget prjB = prjBudgetService.getProjectBudgetByPrjId(project.getId());
        assertNotNull(prjB);

        assertEquals("prj budget' month budget count:" + count, count, prjB.getMonthBudgets().size());

        List<PrjMonthBudget> mbs = mbDao.findAll();
        assertEquals("the prj month budget records should be " + count, count, mbs.size());
    }
}