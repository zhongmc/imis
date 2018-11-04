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

    public void importFile(MultipartFile file, Long depId ) throws Exception
    {
        logger.info("import project from multipart file file: " + file.getName());
        importPrjBudgetInfo( file.getInputStream(), depId );

    }



    // 从文件中导入 //depId 所属部门
    public void importFile(File file, Long depId) throws Exception {
        logger.info("import project file: " + file.getName());
        FileInputStream fi = new FileInputStream( file );
        importPrjBudgetInfo( fi, depId);
        fi.close();
    }


    private void importPrjBudgetInfo(InputStream in, Long depId )throws Exception
    {

        Workbook workbook = WorkbookFactory.create(in);

        SimpleDateFormat fmt = new SimpleDateFormat("yyyy/MM");
        // Date begDate = null, endDate = null;

        Project prevProject = null;
        // PrjRightsConfirm confirm = null;
        PrjBudget prevPrjBudget = null;
        List<PrjIncomeForecast> incomes = new ArrayList<PrjIncomeForecast>();
        List<PrjRightsConfirm> confirms = new ArrayList<PrjRightsConfirm>();
        boolean isSamePrj = false;
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int newPrjCount = 0;
        Sheet sheet = workbook.getSheet("延续性项目");

        if (sheet != null) {
            logger.info("read the sheet of " + sheet.getSheetName());
            int rows = sheet.getPhysicalNumberOfRows();
            for (int i = 1; i < rows; i++) {

                Row row = sheet.getRow(i);
                if (row == null)
                    continue;

                isSamePrj = false;

                Cell cell = row.getCell(4); // 4:合同名称
                if (cell == null)
                    continue;
                String prjName = cell.getStringCellValue();
                if (prjName == null || prjName.length() == 0)
                    continue;

                PrjBudget prjBudget = new PrjBudget();
                prjBudget.setDepId(depId);
                Project project = new Project();
                project.setDepId(depId);

                project.setName(prjName);
                project.setPrjClass(PrjClass.LAST_PRJ);

                cell = row.getCell(2); // 2:客户名称
                if (cell != null) {
                    String customName = cell.getStringCellValue();
                    Custom custom = getOrAddCustomByName(customName);
                    project.setCustom(custom);
                }

                cell = row.getCell(3); // 3:合同编号
                if (cell != null) {
                    String constractNo = cell.getStringCellValue();
                    project.setContractNo(constractNo);
                    if (prevProject != null && constractNo.equals(prevProject.getContractNo())) {
                        // same project
                        isSamePrj = true;
                    }
                }

                cell = row.getCell(5); // 5 核算类型
                if (cell != null) {
                    String prjType = cell.getStringCellValue();
                    if ("实施类".equals(prjType))
                        project.setPrjType(PrjType.PROJECT);
                    else if ("派单类".equals(prjType))
                        project.setPrjType(PrjType.TASKS);
                    else if ("人月类".equals(prjType))
                        project.setPrjType(PrjType.MAN_MONTH);
                    else if ("项目类".equals(prjType))
                        project.setPrjType(PrjType.PROJECT);
                    else
                        logger.info("Unknow project type of " + prjType + " line " + (i + 1));
                }

                cell = row.getCell(6); // 6 预计签订日期
                if (cell != null) {
                    Date contractDate = cell.getDateCellValue();
                    project.setContractDate(contractDate);
                }

                cell = row.getCell(7); // 7 预计合同金额
                if (cell != null) {
                    double contractAmount = cell.getNumericCellValue();
                    BigDecimal ctrAmount = BigDecimal.valueOf(contractAmount);
                    project.setContractAmount(ctrAmount);

                    if (isSamePrj) // the same project
                    {
                        ctrAmount = ctrAmount.add(prevProject.getContractAmount());
                        prevProject.setContractAmount(ctrAmount);
                    }
                }

                // 对应期间
                cell = row.getCell(9); // 9 对应期间 2017/4-2017/12 2018年1月-2018年3月 2017/4-2017/12 2018年1月-2018年3月
                                       // 2017年12月至2018年12月 2017/2~2018/4 2018.1-2018.12  2018年1-6月 2018年1月～3月 已结项  2018(南一)




                String confirmName = "";
                Date begDate = null, endDate = null;
                if (cell != null && cell.getCellType() == CellType.STRING ) {
                    String strVal = cell.getStringCellValue();
                    if (strVal == null || strVal.length() < 5) {
                        logger.info("Unknow info of 对应期间 " + strVal + " in excell file s line " + (i + 1));

                    } 
                    else 
                    
                    {
                        
                            String rStrVal = strVal.replace('年', '/');
                            rStrVal = rStrVal.replace('月', '/');
                            rStrVal = rStrVal.replace('至', '-');
                            rStrVal = rStrVal.replace('.', '/');
                            rStrVal = rStrVal.replace('~', '-');
                            int idx = rStrVal.indexOf('-');
                            confirmName = rStrVal;
                            String begDateStr, endDateStr;
                            if( idx != -1)
                            {
                                begDateStr = rStrVal.substring(0, idx);
                                endDateStr = rStrVal.substring(idx + 1);
                                try {
                                    begDate = fmt.parse(begDateStr);
                                    if( endDateStr.length() < 4)
                                    {
                                        //只有月
                                        idx = endDateStr.indexOf('/');
                                        if( idx != -1)
                                            endDateStr = endDateStr.substring(0, idx);
                                        int month = Integer.parseInt( endDateStr );

                                        Calendar cal = Calendar.getInstance();
                                        cal.setTime( begDate);
                                        cal.set(Calendar.MONTH, month-1);
                                        endDate = cal.getTime();
                                    }
                                    else
                                        endDate = fmt.parse(endDateStr);
                                } catch (ParseException e) {
                                    logger.info("Failed to parse the date: " + strVal);
                                }
                            }
                        // SimpleDateFormat ofmt = new SimpleDateFormat("yyyy/MM/dd");
                        // logger.info("对应期间：" + strVal + ", cfName:" + confirmName + ", begStr: " + begDateStr + ", endStr:"+ endDateStr + ",bd:" + ofmt.format(begDate) + " ed:"+ ofmt.format(endDate));

                        project.setBeginDate(begDate);
                        project.setEndDate(endDate);

                        if (isSamePrj) {
                            if( prevProject.getBeginDate() == null )
                                prevProject.setBeginDate(begDate);
                            if( prevProject.getEndDate() == null || (endDate != null && prevProject.getEndDate().getTime() < endDate.getTime()) )    
                                prevProject.setEndDate(endDate); /////
                        }
                    }

                }
                // 10 确权（ 本年确认含税收入）
                cell = row.getCell(10);
                if (cell != null) {
                    BigDecimal confirmAmount = BigDecimal.valueOf(cell.getNumericCellValue());
                    PrjRightsConfirm confirm = new PrjRightsConfirm();
                    confirm.setAmount(confirmAmount);
                    if( begDate != null )
                    {
                        confirm.setBegDate(begDate);
                        confirm.setEndDate(endDate);
                        confirm.setName(confirmName);
                    }
                    confirm.setDepId(depId);
                    Calendar cal = Calendar.getInstance();
                    cal.set(Calendar.MONTH, 11);
                    confirm.setExpectDate(cal.getTime()); // 今年
                    confirms.add(confirm);
                }

                // 25 人均成本
                cell = row.getCell(25);
                if (cell != null) {
                    BigDecimal avgManMonthCost = BigDecimal.valueOf(cell.getNumericCellValue());
                    prjBudget.setAvgManMonthCost(avgManMonthCost);
                }

                // 26 上年递延成本
                cell = row.getCell(26);
                if (cell != null) {
                    BigDecimal defferedAmount = BigDecimal.valueOf(cell.getNumericCellValue());
                    prjBudget.setDefferedAmount(defferedAmount);
                    // prjBudget.setProject( project );
                }

                // 28 递延成本对应人月数
                cell = row.getCell(28);
                if (cell != null) {
                    double defferedManMonth = cell.getNumericCellValue();
                    prjBudget.setDefferedManMonth(defferedManMonth);
                    // prjBudget.setProject( project );
                }


                for (int k = 0; k < 12; k++) // 34 1-12月 人月数
                {
                    cell = row.getCell(34 + k);
                    if (cell == null)
                        continue;
                    double manMonth = cell.getNumericCellValue();
                    if (manMonth <= 0)
                        continue;
                    PrjMonthBudget prjMonth = new PrjMonthBudget();
                    prjMonth.setManMonth((float) manMonth);
                    prjMonth.setMonth((short) k);
                    prjMonth.setYear(year);
                    prjMonth.setDepId(depId);

                    if (isSamePrj) {
                        prevPrjBudget.addPrjMonthBudget(prjMonth);
                    } else
                        prjBudget.addPrjMonthBudget(prjMonth);

                }

                // 47 1月回款
                for (int k = 0; k < 12; k++) // 1-12月 人月
                {
                    calendar.set(Calendar.MONTH, k);
                    cell = row.getCell(47 + k);
                    if (cell == null)
                        continue;
                    double amount = cell.getNumericCellValue();
                    if (amount <= 0)
                        continue;
                    PrjIncomeForecast income = new PrjIncomeForecast();
                    income.setAmount(BigDecimal.valueOf(amount));
                    income.setExpectDate(calendar.getTime());
                    income.setDepId(depId);

                    incomes.add(income);

                }

                if (isSamePrj) {

                    logger.info("Save the same project: " + prevProject.getId() + " " + prevProject.getName() + " budgetId: " + prevPrjBudget.getId());

                    prevProject = this.projectDao.save(prevProject);
                    prevPrjBudget.setProject(prevProject);
                    prevPrjBudget = this.prjBudgetDao.save(prevPrjBudget);

                    // logger.info("Saved  project: " + prevProject.getId() + " " + prevProject.getName() + " budgetId: " + prevPrjBudget.getId());

                    // logger.info("Save the same project!");
                    // logger.info(ImisUtils.objectJsonStr(prevProject));
                    // logger.info(ImisUtils.objectJsonStr(prevPrjBudget));

                    logger.info("income forecast  " + incomes.size());

                    for (PrjIncomeForecast income : incomes) {
                        income.setProject(prevProject);
                        income.setDepId(depId);
                        // logger.info(ImisUtils.objectJsonStr(income));
                    }
                    this.prjIncomeDao.saveAll(incomes);

                    // logger.info("prj confirms " + confirms.size());

                    for (PrjRightsConfirm aconfirm : confirms) {
                        aconfirm.setProject(prevProject);
                        aconfirm.setDepId(depId);
                        // logger.info(ImisUtils.objectJsonStr(aconfirm));
                    }
                    this.prjConfirmDao.saveAll(confirms);

                } else {

                    if (project.getContractAmount() == null && (confirms.size() == 0 || incomes.size() == 0)) {
                        logger.info(" null project: " + project.getName());
                        continue;
                    }

                    newPrjCount++;
                    project.setDepId(depId);
                    project = this.projectDao.save(project);
                    prjBudget.setProject(project);
                    prjBudget = this.prjBudgetDao.save(prjBudget);
                    logger.info(" New  project: " + project.getId() + ": " + project.getName() + " budget id: " + prjBudget.getId() + " prj count:" + newPrjCount);

                    // logger.info(ImisUtils.objectJsonStr(project));
                    // logger.info(ImisUtils.objectJsonStr(prjBudget));

                    // logger.info("income forecast  " + incomes.size() );
                    for (PrjIncomeForecast income : incomes) {
                        income.setProject(project);
                        income.setDepId(depId);
                        // logger.info(ImisUtils.objectJsonStr(income));
                    }
                    this.prjIncomeDao.saveAll(incomes);

                    // logger.info("project confirms " + confirms.size());
                    for (PrjRightsConfirm aconfirm : confirms) {
                        aconfirm.setProject(project);
                        aconfirm.setDepId(depId);
                        logger.info(ImisUtils.objectJsonStr(aconfirm));
                    }
                    this.prjConfirmDao.saveAll(confirms);

                    prevProject = project;
                    prevPrjBudget = prjBudget;

                }

                incomes.clear();
                confirms.clear();
                 // 0:大区名称 1:部门名称 2:客户名称 3:合同编号 4:合同名称 5:核算类型 6:预计签订日期 7:预计合同金额
                // 8:截至18年底是否能拿回验收报告/确认单 9:对应期间 10:本年应确认含税收入（合同金额） 11:本年应确认收入 12:本年应确认成本
                // 13:本年应确认人月数 14:累计已确认收入 15:累计已确认成本
                // 16:累计已确认人月数 17:累计人均收入 18:累计人均成本 19:本年账面收入 20:本年账面成本 21:项目津贴金额 22:预计开票收入
                // 23:预计截至18年底发生成本
                // 24:预计截至18年底投入人月数 25:预计人均成本 26:上年递延成本 27:2018年1-5月实际发生成本 28:递延成本对应的人月数 29:递延成本
                // 30:预计回款金额 31:是否为外包
                // 32:外包部分预计成本 34:1月人月数 35:2月人月数 36:3月人月数 37:4月人月数 38:5月人月数 39:6月人月数
                // 40:7月人月数 41:8月人月数 42:9月人月数 43:10月人月数 44:11月人月数 45:12月人月数 47:1月回款
                // 48:2月回款 49:3月回款 50:4月回款 51:5月回款 52:6月回款 53:7月回款 54:8月回款 55:9月回款
                // 56:10月回款 57:11月回款 58:12月回款 60:截至2017年12月考核报表已确认收入 61:截至2017年12月考核报表已确认成本
                // 62:截至2017年12月审计报表已确认收入 63:截至2017年12月审计报表已确认成本
                // 64: 65:考核报表已确认收入与审计报表已确认收入差异 66:考核报表已确认成本与审计报表已确认成本差异

            }
        }

        newPrjCount = 0;

        sheet = workbook.getSheet("新项目");
        if (sheet == null) {
            workbook.close();
            return;
        }
        logger.info(" read the sheet of " + sheet.getSheetName());
        int rows = sheet.getPhysicalNumberOfRows();
        for (int i = 1; i < rows; i++) {
            Row row = sheet.getRow(i);
            if (row == null)
                continue;

            isSamePrj = false;
            Cell cell = row.getCell(4); // 4 合同（项目）名称
            if (cell == null)
                continue;
            String prjName = cell.getStringCellValue();
            if (prjName == null || prjName.length() == 0)
                continue;

            PrjBudget prjBudget = new PrjBudget();

            Project project = new Project();
            project.setName(prjName);
            project.setPrjClass(PrjClass.LAST_PRJ);

            cell = row.getCell(2); // 2:客户名称
            if (cell != null) {
                String customName = cell.getStringCellValue();
                Custom custom = getOrAddCustomByName(customName);
                project.setCustom(custom);
            }

            cell = row.getCell(3); // 3:合同编号
            if (cell != null) {
                String constractNo = cell.getStringCellValue();
                project.setContractNo(constractNo);
            }

            cell = row.getCell(5); // 5 核算类型  (北一的多两列)
            if (cell != null) {
                String prjType = cell.getStringCellValue();
                if ("实施类".equals(prjType))
                    project.setPrjType(PrjType.PROJECT);
                else if ("派单类".equals(prjType))
                    project.setPrjType(PrjType.TASKS);
                else if ("人月类".equals(prjType))
                    project.setPrjType(PrjType.MAN_MONTH);
                else if ("项目类".equals(prjType))
                    project.setPrjType(PrjType.PROJECT);
                else
                    logger.info("Unknow project type of " + prjType + " in excell file 's line " + (i + 1));
            }

            cell = row.getCell(6); // 6 合同日期
            if (cell != null) {
                Date contractDate = cell.getDateCellValue();
                project.setContractDate(contractDate);
            }

            cell = row.getCell(7); // 7 合同金额
            if (cell != null) {
                double contractAmount = cell.getNumericCellValue();
                BigDecimal ctrAmount = BigDecimal.valueOf(contractAmount);
                project.setContractAmount(ctrAmount);
            }

            // 对应期间
            cell = row.getCell(9); //9 对应期间 2017/4-2017/12 2018年1月-2018年3月 2017/4-2017/12 2018年1月-2018年3月
                                    // 2017年12月至2018年12月 2018年1-12月 2017.5-2018.4
            String confirmName = "";
            Date begDate = null, endDate = null;
            if (cell != null  && cell.getCellType() == CellType.STRING ) {
                String strVal = cell.getStringCellValue();
                if (strVal == null || strVal.length() < 5) {
                    logger.info("Unknow info of 对应期间 " + strVal + " in excell file 's line " + (i + 1));

                } 
                else 
                {

                    String rStrVal = strVal.replace('年', '/');
                    rStrVal = rStrVal.replace('月', '/');
                    rStrVal = rStrVal.replace('至', '-');
                    rStrVal = rStrVal.replace('.', '/');
                    rStrVal = rStrVal.replace('~', '-');
                    int idx = rStrVal.indexOf('-');
                    confirmName = rStrVal;
                    String begDateStr, endDateStr;
                    if( idx != -1)
                    {
                        begDateStr = rStrVal.substring(0, idx);
                        endDateStr = rStrVal.substring(idx + 1);
                        try {
                            begDate = fmt.parse(begDateStr);
                            if( endDateStr.length() < 4)
                            {
                                //只有月
                                idx = endDateStr.indexOf('/');
                                if( idx != -1)
                                    endDateStr = endDateStr.substring(0, idx);
                                int month = Integer.parseInt( endDateStr );

                                Calendar cal = Calendar.getInstance();
                                cal.setTime( begDate);
                                cal.set(Calendar.MONTH, month-1);
                                endDate = cal.getTime();
                            }
                            else
                                endDate = fmt.parse(endDateStr);
                        } catch (ParseException e) {
                            logger.info("Failed to parse the date: " + strVal);
                        }
                    }

                    project.setBeginDate(begDate);
                    project.setEndDate(endDate);

                }

            }
            // 11 确权（ 本年确认收入）
            cell = row.getCell(11);
            if (cell != null) {
                BigDecimal confirmAmount = BigDecimal.valueOf(cell.getNumericCellValue());
                PrjRightsConfirm confirm = new PrjRightsConfirm();
                confirm.setAmount(confirmAmount);
                confirm.setBegDate(begDate);
                confirm.setEndDate(endDate);
                confirm.setName(confirmName);

                Calendar cal = Calendar.getInstance();
                cal.set(Calendar.MONTH, 11);
                confirm.setExpectDate(cal.getTime()); // 今年
                confirms.add(confirm);
            }

            // 25 人均成本
            cell = row.getCell(25);
            if (cell != null) {
                BigDecimal avgManMonthCost = BigDecimal.valueOf(cell.getNumericCellValue());
                prjBudget.setAvgManMonthCost(avgManMonthCost);
                // prjBudget.setProject( project );
            }

            for (int k = 0; k < 12; k++) // 1-12月 人月数
            {
                cell = row.getCell(31 + k);
                if (cell == null)
                    continue;
                double manMonth = cell.getNumericCellValue();
                if (manMonth <= 0)
                    continue;
                PrjMonthBudget prjMonth = new PrjMonthBudget();
                prjMonth.setManMonth((float) manMonth);
                prjMonth.setMonth((short) k);
                prjMonth.setYear(year);

                if (isSamePrj) {
                    prevPrjBudget.addPrjMonthBudget(prjMonth);
                } else
                    prjBudget.addPrjMonthBudget(prjMonth);

            }

            // 44 1月回款
            for (int k = 0; k < 12; k++) // 1-12月 人月
            {
                calendar.set(Calendar.MONTH, k);
                cell = row.getCell(44 + k);
                if (cell == null)
                    continue;
                double amount = cell.getNumericCellValue();
                if (amount <= 0)
                    continue;
                PrjIncomeForecast income = new PrjIncomeForecast();
                income.setAmount(BigDecimal.valueOf(amount));
                income.setExpectDate(calendar.getTime());
                incomes.add(income);

            }

 

                if (project.getContractAmount() == null && (confirms.size() == 0 || incomes.size() == 0)) {
                    logger.info(" null project: " + project.getName());
                    continue;
                }

                newPrjCount++;

                project.setDepId(depId);
                project = this.projectDao.save(project);
                prjBudget.setProject(project);
                prjBudget = this.prjBudgetDao.save(prjBudget);
                logger.info(" New  project! " + project.getId() + ":" + project.getName() + " budgetId: " + prjBudget.getId() );
                // logger.info(ImisUtils.objectJsonStr(project));
                // logger.info(ImisUtils.objectJsonStr(prjBudget));

                // logger.info("income forecast  " + incomes.size());
                for (PrjIncomeForecast income : incomes) {
                    income.setProject(project);
                    income.setDepId(depId);
                    // logger.info(ImisUtils.objectJsonStr(income));
                }
                this.prjIncomeDao.saveAll(incomes);

                // logger.info("project confirms " + confirms.size());
                for (PrjRightsConfirm aconfirm : confirms) {
                    aconfirm.setProject(project);
                    aconfirm.setDepId(depId);
                    // logger.info(ImisUtils.objectJsonStr(aconfirm));
                }
                this.prjConfirmDao.saveAll(confirms);

                prevProject = project;
                prevPrjBudget = prjBudget;

            }

            incomes.clear();
            confirms.clear();

            // 0:大区名称  1:部门名称  2:客户名称  3:合同编号  4:合同名称  5:核算类型  6:预计签订日期  7:预计合同金额  
            // 8:截至18年底是否能拿回验收报告/确认单  9:对应期间  10:本年应确认含税收入（合同金额）  11:本年应确认收入  12:本年应确认成本  13:本年应确认人月数  14:累计已确认收入  15:累计已确认成本
    
            // 16:累计已确认人月数  17:累计人均收入  18:累计人均成本  19:本年账面收入  20:本年账面成本  21:项目津贴金额  22:预计开票收入  23:预计截至18年底发生成本  
            
            // 24:预计截至18年底投入人月数  25:预计人均成本  26:递延成本  27:预计回款金额  28:是否为外包  29:外包部分预计成本  31:1月人月数  
            
            // 32:2月人月数  33:3月人月数  34:4月人月数  35:5月人月数  36:6月人月数  37:7月人月数  38:8月人月数  39:9月人月数  
            
            // 40:10月人月数  41:11月人月数  42:12月人月数  44:1月回款  45:2月回款  46:3月回款  47:4月回款  
            
            // 48:5月回款  49:6月回款  50:7月回款  51:8月回款  52:9月回款  53:10月回款  54:11月回款  55:12月回款  
            
            // E:/公司预算/预算_北2部 2018年.xlsx            
        
        workbook.close();
    }

    private Custom getOrAddCustomByName(String customName) {
        if( customName == null || customName.length() < 2)
            return null;

        Custom custom = this.customDao.findByName(customName);
        if (custom == null) {
            logger.info("add custom : " + customName);
            custom = new Custom();
            custom.setName(customName);
            custom = customDao.save(custom);
        }
        return custom;
    }

}