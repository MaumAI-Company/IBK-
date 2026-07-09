package com.mindslab.web.user.service;

import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import com.mindslab.web.vo.MsgVO;

public interface MsgService {

    /**
     * 진위 확인 결과목록 페이징 및 검색 조회
     * @param MsgVO params
     * @return
     */
    public int getMsgTotalCount(MsgVO params);

    /**
     * 진위 확인 결과목록 페이징 및 검색 조회
     * @param MsgVO params
     * @return
     */
    public List<MsgVO> getMsgList(MsgVO params);

    /**
     * 진위 확인 결과목록 페이징 및 검색 엑셀다운
     * @param MsgVO params
     * @return
     */
    public void getMsgListExcelDown(MsgVO params, HttpServletResponse response);
    
    /**
     * 진위 확인 다음 상세보기
     * @param HashMap<String, Object> paramMap
     * @return
     */
    public MsgVO getMsgNextInfo(HashMap<String, Object> paramMap);

    /**
     * 진위 확인 이전 상세보기
     * @param HashMap<String, Object> paramMap
     * @return
     */
    public MsgVO getMsgPrevInfo(HashMap<String, Object> paramMap);
}
