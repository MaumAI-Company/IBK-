package com.mindslab.web.user.service;

import java.util.HashMap;
import java.util.List;

import com.mindslab.web.vo.LearnModelVO;

public interface LearnModelService {

    /**
     * 학습 목록 페이징 및 검색 카운트
     * @param LearnModelVO params
     * @return
     */
    public int getLearnModelTotalCount(LearnModelVO params);

    /**
     * 학습 목록 페이징 및 검색 조회
     * @param LearnModelVO params
     * @return
     */
    public List<LearnModelVO> getLearnModelList(LearnModelVO params);
    
    /**
     * 학습상태변경
     * @param LearnModelVO params
     * @return
     */
    public HashMap<String, Object> deployStatusUpdate(LearnModelVO params);
    
    /**
     * 학습 중인 데이터 카운트
     * @param LearnModelVO params
     * @return
     */
    public int getDeployingCount(LearnModelVO params);
    
    
}
