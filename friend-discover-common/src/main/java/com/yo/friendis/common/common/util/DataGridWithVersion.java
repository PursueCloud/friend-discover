package com.yo.friendis.common.common.util;

import com.yo.friendis.common.easyui.bean.DataGrid;

import java.util.ArrayList;
import java.util.List;

/**
 * 带数据版本的datagrid实现
 * @author Jay.huang
 *
 */
public class DataGridWithVersion extends DataGrid {
	private List<?> rows;
	private long total;
	private String version;

	public DataGridWithVersion() {
		new DataGrid(new ArrayList<Object>(), 0);
	}

	public DataGridWithVersion(List<?> rows, long total) {
		this.rows = rows;
		this.total = total;
		if (rows == null) {
			this.rows = new ArrayList<Object>();
		}
	}

	public DataGridWithVersion(List<?> rows, String version, long total) {
		this.rows = rows;
		this.total = total;
		if (rows == null) {
			this.rows = new ArrayList<Object>();
		}
		this.version = version;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public List<?> getRows() {
		return rows;
	}

	public void setRows(List<?> rows) {
		this.rows = rows;
	}

	public long getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}
}
