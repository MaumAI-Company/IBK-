package com.mindslab.web.mapper.maria;

import java.util.List;

import org.apache.ibatis.session.ResultHandler;
import org.springframework.stereotype.Repository;

import com.mindslab.web.vo.LearnHistoryVO;
import com.mindslab.web.vo.LearnVO;
import com.mindslab.web.vo.MsgVO;

@Repository
public interface MariaLearnHistoryMapper {

    /**
     * 배포 관리 페이징 및 검색 카운트
     * @param LearnHistoryVO params
     * @return
     */
    public int getLearnHistoryTotalCount(LearnHistoryVO params);

    /**
     * 배포 관리 목록 페이징 및 검색 조회
     * @param LearnHistoryVO params
     * @return
     */
    public List<LearnHistoryVO> getLearnHistoryList(LearnHistoryVO params);
    
    /**
     * 배포 이력 저장
     * @param LearnHistoryVO params
     * @return
     */
    public int addLearnHistory(LearnHistoryVO params);

    /**
     * 배포 사용 상태 표시 변경
     * @param LearnHistoryVO params
     * @return
     */
    public int stopLearnHistory(LearnHistoryVO params);
}
