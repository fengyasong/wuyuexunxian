package com.aicat.common.utils;

import com.github.pagehelper.PageInfo;

import java.io.Serializable;
import java.util.List;

public class PageUtils implements Serializable {
	private static final long serialVersionUID = 1L;
	private int total;
	private List<?> rows;

	public PageUtils(List<?> list, int total) {
		this.rows = list;
		this.total = total;
	}
	public PageUtils(PageInfo pageInfo) {
		this.rows = pageInfo.getList();
		this.total = (int) pageInfo.getTotal();
	}

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}

	public List<?> getRows() {
		return rows;
	}

	public void setRows(List<?> rows) {
		this.rows = rows;
	}

}
