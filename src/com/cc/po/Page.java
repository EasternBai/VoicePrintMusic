package com.cc.po;

public class Page {
	private int pageIndex=1;
	private int pageCount=0;
	private int begin=0;
	private int end=0;
	private int pageSize=5;
	private int dataCount=0;
	
	public Page() {
		super();
	}

	public Page(int p_pageIndex,int p_pageSize,int p_dataCount) {
		this.dataCount=p_dataCount;
		
		if(p_pageIndex>0){
			this.pageIndex=p_pageIndex;
		}
		
		if(p_pageSize>0){
			this.pageSize=p_pageSize;
		}
		
		this.pageCount=dataCount%pageSize==0?dataCount/pageSize:dataCount/pageSize+1;
		this.begin=(pageIndex-1)*pageSize;
	}

	public int getPageIndex() {
		return pageIndex;
	}

	public void setPageIndex(int pageIndex) {
		this.pageIndex = pageIndex;
	}

	public int getPageCount() {
		return pageCount;
	}

	public void setPageCount(int pageCount) {
		this.pageCount = pageCount;
	}

	public int getBegin() {
		return begin;
	}

	public void setBegin(int begin) {
		this.begin = begin;
	}

	public int getEnd() {
		return end;
	}

	public void setEnd(int end) {
		this.end = end;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public int getDataCount() {
		return dataCount;
	}

	public void setDataCount(int dataCount) {
		this.dataCount = dataCount;
	}

	
	
	
	
	
}
