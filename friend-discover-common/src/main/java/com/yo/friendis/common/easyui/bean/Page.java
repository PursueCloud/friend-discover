package com.yo.friendis.common.easyui.bean;

public class Page {
	private int page = 1;
	private int rows = 10;
	private String sort;
	private String order;

	public int getPageNum() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public int getPageSize() {
		return rows;
	}

	public String getSort() {
		return sort;
	}

	public void setSort(String sort) {
		this.sort = sort;
	}

	public String getOrder() {
		return order;
	}

	public void setOrder(String order) {
		this.order = order;
	}

	public void setRows(int rows) {
		this.rows = rows;
	}

	public int getStart() {
		return rows * (page - 1);
	}

	public int getEnd() {
		return rows * page;
	}
}
