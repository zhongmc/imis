package com.ynet.imis.service.utils;

import java.io.File;

import org.springframework.web.multipart.MultipartFile;

public interface DataImportService {

    public int ImportDepartmentBudgetInfo(File file, Long depId, int realDataMonth);

    public int importDepartmentBudgetInfo(MultipartFile file, Long depId, int realDataMonth);
}