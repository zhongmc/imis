package com.ynet.imis.utils;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.ynet.imis.bean.CostCollectionItem;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

public class ImisUtils {

    private static Logger logger = LoggerFactory.getLogger("ImisUtils");
    public static ObjectMapper mapper = new ObjectMapper();

    static {
        // 转换为格式化的json
        mapper.enable(SerializationFeature.INDENT_OUTPUT);

        // 如果json中有新增的字段并且是实体类类中不存在的，不报错
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    public static String objectJsonStr(Object obj) {

        try {
            return mapper.writeValueAsString(obj);
        } catch (Exception e) {
            System.out.println("Failed to change obj: " + obj + e);
        }

        return null;

    }

    public static byte[] exportCostCollection2Excel(Map<String, List<CostCollectionItem>> collects) throws Exception {

        Workbook workbook = new XSSFWorkbook();

        Set<String> keys = collects.keySet();
        for (String key : keys) {
            ImisUtils.logger.info("Create sheet: " + key);

            List<CostCollectionItem> collectItems = collects.get(key);
            Sheet sheet = workbook.createSheet(key);
            ImisUtils.writeCostCollectionToExcel(sheet, collectItems);
        }

        ByteArrayOutputStream bo = new ByteArrayOutputStream();
        workbook.write(bo);

        bo.close();
        return bo.toByteArray();

    }

    static private void writeCostCollectionToExcel(Sheet sheet, List<CostCollectionItem> collectItems) {
        String[] titles = { "总计", "一月", "二月", "三月", "四月", "五月", "六月", "七月", "八月", "九月", "十月", "十一月", "十二月", };
        Row row = sheet.createRow(0);
        for (int i = 0; i < titles.length; i++) {
            Cell cell = row.createCell(1 + i, CellType.STRING);
            cell.setCellValue(titles[i]);
        }

        int rowCount = 1;
        for (CostCollectionItem item : collectItems) {

            ImisUtils.logger.info("Create row: " + rowCount + " " + item.getName());

            row = sheet.createRow(rowCount);
            Cell cell = row.createCell(0, CellType.STRING);
            cell.setCellValue(item.getName());

            cell = row.createCell(1, CellType.NUMERIC);
            cell.setCellValue(item.getSum().doubleValue());

            for (int i = 0; i < 12; i++) {
                cell = row.createCell(2 + i, CellType.NUMERIC);
                cell.setCellValue(item.getAmounts()[i].doubleValue());
            }

            rowCount++;

        }

    }

}