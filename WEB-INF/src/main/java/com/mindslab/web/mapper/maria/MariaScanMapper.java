package com.mindslab.web.mapper.maria;

import java.util.List;

import org.apache.ibatis.session.ResultHandler;
import org.springframework.stereotype.Repository;

import com.mindslab.web.vo.ScanVO;

@Repository
public interface MariaScanMapper {

    /**
     * 진위 확인 결과목록 페이징 및 검색 카운트
     * @param String userId
     * @return
     */
    public int getScanTotalCount(ScanVO params);

    /**
     * 진위 확인 결과목록 페이징 및 검색 조회
     * @param String userId
     * @return
     */
    public List<ScanVO> getScanList(ScanVO params);

    /**
     * 진위 확인 결과목록 페이징 및 검색 엑셀 다운
     * @param String userId
     * @return
     */
    public void getScanListExcelDown(ScanVO params, ResultHandler handler);

    /**
     * 진위 확인 다음 상세보기
     * @param ScanVO params
     * @return
     */
    public ScanVO getScanNextInfo(ScanVO params);

    /**
     * 진위 확인 이전 상세보기
     * @param ScanVO params
     * @return
     */
    public ScanVO getScanPrevInfo(ScanVO params);
    
}
