package com.cc.dao;

import java.util.List;

import com.cc.po.MyUsers;
import com.cc.po.Page;
import com.cc.po.MyMusic;

public interface IMyUsersDao {
	/***
	 * 新增
	 * @param mu 实体
	 * @return 是否新增成功
	 */
	public int add(MyUsers mu);
	/***
	 * 查询全部
	 * @return
	 */
	public List<MyUsers> findAll(Page page);
	/***
	 * 查询总数
	 * @param mu
	 * @return
	 */
	public int findCount(MyUsers mu);
	
	/***
	 * 查询全部音乐
	 * @return
	 */
	public List<MyMusic> findMusic(MyMusic mu);
}
