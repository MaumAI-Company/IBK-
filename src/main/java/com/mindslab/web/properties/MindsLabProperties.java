package com.mindslab.web.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import lombok.Data;

@Component
@ConfigurationProperties(prefix = "mindslab.config")
@Data
public class MindsLabProperties {
	/* mindslab system 설정 */
	private String testvalue;
	
	/* bpr 서버 아이피 */
	private String bprServerIp;
	
	/* bpr 서버 포트 */
	private int bprServerPort;
	
	/* bpr이미지를 내려 받는 절대경로 dir설정 */
	private String bprImagePath;
	
	/* 엑셀 업로드 DIR 설정*/
	private String learnExcelPath;
	
	/* mindsLab API 주소 */
	private String mindsLabApiUrl;
}
