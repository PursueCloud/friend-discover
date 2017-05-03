package com.yo.friendis.common.common.util;

import com.yo.friendis.common.easyui.bean.DataGrid;

import java.util.ArrayList;
import java.util.List;

/**
 * 带页脚的datagrid实现
 * @author Jay.huang
 *
 */
public class DataGridWithFooter extends DataGrid {
	private List<?> rows;
	private long total;
	private List<?> footer;//页脚

	public DataGridWithFooter() {
		new DataGrid(new ArrayList<Object>(), 0);
	}

	public DataGridWithFooter(List<?> rows, long total) {
		this.rows = rows;
		this.total = total;
		if (rows == null) {
			this.rows = new ArrayList<Object>();
		}
	}

	public DataGridWithFooter(List<?> rows, List<?> footer, long total) {
		this.rows = rows;
		this.total = total;
		if (rows == null) {
			this.rows = new ArrayList<Object>();
		}
		this.footer = footer;
	}
	
	public List<?> getFooter() {
		return footer;
	}

	public void setFooter(List<?> footer) {
		this.footer = footer;
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
