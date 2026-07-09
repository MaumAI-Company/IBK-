package com.mindslab.web.admin.service;

import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mindslab.web.mapper.maria.MariaAdminDetectLevelMapper;
import com.mindslab.web.vo.DetectionLevelHistoryVO;
import com.mindslab.web.vo.DetectionLevelVO;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class AdminDetectLevelServiceImpl implements AdminDetectLevelService {
	
	@Autowired
	private MariaAdminDetectLevelMapper mariaAdminDetectLevelMapper;

    @Override
    public DetectionLevelVO getDetectionLevel() {
        return mariaAdminDetectLevelMapper.getDetectionLevel();
    }
    
	@Override
	public HashMap<String, Object> getDetectionLevelList(DetectionLevelVO params) {
		HashMap<String, Object> result = new HashMap<String, Object>();
		
		List<DetectionLevelVO> detectionLevelList = mariaAdminDetectLevelMapper.getDetectionLevelList(params);
		String status = "on";// 꺼진상태: off  : 켜진상태 :on
		
		for (DetectionLevelVO vo : detectionLevelList ) {
			if ("0".equals(vo.getLevel()) && "0".equals(vo.getUseStatus())) {// 끔 상태
				status = "off";
			}
		}
		
		
		result.put("detectionLevelList", detectionLevelList); 
		result.put("status", status);
		
		return result;
	}

	@Override
	public List<DetectionLevelHistoryVO> getDetectionLevelHistoryList(DetectionLevelHistoryVO params) {
		return mariaAdminDetectLevelMapper.getDetectionLevelHistoryList(params);
	}

	@Override
	public HashMap<String, Object> setDetectionLevel(HashMap<String, Object> params, HttpServletRequest request) {
		// request parameter
		String prevLevel = params.get("prevLevel") == null ? "0.1": (String) params.get("prevLevel") ;
		String changeLevel = params.get("changeLevel") == null ? "0.1": (String) params.get("changeLevel") ;
		String sessionMemId = params.get("sessionMemId") == null ? "": (String) params.get("sessionMemId") ;

		// value init
		HashMap<String, Object> result = new HashMap<String, Object>();
		DetectionLevelVO detectionLevelVO = new DetectionLevelVO();
		DetectionLevelHistoryVO detectionLevelHistoryVO = new DetectionLevelHistoryVO();
		int modCnt = 0;
		int addCnt = 0;
		String clientIP = getClientIP(request);
		
		// 목록 조회 후
		// 이전 데이터의 status는 1(미사용)로 변경 데이터의 status는 0(사용)으로 변경한다
		List<DetectionLevelVO> detectionLevelList = mariaAdminDetectLevelMapper.getDetectionLevelList(detectionLevelVO);
		
		for (DetectionLevelVO vo : detectionLevelList ) {
			if(prevLevel.equals(vo.getLevelStrng())) {
				// 이전 상태 update
				detectionLevelVO = new DetectionLevelVO();
				detectionLevelVO.setUseStatus("1"); 
				detectionLevelVO.setUpdMemId(sessionMemId);
				detectionLevelVO.setSeq(vo.getSeq());
				modCnt += mariaAdminDetectLevelMapper.setDetectionLevel(detectionLevelVO);
				
				// 이력
				detectionLevelHistoryVO.setPrevLevel(vo.getLevel());//이전 탐지레벨 
				detectionLevelHistoryVO.setPrevLevelStrng(vo.getLevelStrng());//이전 탐지레벨 강도
				detectionLevelHistoryVO.setPrevLevelDescription(vo.getLevelDescription());//이전 레벨 설명
			} else if(changeLevel.equals(vo.getLevelStrng())) {
				// 변경 상태 update
				detectionLevelVO = new DetectionLevelVO();
				detectionLevelVO.setUseStatus("0"); 
				detectionLevelVO.setUpdMemId(sessionMemId);
				detectionLevelVO.setSeq(vo.getSeq());
				modCnt += mariaAdminDetectLevelMapper.setDetectionLevel(detectionLevelVO);
				
				// 이력
				detectionLevelHistoryVO.setLevel(vo.getLevel());//탐지레벨 
				detectionLevelHistoryVO.setLevelStrng(vo.getLevelStrng());//탐지레벨 강도
				detectionLevelHistoryVO.setLevelDescription(vo.getLevelDescription());//레벨 설명
			}
		}
		// 클라이언트 아이피 SET > 로그 INSERT
		detectionLevelHistoryVO.setUpdClientIp(clientIP);
		detectionLevelHistoryVO.setLastModMemId(sessionMemId);
		if ("0".equals(detectionLevelHistoryVO.getPrevLevel())){
			detectionLevelHistoryVO.setLevelStrng("켬");
		} 
		
		
		addCnt += mariaAdminDetectLevelMapper.setDetectionLevelHistory(detectionLevelHistoryVO);

		result.put("modCnt", modCnt);
		result.put("addCnt", addCnt);
		return result;
	}
	
	public String getClientIP(HttpServletRequest request) {
	    String ip = request.getHeader("X-Forwarded-For");
	    log.info("> X-FORWARDED-FOR : " + ip);

	    if (ip == null) {
	        ip = request.getHeader("Proxy-Client-IP");
	        log.info("> Proxy-Client-IP : " + ip);
	    }
	    if (ip == null) {
	        ip = request.getHeader("WL-Proxy-Client-IP");
	        log.info(">  WL-Proxy-Client-IP : " + ip);
	    }
	    if (ip == null) {
	        ip = request.getHeader("HTTP_CLIENT_IP");
	        log.info("> HTTP_CLIENT_IP : " + ip);
	    }
	    if (ip == null) {
	        ip = request.getHeader("HTTP_X_FORWARDED_FOR");
	        log.info("> HTTP_X_FORWARDED_FOR : " + ip);
	    }
	    if (ip == null) {
	        ip = request.getRemoteAddr();
	        log.info("> getRemoteAddr : "+ip);
	    }
	    log.info("> Result : IP Address : "+ip);

	    return ip;
	}
}
