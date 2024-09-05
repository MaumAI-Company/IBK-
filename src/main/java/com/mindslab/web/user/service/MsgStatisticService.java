package com.mindslab.web.user.service;

import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import com.mindslab.web.common.support.utils.CustomMap;
import com.mindslab.web.vo.ScanVO;

public interface MsgStatisticService {

    /**
     * 통계 조회 날짜별 엑셀 다운로드
     * @param HashMap<String, Object> paramMap, HttpServletResponse response
     * @return
     */
    public void getStatisticExcelDown(HashMap<String, Object> paramMap, HttpServletResponse response);
    
    /**
     * HashMap<String, Object> paramMap
     * 
     * @param null
     * @return
     * @throws Exception
     */
    public List<CustomMap> getStatistic(HashMap<String, Object> paramMap);
}
