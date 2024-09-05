package com.mindslab.web.mapper.maria;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.mindslab.web.vo.DetectionLevelHistoryVO;
import com.mindslab.web.vo.DetectionLevelVO;

@Repository
public interface MariaAdminDetectLevelMapper {

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
    public List<DetectionLevelVO> getDetectionLevelList(DetectionLevelVO params);

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
    public int setDetectionLevel(DetectionLevelVO params);
    
    /**
     * 탐지레벨 변경
     * @param String userId
     * @return
     */
    public int setDetectionLevelHistory(DetectionLevelHistoryVO params);
    
    
    
}
