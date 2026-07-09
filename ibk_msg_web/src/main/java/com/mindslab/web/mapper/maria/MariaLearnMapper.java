package com.mindslab.web.mapper.maria;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.mindslab.web.vo.LearnVO;

@Repository
public interface MariaLearnMapper {

    /**
     * 학습 목록 페이징 및 검색 카운트
     * @param LearnVO params
     * @return
     */
    public int getLearnTotalCount(LearnVO params);

    /**
     * 학습 목록 페이징 및 검색 조회
     * @param LearnVO params
     * @return
     */
    public List<LearnVO> getLearnList(LearnVO params);

    /**
     * 학습 데이터 정보 한건 조회
     * @param LearnVO params
     * @return
     */
    public LearnVO getLearnInfo(LearnVO params);
    
    /**
     * 학습파일 업로드
     * @param LearnVO params
     * @return
     */
    public int learnDatafileupload(LearnVO params);
    
    /**
     * 정답셋파일 업로드 및 학습 실행데이터 저장
     * @param LearnVO params
     * @return
     */
    public int answerDatafileupload(LearnVO params);
    
    /**
     * 학습상태변경
     * @param LearnVO params
     * @return
     */
    public int learningStatusUpdate(LearnVO params);

    /**
     * 학습 중인 데이터 카운트
     * @param LearnVO params
     * @return
     */
    public int getLearningCount(LearnVO params);
    
    /**
     * 중복된  학습명 확인
     * @param LearnVO params
     * @return
     */
    public int getLearnNameCount(String learnName);
    
    /**
     * 학습상태변경
     * @param LearnVO params
     * @return
     */
    public int deleteLearn(LearnVO params);
    
}
