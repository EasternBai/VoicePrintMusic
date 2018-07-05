package com.cc.po;

public class MyUsers {
	private Integer id;
	private String name;
	private Integer age;
	private MyDept dept;
	private String headpath;
	
	//分页专用
	private int pageIndex=0;
	private int pageSize=0;
		
	public MyDept getDept() {
		return dept;
	}
	public void setDept(MyDept dept) {
		this.dept = dept;
	}
	public int getPageIndex() {
		return pageIndex;
	}
	public void setPageIndex(int pageIndex) {
		this.pageIndex = pageIndex;
	}
	public int getPageSize() {
		return pageSize;
	}
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}
	public String getHeadpath() {
		return headpath;
	}
	public void setHeadpath(String headpath) {
		this.headpath = headpath;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Integer getAge() {
		return age;
	}
	public void setAge(Integer age) {
		this.age = age;
	}
	
}
