package com.yo.friendis.common.common.service.impl;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yo.friendis.common.common.mapper.ExportMapper;
import com.yo.friendis.common.common.service.ExportService;

/**
 * @author yhl
 *
 */
@Service("exportService")
public class ExportServiceImpl implements ExportService {

	/**
	 * 字符单位长度
	 */
	private static final int CHAR_WIDTH = 256;
	/**
	 * 表格每列最大的宽度值
	 */
	private static final int MAX_WIDTH = 300;
	/**
	 * 样例，从结果中选取第几行数据，用于创建表头，表格每列宽度。
	 */
	private static final int EXAMPLE_ROW_INDEX = 0;
	/**
	 * 由sql生成文件限制记录数
	 */
	private static Integer MAX_TOTAL = 100 * 10000;
	@Autowired
	protected ExportMapper exportMapper;

	@Override
	public HSSFWorkbook createExcelFromSql(String sql) {
		HSSFWorkbook wb = new HSSFWorkbook();
		Sheet sheet = wb.createSheet();
		if (StringUtils.isEmpty(sql)) {
			return wb;
		}
		List<LinkedHashMap<String, Object>> list = exportMapper.selecltBySql(sql);
		if (list.isEmpty() || list.size() > MAX_TOTAL) {// 一百万条数据
			return wb;
		}
		Map<String, Object> map0 = list.get(EXAMPLE_ROW_INDEX);// 获取第一列数据
		String[] titles = new String[map0.size()];
		int k = 0;
		for (String title : map0.keySet()) {// 获取表头
			titles[k++] = title;
		}
		k = 0;
		Row titleRow = sheet.createRow(0);
		for (k = 0; k < titles.length; k++) {// 创建第一列表头
			Cell cell = titleRow.createCell(k);
			cell.setCellValue(titles[k]);
			String value = formatObject(map0.get(titles[k]));
			int length = titles[k].getBytes().length;
			if (length < value.getBytes().length) {
				length = value.getBytes().length < MAX_WIDTH ? value.getBytes().length : MAX_WIDTH;
			}
			sheet.setColumnWidth(k, length * MAX_WIDTH);
		}
		for (int i = 0; i < list.size(); i++) {
			Map<String, Object> map = list.get(i);
			Row row = sheet.createRow(i + 1);
			for (int j = 0; j < titles.length; j++) {
				Cell cell = row.createCell(j);
				cell.setCellValue(formatObject(map.get(titles[j])));
			}
		}
		return wb;
	}

	protected String formatObject(Object o) {
		if (o == null) {
			return "";
		}
		return o.toString();
	}

}
