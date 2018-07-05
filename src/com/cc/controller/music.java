package com.cc.controller;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.*;

public class music extends Thread
{
    Player player;
    File music;
    //构造方法  参数是一个.mp3音频文件
    public music(File file) 
    {
        this.music = file;
    }
    
    //重写run方法
    @Override
    public void run() 
    {
        super.run();
        try 
        {
            play();     
        } catch (FileNotFoundException | JavaLayerException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    
    //播放方法
    public void play() throws FileNotFoundException, JavaLayerException 
    {
        BufferedInputStream buffer = new BufferedInputStream(new FileInputStream(music));
        player = new Player(buffer);
        player.play();
    }
}
