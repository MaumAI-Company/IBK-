package com.mindslab.web.mapper.maria;

import java.util.List;

import org.apache.ibatis.session.ResultHandler;
import org.springframework.stereotype.Repository;

import com.mindslab.web.vo.MsgVO;

@Repository
public interface MariaMsgSsoMapper {

    /**
     * 사전 검증 결과목록 페이징 및 검색 카운트
     * @param String userId
     * @return
     */
    public int getMsgTotalCount(MsgVO params);

    /**
     * 사전 검증 결과목록 페이징 및 검색 조회
     * @param String userId
     * @return
     */
    public List<MsgVO> getMsgList(MsgVO params);

    /**
     * 사전 검증 결과목록 페이징 및 검색 엑셀 다운
     * @param String userId
     * @return
     */
    public void getMsgListExcelDown(MsgVO params, ResultHandler handler);

    /**
     * 사전 검증 다음 상세보기
     * @param MsgVO params
     * @return
     */
    public MsgVO getMsgNextInfo(MsgVO params);

    /**
     * 사전 검증 이전 상세보기
     * @param MsgVO params
     * @return
     */
    public MsgVO getMsgPrevInfo(MsgVO params);
    
}
