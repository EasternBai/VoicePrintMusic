package com.cc.controller;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.WritableRaster;

import java.io.FileNotFoundException;
import java.io.IOException;

import javafx.application.Application;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.stage.Stage;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.videoio.VideoCapture;

import javax.swing.JFrame;  
import javax.swing.JPanel;  
import javax.swing.WindowConstants;  
  
import java.awt.*;   
import org.opencv.videoio.Videoio;  
  
public class camera extends JPanel 
{  
    private static final long serialVersionUID=1L;  
      
    public BufferedImage mImg;  
      
    public BufferedImage mat2BI(Mat mat)
    {  
        int dataSize =mat.cols()*mat.rows()*(int)mat.elemSize();  
        byte[] data=new byte[dataSize];  
        mat.get(0, 0,data);  
        int type=mat.channels()==1?  
                BufferedImage.TYPE_BYTE_GRAY:BufferedImage.TYPE_3BYTE_BGR;  
          
        if(type==BufferedImage.TYPE_3BYTE_BGR)
        {  
            for(int i=0;i<dataSize;i+=3)
            {  
                byte blue=data[i+0];  
                data[i+0]=data[i+2];  
                data[i+2]=blue;  
            }  
        }  
        BufferedImage image=new BufferedImage(mat.cols(),mat.rows(),type);  
        image.getRaster().setDataElements(0, 0, mat.cols(), mat.rows(), data);  
          
        return image;  
    }  
  
    public void paintComponent(Graphics g)
    {  
        if(mImg!=null)
        {  
            g.drawImage(mImg, 0, 0, mImg.getWidth(),mImg.getHeight(),this);  
        }  
    }  
      
//    public static void main(String[] args) {  
//        try{  
//            System.loadLibrary(Core.NATIVE_LIBRARY_NAME);  
//              
//            Mat capImg=new Mat();  
//            VideoCapture capture=new VideoCapture(0);  
//            int height = (int)capture.get(Videoio.CAP_PROP_FRAME_HEIGHT);  
//            int width = (int)capture.get(Videoio.CAP_PROP_FRAME_WIDTH);  
//            if(height==0||width==0){  
//                throw new Exception("camera not found!");  
//            }  
//              
//            JFrame frame=new JFrame("camera");  
//            frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);  
//            camera panel=new camera();  
//            frame.setContentPane(panel);  
//            frame.setVisible(true);  
//            frame.setSize(width+frame.getInsets().left+frame.getInsets().right,  
//                    height+frame.getInsets().top+frame.getInsets().bottom);  
//            while(frame.isShowing()){  
//                capture.read(capImg);  
//                panel.mImg=panel.mat2BI(capImg);  
//                panel.repaint();  
//            }  
//            capture.release();  
//            frame.dispose();  
//        }catch(Exception e){  
//            System.out.println("例外：" + e);  
//        }finally{  
//            System.out.println("--done--");  
//        }  
//  
//    }  
  
}  

