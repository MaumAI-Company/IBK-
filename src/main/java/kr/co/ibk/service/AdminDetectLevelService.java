package kr.co.ibk.service;

import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.co.ibk.domain.web.DetectionLevel;
import kr.co.ibk.domain.web.DetectionLevelHistory;
import kr.co.ibk.repository.AdminDetectLevelRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AdminDetectLevelService extends _BaseService{
	
	private final AdminDetectLevelRepository adminDetectLevelRepository;
	
	public HashMap<String, Object> getDetectionLevelList(DetectionLevel params) {
		HashMap<String, Object> result = new HashMap<String, Object>();
		
		List<DetectionLevel> detectionLevelList = adminDetectLevelRepository.getDetectionLevelList(params);
		String status = "on";// 꺼진상태: off  : 켜진상태 :on
		
		for (DetectionLevel vo : detectionLevelList ) {
			if ("0".equals(vo.getLevel()) && "0".equals(vo.getUseStatus())) {// 끔 상태
				status = "off";
			}
		}
		
		
		result.put("detectionLevelList", detectionLevelList); 
		result.put("status", status);
		
		return result;
	}

	
	public List<DetectionLevelHistory> getDetectionLevelHistoryList(DetectionLevelHistory params) {
		return adminDetectLevelRepository.getDetectionLevelHistoryList(params);
	}

	
	public HashMap<String, Object> setDetectionLevel(HashMap<String, Object> params, HttpServletRequest request) {
		// request parameter
		String prevLevel = params.get("prevLevel") == null ? "0.1": (String) params.get("prevLevel") ;
		String changeLevel = params.get("changeLevel") == null ? "0.1": (String) params.get("changeLevel") ;
		String sessionMemId = params.get("sessionMemId") == null ? "": (String) params.get("sessionMemId") ;

		// value init
		HashMap<String, Object> result = new HashMap<String, Object>();
		DetectionLevel DetectionLevel = new DetectionLevel();
		DetectionLevelHistory DetectionLevelHistory = new DetectionLevelHistory();
		int modCnt = 0;
		int addCnt = 0;
		String clientIP = getClientIP(request);
		
		// 목록 조회 후
		// 이전 데이터의 status는 1(미사용)로 변경 데이터의 status는 0(사용)으로 변경한다
		List<DetectionLevel> detectionLevelList = adminDetectLevelRepository.getDetectionLevelList(DetectionLevel);
		
		for (DetectionLevel vo : detectionLevelList ) {
			if(prevLevel.equals(vo.getLevelStrng())) {
				// 이전 상태 update
				DetectionLevel = new DetectionLevel();
				DetectionLevel.setUseStatus("1"); 
				DetectionLevel.setUpdMemId(sessionMemId);
				DetectionLevel.setSeq(vo.getSeq());
				modCnt += adminDetectLevelRepository.setDetectionLevel(DetectionLevel);
				
				// 이력
				DetectionLevelHistory.setPrevLevel(vo.getLevel());//이전 탐지레벨 
				DetectionLevelHistory.setPrevLevelStrng(vo.getLevelStrng());//이전 탐지레벨 강도
				DetectionLevelHistory.setPrevLevelDescription(vo.getLevelDescription());//이전 레벨 설명
			} else if(changeLevel.equals(vo.getLevelStrng())) {
				// 변경 상태 update
				DetectionLevel = new DetectionLevel();
				DetectionLevel.setUseStatus("0"); 
				DetectionLevel.setUpdMemId(sessionMemId);
				DetectionLevel.setSeq(vo.getSeq());
				modCnt += adminDetectLevelRepository.setDetectionLevel(DetectionLevel);
				
				// 이력
				DetectionLevelHistory.setLevel(vo.getLevel());//탐지레벨 
				DetectionLevelHistory.setLevelStrng(vo.getLevelStrng());//탐지레벨 강도
				DetectionLevelHistory.setLevelDescription(vo.getLevelDescription());//레벨 설명
			}
		}
		// 클라이언트 아이피 SET > 로그 INSERT
		DetectionLevelHistory.setUpdClientIp(clientIP);
		DetectionLevelHistory.setLastModMemId(sessionMemId);
		if ("0".equals(DetectionLevelHistory.getPrevLevel())){
			DetectionLevelHistory.setLevelStrng("켬");
		} 
		
		
		addCnt += adminDetectLevelRepository.setDetectionLevelHistory(DetectionLevelHistory);

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
