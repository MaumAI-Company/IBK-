package com.mindslab.web.user.service;

import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import com.mindslab.web.vo.ScanVO;

public interface ScanService {

    /**
     * 진위 확인 결과목록 페이징 및 검색 조회
     * @param ScanVO params
     * @return
     */
    public int getScanTotalCount(ScanVO params);

    /**
     * 진위 확인 결과목록 페이징 및 검색 조회
     * @param ScanVO params
     * @return
     */
    public List<ScanVO> getScanList(ScanVO params);

    /**
     * 진위 확인 결과목록 페이징 및 검색 엑셀다운
     * @param ScanVO params
     * @return
     */
    public void getScanListExcelDown(ScanVO params, HttpServletResponse response);
    
    /**
     * 진위 확인 다음 상세보기
     * @param HashMap<String, Object> paramMap
     * @return
     */
    public ScanVO getScanNextInfo(HashMap<String, Object> paramMap);

    /**
     * 진위 확인 이전 상세보기
     * @param HashMap<String, Object> paramMap
     * @return
     */
    public ScanVO getScanPrevInfo(HashMap<String, Object> paramMap);
}
