package com.mindslab.web.user.service;

import java.util.HashMap;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.mindslab.web.vo.LearnVO;

public interface LearnService {

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
     * 학습파일 업로드
     * @param MultipartFile file, MemberVO memberVO
     * @return
     */
    public HashMap<String, Object> learnDatafileupload(MultipartFile file, LearnVO memberVO);

    /**
     * 정답셋파일 업로드
     * @param MultipartFile file, MemberVO memberVO
     * @return
     */
    public HashMap<String, Object> answerDatafileupload(MultipartFile file, LearnVO learnVO);
    
    /**
     * 학습상태변경
     * @param LearnVO params
     * @return
     */
    public HashMap<String, Object> learningStatusUpdate(LearnVO params);
    
    /**
     * 학습 중인 데이터 카운트
     * @param LearnVO params
     * @return
     */
    public int getLearningCount(LearnVO params);
    

    /**
     * 학습 데이터 삭제
     * @param LearnVO params
     * @return
     */
    public HashMap<String, Object> deleteLearn(LearnVO params);
    
}
