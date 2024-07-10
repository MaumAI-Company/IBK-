package com.mindslab.web.mapper.maria;

import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.session.ResultHandler;
import org.springframework.stereotype.Repository;

import com.mindslab.web.common.support.utils.CustomMap;
import com.mindslab.web.vo.ScanVO;

@Repository
public interface MariaStatisticMapper {

    /**
     * 진위 확인 결과목록 페이징 및 검색 엑셀 다운
     * @param String userId
     * @return
     */
    public void getStatisticListExcelDown(HashMap<String, Object> paramMap, ResultHandler handler);

    /**
     * 통계 조회 날짜별
     * 
     * @param null
     * @return
     * @throws Exception
     */
    public List<CustomMap> getStatisticDate(HashMap<String, Object> paramMap);
}
