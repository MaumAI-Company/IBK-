package com.mindslab.web.apis;

import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.speno.apis.aConnect;
import com.speno.apis.aUsrBundle;

@Component
@RequestMapping("agentApi")
public class AgentApi {

    @SuppressWarnings({ "unchecked", "rawtypes" })
	@GetMapping("imageDown")
    @ResponseBody
    public void imageDown(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        aConnect iconn = null;
        try {
            // img_srv_ip : 서버IP
            // (개발) xxx.xxx.xxx.xxx, (운영) xxx.xxx.xxx.xxx
            // img_srv_port : 18931 (운영,개발 동일포트 사용)

            String img_srv_ip = "127.0.0.1";
            int img_srv_port = 18931;
            String elementId = "";

            iconn = new aConnect(img_srv_ip, img_srv_port, "JavaAPI", "SUPER", "SUPER");
            aUsrBundle bun = new aUsrBundle(iconn);
            // keys
            // ELEMENT_ID: 조회할 ElementID
            // USER_ID: 직원번호(비대면 및 외부에서 조회시 1234567890)
            // LOCALPATH: 다운로드 받을 경로
            // EXT: 다운로드 받을 파일 확장자
            String[] keys = { "ELEMENT_ID", "USER_ID", "LOCALPATH", "EXT" };
            // values(위의 keys에 해당하는 정보)
            String[] values = { "2019031101053300", "1234567890", "C://DOWN", "tif" };
            
            // EXT: 다운로드 받을 파일 확장자
            String[] keys1 = { "ELEMENT_ID", "USER_ID", "LOCALPATH", "EXT" };
            // values(위의 keys에 해당하는 정보)
            String[] values1 = { "2019031101053301", "1234567890", "C://DOWN", "tif" };
            
            // 위에서 설정한 조회 요청할 정보 등록
            Vector vData = bun.addINFString(null, "DOWNLOAD", aUsrBundle.GET, keys, values);
            Vector vData1 = bun.addINFString(null, "DOWNLOAD", aUsrBundle.GET, keys1, values1);
            Vector vAllData = new Vector();
            vAllData.add(vData);
            vAllData.add(vData1);
            // 조회 요청 정보 전송
            int ret = bun.sendBundleByFiles(vAllData, null);
            if (ret == 0) {
                ret = bun.startImportBundleDirect(bun.m_bundleid);
                if (ret != 0) {
                    throw new Exception(elementId + " >> get error:" + ret + ","
                            + bun.getErrorDetail() + "," + bun.getLastError());
                }
            } else {
                throw new Exception(elementId + " >> get error:" + bun.getLastError());
            }
        } catch (Exception ex) {
            throw ex;
        } finally {
            // 에이전트 연결 해제
            try {
                iconn.close();
            } catch (Exception ignored) {
            }
        }
    }

}
