package com.ynet.imis.utils;

import java.io.File;
import java.io.FileInputStream;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.ynet.imis.domain.budget.PrjBudget;
import com.ynet.imis.domain.budget.PrjIncomeForecast;
import com.ynet.imis.domain.budget.PrjMonthBudget;
import com.ynet.imis.domain.budget.PrjRightsConfirm;
import com.ynet.imis.domain.org.Custom;
import com.ynet.imis.domain.org.Department;
import com.ynet.imis.domain.project.Project;
import com.ynet.imis.domain.project.Project.PrjClass;
import com.ynet.imis.domain.project.Project.PrjType;
import com.ynet.imis.repository.org.DepartmentRepository;
import com.ynet.imis.service.org.DepartmentService;
import com.ynet.imis.service.project.ProjectService;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MultipartFile;

public class ProjectImport {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    ProjectService prjService;

    @Autowired
    DepartmentService depService;

    @Autowired
    DepartmentRepository depDao;

    public void doImport() throws Exception {
        String depName, fileName;

        depName = "北一部";
        Department dep = depDao.findByName(depName);
        fileName = "D:/公司预算/预算_北1部 2018年.xlsx";
        prjService.importFile(new File(fileName), dep.getId());

        depName = "北二部";
        dep = depDao.findByName(depName);
        fileName = "D:/公司预算/预算_北2部 2018年.xlsx";
        prjService.importFile(new File(fileName), dep.getId());

        depName = "北三部";
        dep = depDao.findByName(depName);
        fileName = "D:/公司预算/预算_北3部 2018年.xlsx";
        prjService.importFile(new File(fileName), dep.getId());

        depName = "东一部";
        dep = depDao.findByName(depName);
        fileName = "D:/公司预算/预算_东1部 2018年.xlsx";
        prjService.importFile(new File(fileName), dep.getId());

        depName = "东二部";
        dep = depDao.findByName(depName);
        fileName = "D:/公司预算/预算_东2部 2018年.xlsx";
        prjService.importFile(new File(fileName), dep.getId());

        depName = "东三部";
        dep = depDao.findByName(depName);
        fileName = "D:/公司预算/预算_东3部 2018年.xlsx";
        prjService.importFile(new File(fileName), dep.getId());

        depName = "南一部";
        dep = depDao.findByName(depName);
        fileName = "D:/公司预算/预算_南1部 2018年.xlsx";
        prjService.importFile(new File(fileName), dep.getId());

        depName = "南二部";
        dep = depDao.findByName(depName);
        fileName = "D:/公司预算/预算_南2部 2018年.xlsx";
        prjService.importFile(new File(fileName), dep.getId());

        depName = "南三部";
        dep = depDao.findByName(depName);
        fileName = "D:/公司预算/预算_南3部 2018年.xlsx";
        prjService.importFile(new File(fileName), dep.getId());

    }

    public static void main(String[] args) {

        ProjectImport t = new ProjectImport();
        try {
             t.whenImportSuccess("E:/公司预算/预算_北1部 2018年.xlsx");
             t.whenImportSuccess("E:/公司预算/预算_北2部 2018年.xlsx");
             t.whenImportSuccess("E:/公司预算/预算_东1部 2018年.xlsx");
             t.whenImportSuccess("E:/公司预算/预算_南2部 2018年.xlsx");
            // t.importFile(null, Long.valueOf(1122));

            //t.doImport();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void whenImportSuccess(String fileName) throws Exception {
        System.out.println();
        System.out.println(fileName);
        File file = new File(fileName);
        FileInputStream instream = new FileInputStream(file);

        // XSSFWorkbook workbook;
        Workbook workbook = WorkbookFactory.create(instream);

        // SimpleDateFormat fmt = new SimpleDateFormat("yyyy/mm");
        // Date begDate = null, endDate = null;
        // // XSSFSheet
        // Project prevProject = null;
        // PrjRightsConfirm confirm = null;
        // PrjBudget prevPrjBudget = null;
        // List<PrjIncomeForecast> incomes = new ArrayList<PrjIncomeForecast>();
        // List<PrjRightsConfirm> confirms = new ArrayList<PrjRightsConfirm>();
        // boolean isSamePrj = false;
        // Calendar calendar = Calendar.getInstance();
        // int year = calendar.get(Calendar.YEAR);

        Sheet sheet = workbook.getSheet("延续性项目");
        if (sheet != null) {
            System.out.println();
            System.out.println(sheet.getSheetName());
            // logger.info("read the sheet of " + sheet.getSheetName());
            Row row = sheet.getRow(0);
            int cs = row.getPhysicalNumberOfCells();
            for (int i = 0; i < cs; i++) {
                Cell cell = row.getCell(i);
                if (cell != null) {
                    // logger.info("{}: {}", i, cell.getStringCellValue());
                    if (i % 8 == 0)
                        System.out.println();
                    System.out.print(String.format("%s:%s  ", i, cell.getStringCellValue()));
                }
            }
        }

        sheet = workbook.getSheet("新项目");
        if (sheet != null) {
            // logger.info("read the sheet of " + sheet.getSheetName());
            System.out.println();
            System.out.println();
            System.out.println(sheet.getSheetName());
            Row row = sheet.getRow(0);
            int cs = row.getPhysicalNumberOfCells();
            for (int i = 0; i < cs; i++) {
                Cell cell = row.getCell(i);
                if (cell != null) {
                    if (i % 8 == 0)
                        System.out.println();
                    System.out.print(String.format("%s:%s  ", i, cell.getStringCellValue()));
                    // logger.info("{}:{}", i, cell.getStringCellValue());
                }
            }
        }

        instream.close();
        workbook.close();

    }

    // 从文件中导入 //depId 所属部门
    public void importFile(MultipartFile file, Long depId) throws Exception {
        // logger.info("import project file: " + file.getName());

        File afile = new File("D:/公司预算/预算_北1部 2018年.xlsx");
        FileInputStream instream = new FileInputStream(afile);

        // XSSFWorkbook workbook;
        Workbook workbook = WorkbookFactory.create(instream);

        SimpleDateFormat fmt = new SimpleDateFormat("yyyy/mm");
        Date begDate = null, endDate = null;

        Project prevProject = null;
        PrjRightsConfirm confirm = null;
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
                confirm = null;
                Cell cell = row.getCell(4); // 4:合同名称
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
                        logger.info("Unknow project type of " + prjType + " in excell file " + file.getName()
                                + " 's line " + i + 1);
                }

                cell = row.getCell(6); // 合同日期
                if (cell != null) {
                    Date contractDate = cell.getDateCellValue();
                    project.setContractDate(contractDate);
                }

                cell = row.getCell(7); // 合同金额
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
                cell = row.getCell(9); // 对应期间 2017/4-2017/12 2018年1月-2018年3月 2017/4-2017/12 2018年1月-2018年3月
                                       // 2017年12月至2018年12月
                String confirmName = "";
                if (cell != null) {
                    String strVal = cell.getStringCellValue();
                    if (strVal == null || strVal.length() < 5) {
                        logger.info("Unknow info of 对应期间 " + strVal + " in excell file s line " + (i + 1));

                    } else {
                        String rStrVal = strVal.replace('年', '/');
                        rStrVal = rStrVal.replace('月', '/');
                        rStrVal = rStrVal.replace('至', '-');
                        int idx = rStrVal.indexOf('-');
                        confirmName = rStrVal;
                        String begDateStr, endDateStr;
                        begDateStr = rStrVal.substring(0, idx);
                        endDateStr = rStrVal.substring(idx + 1);
                        try {
                            begDate = fmt.parse(begDateStr);
                            endDate = fmt.parse(endDateStr);
                        } catch (ParseException e) {
                            logger.info("Failed to parse the date: " + strVal);
                        }

                        project.setBeginDate(begDate);
                        project.setEndDate(endDate);

                        if (isSamePrj) {
                            prevProject.setEndDate(endDate); /////
                        }
                    }

                }
                // 10 确权（ 本年确认含税收入）
                cell = row.getCell(10);
                if (cell != null) {
                    BigDecimal confirmAmount = BigDecimal.valueOf(cell.getNumericCellValue());
                    confirm = new PrjRightsConfirm();
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
                    incomes.add(income);

                }

                if (isSamePrj) {

                    // prevProject = this.projectDao.save(prevProject);
                    prevPrjBudget.setProject(prevProject);
                    // prevPrjBudget = this.prjBudgetDao.save(prevPrjBudget);

                    logger.info("Save the same project!");
                    logger.info(ImisUtils.objectJsonStr(prevProject));
                    logger.info(ImisUtils.objectJsonStr(prevPrjBudget));

                    logger.info("income forecast  ");

                    for (PrjIncomeForecast income : incomes) {
                        income.setProject(project);
                        income.setDepId(depId);
                        logger.info(ImisUtils.objectJsonStr(income));
                    }
                    // this.prjIncomeDao.saveAll(incomes);

                    logger.info("prj confirms ");

                    for (PrjRightsConfirm aconfirm : confirms) {
                        aconfirm.setProject(project);
                        aconfirm.setDepId(depId);
                        logger.info(ImisUtils.objectJsonStr(aconfirm));
                    }
                    // this.prjConfirmDao.saveAll(confirms);

                } else {

                    if (project.getContractAmount() == null && (confirms.size() == 0 || incomes.size() == 0)) {
                        logger.info(" null project: " + project.getName());
                        continue;
                    }

                    newPrjCount++;
                    project.setDepId(depId);
                    // project = this.projectDao.save(project);
                    prjBudget.setProject(project);
                    // prjBudget = this.prjBudgetDao.save(prjBudget);
                    logger.info(" New  project! " + newPrjCount);
                    logger.info(ImisUtils.objectJsonStr(project));

                    logger.info(ImisUtils.objectJsonStr(prjBudget));

                    logger.info("income forecast  ");
                    for (PrjIncomeForecast income : incomes) {
                        income.setProject(project);
                        income.setDepId(depId);
                        logger.info(ImisUtils.objectJsonStr(income));
                    }
                    // this.prjIncomeDao.saveAll(incomes);

                    logger.info("project confirms");
                    for (PrjRightsConfirm aconfirm : confirms) {
                        aconfirm.setProject(project);
                        aconfirm.setDepId(depId);
                        logger.info(ImisUtils.objectJsonStr(aconfirm));
                    }
                    // this.prjConfirmDao.saveAll(confirms);

                    prevProject = project;
                    prevPrjBudget = prjBudget;

                }

                incomes.clear();
                confirms.clear();
                // 0 客户名称 1 合同编号 2 合同名称 3 核算类型 4 预计签订日期 5 预计合同金额 6 截至18年底是否能拿回验收报告/确认单
                // 7 对应期间 8 本年应确认收入 9 本年应确认成本 10 本年应确认人月数 11 预计开票收入 12 预计截至18年底发生成本
                // 13 预计截至18年底投入人月数 14 预计人均成本 15 上年递延成本 16 2018年1-5月实际发生成本 17 递延成本对应的人月数 18 递延成本
                // 19 预计回款金额 20 是否为外包 21 外包部分预计成本 22 1月人月数 23 2月人月数 24 3月人月数 25 4月人月数 26 5月人月数
                // 27 6月人月数
                // 28 7月人月数 29 8月人月数 30 9月人月数 31 10月人月数 32 11月人月数 33 12月人月数
                // 34 1月回款 35 2月回款 36 3月回款 37 4月回款 38 5月回款 39 6月回款 40 7月回款 41 8月回款 42 9月回款 43
                // 10月回款 44 11月回款 45 12月回款
                // 46 截至2017年12月考核报表已确认收入 47 截至2017年12月考核报表已确认成本 48 截至2017年12月审计报表已确认收入
                // 49 截至2017年12月审计报表已确认成本 50 考核报表已确认收入与审计报表已确认收入差异 51 考核报表已确认成本与审计报表已确认成本差异
                // 52 累计已发生成本 53 累计已发生人月数

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
            confirm = null;
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
                if (prevProject != null && constractNo.equals(prevProject.getContractNo())) {
                    // same project
                    isSamePrj = true;
                }
            }

            cell = row.getCell(7); // 7 核算类型
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

            cell = row.getCell(8); // 8 合同日期
            if (cell != null) {
                Date contractDate = cell.getDateCellValue();
                project.setContractDate(contractDate);
            }

            cell = row.getCell(9); // 9 合同金额
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
            cell = row.getCell(11); // 对应期间 2017/4-2017/12 2018年1月-2018年3月 2017/4-2017/12 2018年1月-2018年3月
                                    // 2017年12月至2018年12月
            String confirmName = "";
            if (cell != null) {
                String strVal = cell.getStringCellValue();
                if (strVal == null || strVal.length() < 5) {
                    logger.info("Unknow info of 对应期间 " + strVal + " in excell file 's line " + (i + 1));

                } else {
                    String rStrVal = strVal.replace('年', '/');
                    rStrVal = rStrVal.replace('月', '/');
                    rStrVal = rStrVal.replace('至', '-');
                    int idx = rStrVal.indexOf('-');
                    confirmName = rStrVal;
                    String begDateStr, endDateStr;
                    begDateStr = rStrVal.substring(0, idx);
                    endDateStr = rStrVal.substring(idx + 1);
                    try {
                        begDate = fmt.parse(begDateStr);
                        endDate = fmt.parse(endDateStr);
                    } catch (ParseException e) {
                        logger.info("Failed to parse the date: " + strVal);
                    }

                    project.setBeginDate(begDate);
                    project.setEndDate(endDate);

                    if (isSamePrj) {
                        prevProject.setEndDate(endDate); /////
                    }
                }

            }
            // 13 确权（ 本年确认收入）
            cell = row.getCell(13);
            if (cell != null) {
                BigDecimal confirmAmount = BigDecimal.valueOf(cell.getNumericCellValue());
                confirm = new PrjRightsConfirm();
                confirm.setAmount(confirmAmount);
                confirm.setBegDate(begDate);
                confirm.setEndDate(endDate);
                confirm.setName(confirmName);

                Calendar cal = Calendar.getInstance();
                cal.set(Calendar.MONTH, 11);
                confirm.setExpectDate(cal.getTime()); // 今年
                confirms.add(confirm);
            }

            // 27 人均成本
            cell = row.getCell(27);
            if (cell != null) {
                BigDecimal avgManMonthCost = BigDecimal.valueOf(cell.getNumericCellValue());
                prjBudget.setAvgManMonthCost(avgManMonthCost);
                // prjBudget.setProject( project );
            }

            for (int k = 0; k < 12; k++) // 1-12月 人月数
            {
                cell = row.getCell(33 + k);
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

            // 46 1月回款
            for (int k = 0; k < 12; k++) // 1-12月 人月
            {
                calendar.set(Calendar.MONTH, k);
                cell = row.getCell(46 + k);
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

            if (isSamePrj) {
                logger.info("Save the same project!");
                logger.info(ImisUtils.objectJsonStr(prevProject));

                logger.info(ImisUtils.objectJsonStr(prevPrjBudget));
                // prevProject = this.projectDao.save(prevProject);
                prevPrjBudget.setProject(prevProject);
                // prevPrjBudget = this.prjBudgetDao.save(prevPrjBudget);

                logger.info("income forecast  ");

                for (PrjIncomeForecast income : incomes) {
                    income.setProject(project);
                    income.setDepId(depId);
                    logger.info(ImisUtils.objectJsonStr(income));
                }
                // this.prjIncomeDao.saveAll(incomes);

                logger.info("prj confirms ");

                for (PrjRightsConfirm aconfirm : confirms) {
                    aconfirm.setProject(project);
                    aconfirm.setDepId(depId);
                    logger.info(ImisUtils.objectJsonStr(aconfirm));
                }
                // this.prjConfirmDao.saveAll(confirms);

            } else {

                if (project.getContractAmount() == null && (confirms.size() == 0 || incomes.size() == 0)) {
                    logger.info(" null project: " + project.getName());
                    continue;
                }

                newPrjCount++;

                project.setDepId(depId);
                // project = this.projectDao.save(project);
                prjBudget.setProject(project);
                // prjBudget = this.prjBudgetDao.save(prjBudget);
                logger.info(" New  project!");
                logger.info(ImisUtils.objectJsonStr(project));
                logger.info(ImisUtils.objectJsonStr(prjBudget));

                logger.info("income forecast  ");
                for (PrjIncomeForecast income : incomes) {
                    income.setProject(project);
                    income.setDepId(depId);
                    logger.info(ImisUtils.objectJsonStr(income));
                }
                // this.prjIncomeDao.saveAll(incomes);

                logger.info("project confirms");
                for (PrjRightsConfirm aconfirm : confirms) {
                    aconfirm.setProject(project);
                    aconfirm.setDepId(depId);
                    logger.info(ImisUtils.objectJsonStr(aconfirm));
                }
                // this.prjConfirmDao.saveAll(confirms);

                prevProject = project;
                prevPrjBudget = prjBudget;

            }

            incomes.clear();
            confirms.clear();

            // 0 客户名称 1 合同编号 2 合同名称 3 核算类型 4 预计签订日期 5 预计合同金额 6 截至18年底是否能拿回验收报告/确认单 7 对应期间 8
            // 本年应确认收入 9 本年应确认成本
            // 10 本年应确认人月数 11 预计开票收入 12 预计截至18年底发生成本 13 预计截至18年底投入人月数 14 预计人均成本 15 递延成本 16
            // 预计回款金额 17 是否为外包 18 外包部分预计成本
            // 18 1月人月数 19 2月人月数 20 3月人月数 21 4月人月数 22 5月人月数 23 6月人月数 24 7月人月数 25 8月人月数 26
            // 9月人月数 27 10月人月数 28 11月人月数 29 12月人月数
            // 30 1月回款 31 2月回款 32 3月回款 33 4月回款 34 5月回款 35 6月回款 36 7月回款 37 8月回款 38 9月回款 39
            // 10月回款 40 11月回款 41 12月回款

            // 0:大区名称 1:部门名称 2:客户名称 3:合同编号 4:合同名称 5:项目编号 6:项目名称 7:核算类型
            // 8:预计签订日期 9:预计合同金额 10:截至18年底是否能拿回验收报告/确认单 11:对应期间 12:本年应确认含税收入（合同金额）
            // 13:本年应确认收入 14:本年应确认成本 15:本年应确认人月数
            // 16:累计已确认收入 17:累计已确认成本 18:累计已确认人月数 19:累计人均收入 20:累计人均成本 21:本年账面收入 22:本年账面成本
            // 23:项目津贴金额
            // 24:预计开票收入 25:预计截至18年底发生成本 26:预计截至18年底投入人月数 27:预计人均成本 28:递延成本 29:预计回款金额
            // 30:是否为外包 31:外包部分预计成本 33:1月人月数 34:2月人月数 35:3月人月数 36:4月人月数 37:5月人月数 38:6月人月数
            // 39:7月人月数
            // 40:8月人月数 41:9月人月数 42:10月人月数 43:11月人月数 44:12月人月数 46:1月回款 47:2月回款
            // 48:3月回款 49:4月回款 50:5月回款 51:6月回款 52:7月回款 53:8月回款 54:9月回款 55:10月回款
            // 56:11月回款 57:12月回款

        }
        instream.close();
        workbook.close();
    }

    private Custom getOrAddCustomByName(String customName) {

        logger.info("New custom:" + customName);
        Custom custom = new Custom();
        custom.setName(customName);
        return custom;
        // Custom custom = this.customDao.findByName(customName);
        // if (custom == null) {
        // logger.info("add custom : " + customName);
        // custom = new Custom();
        // custom.setName(customName);
        // custom = customDao.save(custom);
        // }
        // return custom;
    }

}