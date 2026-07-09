package com.mindslab.web.sample;


//import com.windfire.apis.asys.asysUsrElement;
//import com.windfire.apis.*;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
	/*
public class DownloadContent{
	//public asysConnectData con = null; 
	public DownloadContent(){
		//con = new asysConnectData("개발서버IP", 2102, "OCR서버 Hostname", "SUPER", "SUPER");
	}
	public static void main(String[] args){
		DownloadContent dc = new DownloadContent();
		dc.download("XVARM_MAIN::2021071915044900::IMAGE", "C:\\Users\\user\\Desktop\\my\\war\\des\\2021071915044900.pdf");
		dc.download("XVARM_MAIN::2021071915044901::IMAGE", "C:\\Users\\user\\Desktop\\my\\war\\des\\2021071915044901.tif");
//		dc.download("XVARM_MAIN::2021071515352307::IMAGE", "C:\\Users\\user\\Desktop\\my\\war\\des\\2021071515352307.jpg");
//		dc.download("XVARM_MAIN::2021070810591900::IMAGE", "C:\\Users\\user\\Desktop\\my\\war\\des\\2021070810591900.bmp");
//		dc.download("XVARM_MAIN::2021071914345600::IMAGE", "C:\\Users\\user\\Desktop\\my\\war\\des\\2021071914345600.png");
		//dc.downloadStream();
		//dc.getContentKey();
		//dc.downloadListener();
		dc.discon();
	}	
	public void download(String id, String path){
		asysUsrElement uePage1 = new asysUsrElement(con);
		uePage1.m_elementId=id;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss,SSS");
		System.out.println(sdf.format(new Date()));
		int ret = uePage1.getContent(path,"","");
		System.out.println(sdf.format(new Date()));
		if (ret != 0)
			System.out.println("Error, download normal, " + uePage1.m_lastError);
		else
			System.out.println("Success, download normal, " + uePage1.m_elementId);
    	}

    	public void downloadStream(){
		int rCode = -1;
		asysUsrElement ue = new asysUsrElement(con);
		// String descr, String userSClass, String eClassId, String file	
		ue.m_elementId = "XVARM_MAIN::200901201539092::IMAGE";
		ByteArrayOutputStream fileOut = new ByteArrayOutputStream();
		rCode = ue.getContent(fileOut, "", "");
		if (rCode != 0){
			System.out.println("Error, downloadStream, " + ue.getLastError());
		}else{
			try{
				byte[] tmp = fileOut.toByteArray();
				FileOutputStream fos = new FileOutputStream(new File("D:/200901201539092_1.tif"));
				fos.write(tmp);
				fos.close();
			}
			catch(IOException ioe){
				ioe.printStackTrace();
			}
			System.out.println("SUCCESS, downloadStream");
		}
		
		try{
			fileOut.close();
		}
		catch(IOException ioe){
			ioe.printStackTrace();
		}	
    	}

    	public void downloadListener(){
		asysUsrElement uePage1 = new asysUsrElement(con);
		uePage1.m_elementId="XVARM_MAIN::200901201539092::IMAGE";
		uePage1.setDownloadListener(new MyDownloadListener());
		int ret = uePage1.getContent("D:/asfdasf/200901201539092.txt");
		if (ret != 0)
			System.out.println("Error : " + uePage1.m_lastError);
		else
			System.out.println("Success : " + uePage1.m_contentKey);
    	}
    	
	public void discon(){
		// has to be called after all transaction;
		if(con != null){
			con.close();
			con = null;
		}
	}
}
*/