package com.yo.friendis.common.common.service;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;

public interface ExportService {
	/**
	 * 根据sql在内存中创建excel文件，限制100w记录数
	 * @param sql
	 * @return
	 */
	HSSFWorkbook createExcelFromSql(String sql);
}
