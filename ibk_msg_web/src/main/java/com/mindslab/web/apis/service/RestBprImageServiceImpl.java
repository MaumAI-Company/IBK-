package com.mindslab.web.apis.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import javax.media.jai.JAI;
import javax.media.jai.RenderedOp;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mindslab.web.common.error.CommonErrorCode;
import com.mindslab.web.common.error.MindsLabRestException;
import com.mindslab.web.common.support.utils.CustomMap;
import com.mindslab.web.common.support.utils.StringUtil;
import com.mindslab.web.properties.MindsLabProperties;
import com.mindslab.web.properties.SystemProperties;
import com.speno.apis.aConnect;
import com.speno.apis.aUsrBundle;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class RestBprImageServiceImpl  implements RestBprImageService{
	
	@Autowired
	protected MindsLabProperties mindsLabProperties;	

    @Autowired
    protected SystemProperties systemProperties;
	
	/**
	 * BPR 서버에서 이미지를 서버에 내려받는 API
	 * @param server
	 * @return 
	 */
	@Override
	public CustomMap getBprImagePath(Map<String, Object> reqParam) {
		aConnect iconn = null;
		CustomMap result = new CustomMap();
        // img_srv_ip : 서버IP
        // (개발) 172.18.121.145, (운영) xxx.xxx.xxx.xxx
        // img_srv_port : 18931 (운영,개발 동일포트 사용)

        String vImgSrvIp = mindsLabProperties.getBprServerIp();
        int vImgSrvPort = mindsLabProperties.getBprServerPort();
        String elementId = "";
        elementId = (String) reqParam.get("elementId");
        String bprImgPath = mindsLabProperties.getBprImagePath();        
        
        log.info("RestBprImageService.getBprImagePath : vImgSrvIp: {}, vImgSrvPort: {}, elementId: {}, bprImgPath: {}",vImgSrvIp,vImgSrvPort,elementId,bprImgPath);
        
		try {
			if(systemProperties.isLocal()) {
				result.orginPut("ret", 0);
				Vector avData = new Vector();
				Vector vdata = new Vector();
				String opcode = "";
				String command = aUsrBundle.GET;
				String[] names = { "ELEMENT_ID", "USER_ID", "LOCALPATH", "EXT" };
				String[] values = { elementId, "1234567890", "F:/image/2019031101053300", "png" };
			    vdata.add(opcode);
			    vdata.add(command);
			    vdata.add(names);
			    vdata.add(values);			    
			    avData.add(vdata);				
		        result.orginPut("vData", avData);
		        result.orginPut("bprImgFileId","2019031101053300");
		        result.orginPut("imgUrl", "/bprImg/2019031101053300.png");
			}else {
				iconn = getAsysConnectData(vImgSrvIp, vImgSrvPort, "ifdusr", "SUPER", "SUPER");
				result = doAsysUsrElement(iconn, elementId, bprImgPath);
			}
		} catch (MindsLabRestException e) {
			// TODO: handle exception
			throw new MindsLabRestException(e);
		} catch (Exception e) {
			throw new MindsLabRestException(CommonErrorCode.INTERNAL_SERVER_ERROR);
		} finally {
			// TODO: handle finally clause
			if(iconn!= null) {iconn.close();}
		}
		return result;
	}

	
	/**
	 * BRP API aConnect 생성
	 * @param server
	 * @param port
	 * @param client
	 * @param user
	 * @param pswd
	 * @return
	 */
	public aConnect getAsysConnectData(String server, int port, String client, String user, String pswd) {
		aConnect iconn = new aConnect(server, port, client, user, pswd);
		return iconn;
	}
	
	/**
	 * BRP API 이미지 다운로드 처리
	 * @param conn
	 * @param elementId
	 * @param bprImgPath
	 * @return
	 * @throws MindsLabRestException
	 */
	public CustomMap doAsysUsrElement(aConnect conn, String elementId, String bprImgPath) throws MindsLabRestException{
		
        aUsrBundle bun = new aUsrBundle(conn);
        CustomMap result = new CustomMap();
        //중복되지 않는 파일명으로 파일디렉토리를 구한다.
        //String brpImgFileId = getExistFileName(bprImgPath);
        
        log.info("doAsysUsrElement() : elementId : {}, bprImgPath : {}",elementId,bprImgPath); 
        
        // keys
        // ELEMENT_ID: 조회할 ElementID
        // USER_ID: 직원번호(비대면 및 외부에서 조회시 1234567890)
        // LOCALPATH: 다운로드 받을 경로
        // EXT: 다운로드 받을 파일 확장자
        String[] keys = { "ELEMENT_ID", "USER_ID", "LOCALPATH", "EXT" };
        // values(위의 keys에 해당하는 정보)
        String bprPath = bprImgPath;
        String[] values = { elementId, "1234567890", bprPath, "png" };       
        
        File bprDir = new File(bprImgPath);
        if(!bprDir.exists()) {        	
        	try {
        		log.info(bprImgPath + "not exists");
        		log.info(bprImgPath + "Make Directory Start");
        		FileUtils.forceMkdir(bprDir);
        		log.info(bprImgPath + "Make Directory End");
			} catch (MindsLabRestException e) {
				log.error("BRP Download Make Directory Fail downLoad path:"+bprDir.toString() + "\n MindsLabRestException : "+e.toString());
				throw new MindsLabRestException(CommonErrorCode.INTERNAL_SERVER_ERROR);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				log.error("BRP Download Make Directory Fail downLoad path:"+bprDir.toString() + "\n IOException : "+e.toString());
				throw new MindsLabRestException(CommonErrorCode.INTERNAL_SERVER_ERROR);
			}
        }
        
        // 위에서 설정한 조회 요청할 정보 등록
        log.info("aUsrBundle.addINFString() start");
        Vector vData = bun.addINFString(null, "DOWNLOAD", aUsrBundle.GET, keys, values);
        log.info("aUsrBundle.addINFString() End");
        // 조회 요청 정보 전송
        int ret = 999; 
        log.info("aUsrBundle.sendBundleByFiles() start");
        
        ret = bun.sendBundleByFiles(vData, null);
        if (ret == 0) {
            ret = bun.startImportBundleDirect(bun.m_bundleid);
            if (ret != 0) {
                throw new MindsLabRestException(CommonErrorCode.INTERNAL_SERVER_ERROR,elementId + " >> get error:" + ret + ","
                        + bun.getErrorDetail() + "," + bun.getLastError());
            }else {
            	//ConvertTifToPng(bprPath,elementId+".tif");
            }
        } else {
            throw new MindsLabRestException(CommonErrorCode.INTERNAL_SERVER_ERROR,elementId + " >> get error:" + bun.getLastError());
        }
        log.info("aUsrBundle.sendBundleByFiles() End");
                
        String bprImgFileId = elementId+".png";
        result.orginPut("ret", ret);
        result.orginPut("vData", vData);
        result.orginPut("bprImgFileId",elementId);
        result.orginPut("imgUrl", "/bprImg/"+bprImgFileId);
        
        log.info("doAsysUsrElement() ret:{}, bprImgFileId:{}, imgUrl:{}", ret, elementId, bprImgFileId);
        
		return result;
	}
	
	/**
	 * 임의의 파일명 생성
	 * @param bprImgPath
	 * @return
	 */
	public String getExistFileName(String bprImgPath) {
        String fileId;
        File destLoc;
        int checkCnt = 0;
        do {
        	checkCnt++;
        	fileId = RandomStringUtils.randomAlphanumeric(10);
        	//fileNm = RandomStringUtils.randomNumeric(1);
        	log.info("getExistFileName while is file Name : {}", fileId);
        	destLoc = new File(bprImgPath+"/"+fileId+".tif");
        	if(checkCnt > 10) {
        		fileId = "9999999999999999999999999";
        		System.out.println("checkCnt :: " + checkCnt);
        		break;
        	}
        }while(destLoc.exists());
        
        return fileId;
	}
	
    public static void ConvertTifToPng(String path, String name) throws MindsLabRestException{
    	log.info("ConvertTifToPng() path: {}, name: {}", path, name);
    	String convertFileName = path+"/"+name;
        RenderedOp src = JAI.create("fileload", convertFileName);
        try{
            FileOutputStream fos = new FileOutputStream(convertFileName.replace(".tif", ".png"));
            JAI.create("encode", src, fos, "PNG",null);
            fos.close();
        }catch (Exception e){
              log.error("getBprImagePath.ConvertTifToPng() : " + e.getMessage());
              throw new MindsLabRestException(CommonErrorCode.INTERNAL_SERVER_ERROR,path + " >> Convert get error File:" + name);
        }
    }	
	
}
