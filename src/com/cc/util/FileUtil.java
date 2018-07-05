package com.cc.util;

import java.io.File;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MultipartFile;

/**
 * 
 * @author Roy
 *
 */
public class FileUtil {
	//上传路径
	private String path="";
	
	@Autowired
	private HttpServletRequest request;
	
	public void setPath(String path) {
		this.path = path;
	}

	/**
	 * 
	 * @param fileName 文件的后缀名带小点 .jpg.png
	 * @return 新的文件名
	 */
	public String createFileName(String fileName){
		//取后缀名
		String p_n=fileName.substring(fileName.indexOf("."));
		
		//构建新文件名 =pic_当前时间毫秒值.后缀
		String newName="f_"+System.currentTimeMillis()+p_n;
		
		
		return newName;
	}
	
	public boolean uploadFile(String fileName,MultipartFile file){
		boolean res=true;
		
		try {
			//上传路径对象
			File fileDir=new File(request.getSession().getServletContext().getRealPath("/")+this.path);
			
			if(!fileDir.exists()){
				fileDir.mkdirs();
			}
			
			//上传目标文件对象
			File f =new File(fileDir,fileName);
			
			//转存文件
			file.transferTo(f);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return res;
	}
}
