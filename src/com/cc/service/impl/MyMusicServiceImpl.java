package com.cc.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.cc.dao.IMyUsersDao;
import com.cc.po.Data;
import com.cc.po.MyMusic;
import com.cc.service.IMyMusicService;

@Service
public class MyMusicServiceImpl implements IMyMusicService
{
	@Autowired
	private IMyUsersDao imd;
	
	@Transactional(propagation=Propagation.NOT_SUPPORTED)
	public Data<MyMusic> findMusic(MyMusic mu) 
	{
		Data<MyMusic> data=null;					
		
		List<MyMusic> list=imd.findMusic(mu);
		if(list !=null)
		{
			data=new Data<MyMusic>();
			data.setList(list);
		}
		
		return data;
	}
}
