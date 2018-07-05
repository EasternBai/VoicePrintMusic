package com.cc.controller;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.swing.JFrame;
import javax.swing.WindowConstants;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.cc.po.MyUsers;
import com.cc.po.Data;
import com.cc.po.MyMusic;
import com.cc.service.IMyUsersService;
import com.cc.service.IMyMusicService;
import com.cc.util.FileUtil;
import com.cc.controller.camera;
import com.cc.controller.music;
import com.cc.controller.surf;
import com.cc.controller.ImagePHash;
  
import org.opencv.core.Core;  
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.videoio.VideoCapture;  
import org.opencv.videoio.Videoio; 

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import javafx.application.Application;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.stage.Stage;
import javazoom.jl.decoder.JavaLayerException;

import javax.swing.JFrame;  
import javax.swing.JPanel;  
import javax.swing.WindowConstants;  
import java.awt.*;

@Controller
@RequestMapping("/myusers")
public class MUController
{
	
	@Autowired
	private IMyUsersService ims;
	@Autowired
	private FileUtil fileUtil;
	@Autowired
	private IMyMusicService imusic;
	
	private int cameraCount = 0;
//	private int cameraCountThreshold = 200;
	private int cameraCountThreshold = 2;
	
	@RequestMapping(value="/add")
	public String add(MyUsers mu,@RequestParam("headfile") MultipartFile file){
		/**************************文件上传部分开始*******************/
		if(!file.isEmpty()){
			//取得新文件名
			String newName=fileUtil.createFileName(file.getOriginalFilename());
			
			//上传，判断在方法内部
			fileUtil.uploadFile(newName,file);
			//数据实体添加文件名称
			mu.setHeadpath(newName);
		}
		/**************************文件上传部分结束*******************/
		
		if(ims.add(mu)){
			return "redirect:/myusers/findall.do";
		}else{
			return "add";
		}	
	}
	
	private void cameraCount(JFrame frame)
	{
		if(cameraCount > cameraCountThreshold)
		{
			frame.setVisible(false);
		}
		else
		{
			cameraCount ++ ;
		}
	}
	
	private boolean matchCameraImg()
	{
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

        String templateFilePath = "F:/music/timg4.jpg";
        String originalFilePath = "F:/music/camera.jpg";
        boolean matchResult = false;
        //读取图片文件
        Mat templateImage = Imgcodecs.imread(templateFilePath, Imgcodecs.CV_LOAD_IMAGE_COLOR);
        Mat originalImage = Imgcodecs.imread(originalFilePath, Imgcodecs.CV_LOAD_IMAGE_COLOR);

        surf imageRecognition = new surf();
        matchResult = surf.matchImage(templateImage, originalImage);

        System.out.println("匹配的像素点总数：" + imageRecognition.getMatchesPointCount());
		return matchResult;
	}
	
	private String getImgHash(String imgPath)
	{
		String imgHash;
		ImagePHash p = new ImagePHash();
		
		try 
        {
			imgHash = p.getHash(new FileInputStream(new File(imgPath)));
			return imgHash;
        }
		catch (FileNotFoundException e)
        {
            e.printStackTrace();
        } 
        catch (Exception e) 
        {
            e.printStackTrace();
        }
		return null;
	}
	
	private boolean checkImgHash(String imgHash, String sourcHash, int tv)
	{
		ImagePHash p = new ImagePHash();
		
		int dt = p.distance(imgHash, sourcHash);
		if (dt <= tv)
        {
            return true;
        }
		return false;
	}
	
	private void playMusic()
	{
		String filePath = "F:/music/云烟成雨.mp3";
	    File playFile = new File(filePath);
	    music musicPlayer = new music(playFile);
	    musicPlayer.run();
	}
	
	private void playMusic(String playPath)
	{
		String filePath = playPath;
	    File playFile = new File(filePath);
	    music musicPlayer = new music(playFile);
	    musicPlayer.run();
	}
	
//	@RequestMapping(value="/findall")
//	public String findAll(Map<String,Object> m,MyUsers mu){
//		m.put("mudata", ims.findAll(mu));
//		return "index";
//	}
	
	public String findMusic(String compareImgHash, MyMusic mu)
	{
		String sourceImgHash = null;
		String musicPath = null;
		boolean matchResult = false;
		Data<MyMusic> data=null;
		data = imusic.findMusic(mu);
		List<MyMusic> list = data.getList();
		for(int index = 0; index < list.size(); index ++)
		{
			mu = list.get(index);
			sourceImgHash = mu.getHashId();
			matchResult = checkImgHash(compareImgHash, sourceImgHash, 25);
			if(matchResult)
			{
				musicPath = mu.getMusicPath();
				break;
			}
		}
		return musicPath;
//		m.put(mu.getHashId(), mu.getMusicPath());
//		m.put("mudata", imusic.findMusic(mu));
//		return "index";
	}

	@RequestMapping(value="/findall")
	public void findAll() throws FileNotFoundException, JavaLayerException
	{	
		try
		{ 
		    System.loadLibrary(Core.NATIVE_LIBRARY_NAME);  
        
		    String compareImg = "F:/music/timg4.jpg";
		    String compareImgHash = getImgHash(compareImg);
		    String musicPlayPath = null;
		    MyMusic mu = null;
		    
            Mat capImg=new Mat();  
            VideoCapture capture=new VideoCapture(0);  
            int height = (int)capture.get(Videoio.CAP_PROP_FRAME_HEIGHT);  
            int width = (int)capture.get(Videoio.CAP_PROP_FRAME_WIDTH);  
            if(height==0||width==0)
            {  
                throw new Exception("camera not found!");  
            }  
          
            JFrame frame = new JFrame("camera");  
            frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);  
            camera panel = new camera();  
            frame.setContentPane(panel);  
            frame.setVisible(true);  
            frame.setSize(width+frame.getInsets().left+frame.getInsets().right, height+frame.getInsets().top+frame.getInsets().bottom); 
            frame.setAlwaysOnTop(true);
            while(frame.isShowing())
            {  
//            	boolean matchResult = false;
                capture.read(capImg);  
//                Imgcodecs.imwrite("F:\\img\\" + ex + ".png", capImg);
                Imgcodecs.imwrite("F:/music/camera.jpg", capImg); 
                compareImg = "F:/music/camera.jpg";
                compareImgHash = getImgHash(compareImg);
                panel.mImg=panel.mat2BI(capImg);  
                panel.repaint();
                //修改算法，注释掉下面
//                matchResult = matchCameraImg();
                //修改算法，注释掉上面
                musicPlayPath = findMusic(compareImgHash, mu);
                if(musicPlayPath != null)
                {
                	frame.setVisible(false);
                }
//                cameraCount(frame);
            }  
            capture.release();  
            frame.dispose();  
            playMusic(musicPlayPath);
		}
		catch(Exception e)
		{  
            System.out.println("例外：" + e);  
        }
		finally
		{  
            System.out.println("--done--");  
        }
		
//        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
//
//        String templateFilePath = "F:/music/timg1.jpg";
//        String originalFilePath = "F:/music/timg2.jpg";
//        boolean matchResult = false;
//        //读取图片文件
//        Mat templateImage = Imgcodecs.imread(templateFilePath, Imgcodecs.CV_LOAD_IMAGE_COLOR);
//        Mat originalImage = Imgcodecs.imread(originalFilePath, Imgcodecs.CV_LOAD_IMAGE_COLOR);
//
//        surf imageRecognition = new surf();
//        matchResult = surf.matchImage(templateImage, originalImage);
//
//        System.out.println("匹配的像素点总数：" + imageRecognition.getMatchesPointCount());
		
//		if(matchResult)
//		{	
//            String filePath = "F:/music/云烟成雨.mp3";
//		    File playFile = new File(filePath);
//		    music musicPlayer = new music(playFile);
//		    musicPlayer.run();
//		}
	}

}
