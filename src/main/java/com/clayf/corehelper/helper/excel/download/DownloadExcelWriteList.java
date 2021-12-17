package com.clayf.corehelper.helper.excel.download;

import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Sheet;

import java.util.List;

/**
 * 将所有data写入excel。不包含表头
 * <p>
 * created by f at 2020-12-19 20:02
 *
 * @param <D> 写入excel的数据类型
 */
public interface DownloadExcelWriteList<D> extends DownloadExcel {
    /**
     * 写入数据到excel
     *
     * @param sheet excel sheet
     * @param data 数据
     * @param curRowNum 当前行号
     * @param cellStyle cell配置
     * @param creationHelper 写入数据样式
     */
    void writeBody(Sheet sheet, List<D> data, int curRowNum, CellStyle cellStyle, CreationHelper creationHelper);
}
