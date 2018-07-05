package com.cc.po;

import java.util.List;

public class Data<T> {
	private List<T> list;
	private Page page;
	
	public List<T> getList() {
		return list;
	}
	public void setList(List<T> list) {
		this.list = list;
	}
	public Page getPage() {
		return page;
	}
	public void setPage(Page page) {
		this.page = page;
	}
	
}
