package com.cc.service;

import com.cc.po.Data;
import com.cc.po.MyMusic;

public interface IMyMusicService 
{
	public Data<MyMusic> findMusic(MyMusic mu);
}
