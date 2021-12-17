package com.clayf.corehelper.helper.excel.upload;

import org.apache.poi.ss.usermodel.Workbook;

import java.util.List;

/**
 * 读取excel文件接口
 * <p>
 * created by f at 2020-12-19 20:02
 *
 * @param <R> 泛型
 */
public interface ReadUploadExcel<R> {
    /**
     * 读取excel文件
     *
     * @param workbook excel
     * @return list
     */
    List<R> readFile(Workbook workbook);
}
