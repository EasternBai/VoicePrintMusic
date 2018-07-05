package com.cc.po;

public class MyMusic 
{
	private int id;
	private String imageHashId;
	private String musicPath;
	
	public String getHashId() 
	{
		return imageHashId;
	}
	
	public void setHashId(String hashId) 
	{
		this.imageHashId = hashId;
	}
	
	public String getMusicPath() 
	{
		return musicPath;
	}
	
	public void setMusicPath(String musicPath) 
	{
		this.musicPath = musicPath;
	}
	
	public int getId()
	{
		return id;
	}
}
