package com.mindslab.web.user.service;

import java.util.HashMap;
import java.util.List;

import com.mindslab.web.vo.LearnHistoryVO;

public interface LearnHistoryService {

    /**
     * 배포 이력 결과목록 페이징 및 검색 조회
     * @param LearnHistoryVO params
     * @return
     */
    public int getLearnHistoryTotalCount(LearnHistoryVO params);

    /**
     * 배포 이력 페이징 및 검색 조회
     * @param LearnHistoryVO params
     * @return
     */
    public List<LearnHistoryVO> getLearnHistoryList(LearnHistoryVO params);

    /**
     * 배포 이력 저장
     * @param LearnHistoryVO params
     * @return
     */
    public HashMap<String, Object> addLearnHistory(LearnHistoryVO params);

    /**
     * 배포 사용 상태 표시 변경
     * @param LearnHistoryVO params
     * @return
     */
    public HashMap<String, Object> stopLearnHistory(LearnHistoryVO params);
}
