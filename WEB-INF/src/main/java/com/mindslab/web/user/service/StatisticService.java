package com.mindslab.web.user.service;

import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import com.mindslab.web.common.support.utils.CustomMap;
import com.mindslab.web.vo.ScanVO;

public interface StatisticService {

    /**
     * 진위 확인 결과목록 페이징 및 검색 엑셀 다운
     * @param String userId
     * @return
     */
    public void getStatisticListExcelDown(HashMap<String, Object> paramMap, HttpServletResponse response);
    
    /**
     * 통계 조회
     * 
     * @param null
     * @return
     * @throws Exception
     */
    public List<CustomMap> getStatisticList(HashMap<String, Object> paramMap);
}
