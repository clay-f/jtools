package com.clayf.corehelper.helper.excel.upload.impl;

import com.clayf.corehelper.helper.ExcelHelper;
import com.clayf.corehelper.helper.excel.upload.ReadUploadExcel;
import com.google.common.collect.Lists;
import org.apache.poi.ss.usermodel.Workbook;

import java.util.List;

/**
 * 读取上传excel为 list
 * <p>
 * created by f at 2020-12-19 20:02
 */
public class ReadExcelAsList implements ReadUploadExcel<List<String>> {
    @Override
    public List<List<String>> readFile(Workbook workbook) {
        List<List<String>> result = Lists.newArrayList();
        ExcelHelper.readSheet(workbook.getSheetAt(0), result);
        return result;
    }
}
