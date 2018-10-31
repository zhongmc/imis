package com.ynet.imis.imisserver.service;

import java.io.File;
import java.io.FileInputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.ynet.imis.ImisApplication;
import com.ynet.imis.domain.budget.PrjBudget;
import com.ynet.imis.domain.budget.PrjIncomeForecast;
import com.ynet.imis.domain.budget.PrjRightsConfirm;
import com.ynet.imis.domain.project.Project;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PrjImportServiceTest extends ImisApplication {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Test
    public void whenImportSuccess() throws Exception {

        File file = new File("D:/公司预算/预算_北1部 2018年.xlsx");
        FileInputStream instream = new FileInputStream(file);

        // XSSFWorkbook workbook;
        Workbook workbook = WorkbookFactory.create(instream);

        SimpleDateFormat fmt = new SimpleDateFormat("yyyy/mm");
        Date begDate = null, endDate = null;
        // XSSFSheet
        Project prevProject = null;
        PrjRightsConfirm confirm = null;
        PrjBudget prevPrjBudget = null;
        List<PrjIncomeForecast> incomes = new ArrayList<PrjIncomeForecast>();
        List<PrjRightsConfirm> confirms = new ArrayList<PrjRightsConfirm>();
        boolean isSamePrj = false;
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);

        Sheet sheet = workbook.getSheet("延续性项目");
        if (sheet != null) {
            logger.info("read the sheet of " + sheet.getSheetName());
            Row row = sheet.getRow(0);
            int cs = row.getPhysicalNumberOfCells();
            for (int i = 0; i < cs; i++) {
                Cell cell = row.getCell(i);
                if (cell != null) {
                    logger.info("%s:%s ", i, cell.getStringCellValue());
                }
            }
        }

        sheet = workbook.getSheet("新项目");
        if (sheet != null) {
            logger.info("read the sheet of " + sheet.getSheetName());
            Row row = sheet.getRow(0);
            int cs = row.getPhysicalNumberOfCells();
            for (int i = 0; i < cs; i++) {
                Cell cell = row.getCell(i);
                if (cell != null) {
                    logger.info("%s:%s ", i, cell.getStringCellValue());
                }
            }
        }

        instream.close();
        workbook.close();

    }
}