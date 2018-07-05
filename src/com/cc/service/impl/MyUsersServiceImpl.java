package com.cc.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.cc.dao.IMyUsersDao;
import com.cc.po.Data;
import com.cc.po.MyUsers;
import com.cc.po.Page;
import com.cc.service.IMyUsersService;

@Service
public class MyUsersServiceImpl implements IMyUsersService {
	
	@Autowired
	private IMyUsersDao imd;
	
	@Override
	@Transactional
	public boolean add(MyUsers mu) {
		boolean res=false;
		int i=imd.add(mu);
		if(i>0){
			res=true;
		}
		
		return res;
	}

	@Override
	@Transactional(propagation=Propagation.NOT_SUPPORTED)
	public Data<MyUsers> findAll(MyUsers mu) {
		Data<MyUsers> data=null;		
		
		int count=imd.findCount(mu);				
		
		Page page =new Page(mu.getPageIndex(),mu.getPageSize(),count);
		
		List<MyUsers> list=imd.findAll(page);
		if(list !=null){
			data=new Data<MyUsers>();
			data.setList(list);
			data.setPage(page);
		}
		
		return data;
	}

}
