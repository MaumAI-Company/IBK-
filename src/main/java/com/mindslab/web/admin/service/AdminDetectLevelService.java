package com.mindslab.web.admin.service;

import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.mindslab.web.vo.DetectionLevelHistoryVO;
import com.mindslab.web.vo.DetectionLevelVO;

public interface AdminDetectLevelService {

    /**
     * 현재 탐지레벨 조회
     * @param String userId
     * @return
     */
    public DetectionLevelVO getDetectionLevel();
    
    /**
     * 탐지레벨 목록 조회
     * @param String userId
     * @return
     */
    public HashMap<String, Object> getDetectionLevelList(DetectionLevelVO params);

    /**
     * 탐지레벨 기록 조회
     * @param String userId
     * @return
     */
    public List<DetectionLevelHistoryVO> getDetectionLevelHistoryList(DetectionLevelHistoryVO params);

    /**
     * 탐지레벨 변경
     * @param String userId
     * @return
     */
    public HashMap<String, Object> setDetectionLevel(HashMap<String, Object> params, HttpServletRequest request);
    
    
}
