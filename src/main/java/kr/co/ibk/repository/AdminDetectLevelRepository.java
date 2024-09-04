package kr.co.ibk.repository;

import java.util.List;

import org.springframework.stereotype.Repository;

import kr.co.ibk.domain.web.DetectionLevel;
import kr.co.ibk.domain.web.DetectionLevelHistory;

@Repository
public interface AdminDetectLevelRepository {
    /**
     * 현재 탐지레벨 조회
     * @param String userId
     * @return
     */
    public DetectionLevel getDetectionLevel();

    /**
     * 탐지레벨 목록 조회
     * @param String userId
     * @return
     */
    public List<DetectionLevel> getDetectionLevelList(DetectionLevel params);

    /**
     * 탐지레벨 기록 조회
     * @param String userId
     * @return
     */
    public List<DetectionLevelHistory> getDetectionLevelHistoryList(DetectionLevelHistory params);

    /**
     * 탐지레벨 변경
     * @param String userId
     * @return
     */
    public int setDetectionLevel(DetectionLevel params);
    
    /**
     * 탐지레벨 변경
     * @param String userId
     * @return
     */
    public int setDetectionLevelHistory(DetectionLevelHistory params);
}
