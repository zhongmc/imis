package com.ynet.imis.utils;

import java.io.File;
import java.io.FileInputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.ynet.imis.service.utils.InitData;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

public class Test {

    public static void main(String[] args) {

        try {

            SimpleDateFormat fmt = new SimpleDateFormat("yyyy/MM");
            SimpleDateFormat outFmt = new SimpleDateFormat("yyyy/MM/dd");
            String strVal = "2017/4";
            Date aDate = fmt.parse(strVal);
            System.out.println(
                    String.format("unformate %s to: %s LongValue: %s", strVal, outFmt.format(aDate), aDate.getTime()));

            strVal = "2017/12";
            aDate = fmt.parse(strVal);
            System.out.println(
                    String.format("unformate %s to: %s LongValue: %s", strVal, outFmt.format(aDate), aDate.getTime()));

            strVal = "2018/11";
            aDate = fmt.parse(strVal);
            System.out.println(
                    String.format("unformate %s to: %s LongValue: %s", strVal, outFmt.format(aDate), aDate.getTime()));

            Date today = new Date();
            System.out.println(outFmt.format(today) + " " + today.getTime());

            Test t = new Test();

            t.bigdecimalTest();

            // String fileName =
            // "C:\\xuexi\\imis\\imisServer\\src\\main\\resources\\test.xlsx";
            // t.excelDateCellTest(fileName);

            // fileName =
            // "C:\\xuexi\\imis\\imisServer\\src\\main\\resources\\initData.json";
            // t.importJsonInitData(fileName);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void bigdecimalTest() {
        BigDecimal bd1, bd2;
        bd1 = BigDecimal.valueOf(35629611.02);
        bd2 = BigDecimal.valueOf(11470184.8088);
        try {
            BigDecimal bd3 = bd2.divide(bd1, 2);
            System.out.println(String.format("%s / %s = %s", bd2.doubleValue(), bd1.doubleValue(), bd3.doubleValue()));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void importJsonInitData(String fileName) throws Exception {

        ObjectMapper mapper = new ObjectMapper();
        // 转换为格式化的json
        mapper.enable(SerializationFeature.INDENT_OUTPUT);

        // 如果json中有新增的字段并且是实体类类中不存在的，不报错
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        InitData initData = mapper.readValue(new File(fileName), InitData.class);

        System.out.println(ImisUtils.objectJsonStr(initData));
    }

    private void excelDateCellTest(String fileName) throws Exception {
        System.out.println();
        System.out.println(fileName);
        File file = new File(fileName);
        FileInputStream instream = new FileInputStream(file);

        // XSSFWorkbook workbook;
        Workbook workbook = WorkbookFactory.create(instream);
        Sheet sheet = workbook.getSheetAt(0);
        SimpleDateFormat fmt = new SimpleDateFormat("yyyy/MM/dd");

        Row row = sheet.getRow(1);
        Cell cell = row.getCell(1);
        CellType cellType = cell.getCellType();
        double nvalue = cell.getNumericCellValue();
        long lvalue = (long) nvalue;

        Date date = cell.getDateCellValue();
        // String strVal = cell.getStringCellValue();
        System.out.println(cellType);
        System.out.println(fmt.format(date));
        System.out.println(lvalue);
        System.out.println(date.getTime());

        cell = row.getCell(2);
        cellType = cell.getCellType();
        date = cell.getDateCellValue();
        nvalue = cell.getNumericCellValue();
        lvalue = (long) nvalue;

        System.out.println(cellType);
        System.out.println(fmt.format(date));
        System.out.println(lvalue);
        System.out.println(date.getTime());

        cell = row.getCell(3);
        cellType = cell.getCellType();
        date = cell.getDateCellValue();
        nvalue = cell.getNumericCellValue();
        lvalue = (long) nvalue;
        System.out.println(cellType);
        System.out.println(fmt.format(date));
        System.out.println(lvalue);
        System.out.println(date.getTime());

        cell = row.getCell(4);
        cellType = cell.getCellType();
        date = cell.getDateCellValue();
        nvalue = cell.getNumericCellValue();
        lvalue = (long) nvalue;
        System.out.println(cellType);
        System.out.println(fmt.format(date));
        System.out.println(lvalue);
        System.out.println(date.getTime());

        cell = row.getCell(5);
        cellType = cell.getCellType();
        date = cell.getDateCellValue();
        nvalue = cell.getNumericCellValue();
        lvalue = (long) nvalue;
        System.out.println(cellType);
        System.out.println(fmt.format(date));
        System.out.println(lvalue);
        System.out.println(date.getTime());

        workbook.close();

    }

    public void importCostItemInfo(String fileName) throws Exception {
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

        Sheet sheet = workbook.getSheet("部门管理费用");
        if (sheet != null) {
            System.out.println();
            System.out.println(sheet.getSheetName());
            System.out.println();
            // logger.info("read the sheet of " + sheet.getSheetName());
            int rc = sheet.getPhysicalNumberOfRows();
            for (int i = 3; i < rc; i++) {
                Row row = sheet.getRow(i);
                if (row == null)
                    continue;
                Cell cell = row.getCell(1);
                if (cell == null)
                    break;
                String title = cell.getStringCellValue();

                if (title.trim().length() == 0)
                    break;

                // System.out.println(title);
                int idx = title.indexOf('-');
                int idx1 = -1;

                String groupName = "";
                if (idx != -1)
                    idx1 = title.indexOf('-', idx + 1);

                if (idx != -1 && idx1 != -1) {
                    groupName = title.substring(0, idx1);
                } else if (idx != -1) {
                    groupName = title.substring(0, idx);
                }

                String desc = "";
                cell = row.getCell(16);
                if (cell != null)
                    desc = cell.getStringCellValue();

                System.out.println(String.format("\"%s\" \"%s\" \"%s\"", groupName, title, desc));
            }

        }

        instream.close();
        workbook.close();

    }

}