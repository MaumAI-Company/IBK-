package com.mindslab.web.mapper.maria;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.mindslab.web.vo.LearnModelVO;

@Repository
public interface MariaLearnModelMapper {

    /**
     * 모델 목록 페이징 및 검색 카운트
     * @param LearnVO params
     * @return
     */
    public int getLearnModelTotalCount(LearnModelVO params);

    /**
     * 학습 목록 페이징 및 검색 조회
     * @param LearnVO params
     * @return
     */
    public List<LearnModelVO> getLearnModelList(LearnModelVO params);

    /**
     * 학습 데이터 정보 한건 조회
     * @param LearnVO params
     * @return
     */
    public LearnModelVO getLearnModelInfo(LearnModelVO params);
    
    /**
     * 배포상태변경
     * @param LearnVO params
     * @return
     */
    public int deployStatusUpdate(LearnModelVO params);

    /**
     * 배포 중인 데이터 카운트
     * @param LearnVO params
     * @return
     */
    public int getDeployingCount(LearnModelVO params);
    
}
