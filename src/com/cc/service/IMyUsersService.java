package com.cc.service;


import com.cc.po.Data;
import com.cc.po.MyUsers;

public interface IMyUsersService {
	public boolean add(MyUsers mu);
	public Data<MyUsers> findAll(MyUsers mu);
}
