package com.mindslab.web.mapper.maria;

import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.session.ResultHandler;
import org.springframework.stereotype.Repository;

import com.mindslab.web.common.support.utils.CustomMap;
import com.mindslab.web.vo.ScanVO;

@Repository
public interface MariaMsgMatchStatisticMapper {

    /**
     * 통계 조회 날짜별 엑셀 다운로드
     * @param HashMap<String, Object> paramMap, ResultHandler handler
     * @return
     */
    public void getStatisticDateExcelDown(HashMap<String, Object> paramMap, ResultHandler handler);

    /**
     * 기간 조건 조회
     * 
     * @param HashMap<String, Object> paramMap
     * @return
     * @throws Exception
     */
    public List<CustomMap> getStatisticDate(HashMap<String, Object> paramMap);
    

    /**
     * 부점 조건 엑셀 다운로드
     * @param HashMap<String, Object> paramMap, ResultHandler handler
     * @return
     */
    public void getStatisticBrExcelDown(HashMap<String, Object> paramMap, ResultHandler handler);

    /**
     * 부점 조건 조회
     * 
     * @param HashMap<String, Object> paramMap
     * @return
     * @throws Exception
     */
    public List<CustomMap> getStatisticBr(HashMap<String, Object> paramMap);

    /**
     * 검증 조건 엑셀 다운로드
     * @param HashMap<String, Object> paramMap, ResultHandler handler
     * @return
     */
    public void getStatisticResultExcelDown(HashMap<String, Object> paramMap, ResultHandler handler);
    
    /**
     * 검증 목적  조회
     * 
     * @param HashMap<String, Object> paramMap
     * @return
     * @throws Exception
     */
    public List<CustomMap> getStatisticResult(HashMap<String, Object> paramMap);
    
    /**
     * 부점 조회
     * 
     * @param HashMap<String, Object> paramMap
     * @return
     * @throws Exception
     */
    public List<CustomMap> getStatisticBrResult(HashMap<String, Object> paramMap);
    /**
     * 부점 엑셀 다운로드
     * @param HashMap<String, Object> paramMap, ResultHandler handler
     * @return
     */
    public void getStatisticBrResultExcelDown(HashMap<String, Object> paramMap, ResultHandler handler);
    
}
