package com.mindslab.web.tests;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import javax.media.jai.JAI;
import javax.media.jai.RenderedOp;

import org.apache.commons.io.FileUtils;

import com.mindslab.web.common.error.CommonErrorCode;
import com.mindslab.web.common.error.MindsLabRestException;

public class ParseLocaleString {

    public static void main(String[] args) throws IOException {        
        //FileUtils
    	String bprImgPath = "C:/test/teee/d/d/d/d/d";
        File bprDir = new File(bprImgPath);
        System.out.println(bprDir.exists());
        if(!bprDir.exists()) {        	
        	try {
        		System.out.println(bprImgPath + "not exists");
        		System.out.println(bprImgPath + "Make Directory Start");
        		FileUtils.forceMkdir(bprDir);
        		System.out.println(bprImgPath + "Make Directory End");
			} catch (MindsLabRestException e) {
				System.out.println("BRP Download Make Directory Fail downLoad path:"+bprDir.toString() + "\n MindsLabRestException : "+e.toString());
				throw new MindsLabRestException(CommonErrorCode.INTERNAL_SERVER_ERROR);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				System.out.println("BRP Download Make Directory Fail downLoad path:"+bprDir.toString() + "\n IOException : "+e.toString());
				throw new MindsLabRestException(CommonErrorCode.INTERNAL_SERVER_ERROR);
			}
        }
    }
    
    public static synchronized void ConvertTifToPng(String path, String name) {
        try{
        	String fileName = "F:/image/test.tif";
            RenderedOp src = JAI.create("fileload", fileName);
        	File file = new File(fileName.replace(".tif", ".png"));
        	System.out.println(file.exists());
            FileOutputStream fos = new FileOutputStream(fileName.replace(".tif", ".png"));
            JAI.create("encode", src, fos, "PNG",null);
            fos.close();
        }catch (Exception e){
              System.out.println("test: " + e);
        }
    }
    
    public static synchronized void removeFile(String fileName) {
    	File file = new File(fileName);
    	System.out.println(file.toString());
    	System.out.println(file.delete());
    }    
}
