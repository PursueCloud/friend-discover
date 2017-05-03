package com.yo.friendis.common.easyui.bean;

import java.util.ArrayList;
import java.util.List;

public class DataGrid {
	private List<?> rows;
	private long total;

	public DataGrid() {
		new DataGrid(new ArrayList<Object>(), 0);
	}

	public DataGrid(List<?> list, long total) {
		this.rows = list;
		this.total = total;
		if (list == null) {
			this.rows = new ArrayList<Object>();
		}
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
