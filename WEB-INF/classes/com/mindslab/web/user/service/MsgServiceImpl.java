package com.mindslab.web.user.service;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mindslab.web.common.support.utils.excel.ExcelHandler;
import com.mindslab.web.common.support.utils.excel.MsgExcelDown;
import com.mindslab.web.mapper.maria.MariaMsgMapper;
import com.mindslab.web.paging.PaginationInfo;
import com.mindslab.web.vo.MsgVO;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class MsgServiceImpl implements MsgService {
	
	@Autowired
	private MariaMsgMapper mariaMsgMapper;

	@Override
	public int getMsgTotalCount(MsgVO params) {
		return mariaMsgMapper.getMsgTotalCount(params);
	}

	@Override
	public List<MsgVO> getMsgList(MsgVO params) {
		List<MsgVO> msgList = Collections.emptyList();
		
		int msgTotalCount = mariaMsgMapper.getMsgTotalCount(params);
		
		PaginationInfo paginationInfo = new PaginationInfo(params);
		paginationInfo.setTotalRecordCount(msgTotalCount);
		
		params.setPaginationInfo(paginationInfo);
		
		if (msgTotalCount > 0) {
			msgList = mariaMsgMapper.getMsgList(params);
		}
			
		return msgList;
	}
	
	@Override
	public void getMsgListExcelDown(MsgVO params, HttpServletResponse response) {
        log.info("##### class.method :: { MsgServiceImpl.getMsgListExcelDown } start #####");

		//엑셀생성(header 생성, 액셀헤더생성 -> 액셀바디생성 -> 액셀파일쓰기)
    	//String[] header = {"CHANNEL", "CREATE_DTM", "BR_CODE", "EM_CODE", "MSG_TITLE", "SELECT_LABEL", "RESULT_LABEL", "IS_MATCH", "PERCENTAGE". "MSGCONTEXT"};
		//2023-01-27 내용(MSG_CONTEXT) 추가
		String[] header = {"채널", "검증일시", "부점", "직원", "제목", "발송목적", "검증결과", "일치여부", "확률", "내용"};
    	 
    	ExcelHandler excelHandler = new MsgExcelDown(header, "msg_result","msg",500);
    	excelHandler.createExcelHeader();
    	 log.info("##### class.method :: { MsgServiceImpl.getMsgListExcelDown } params.toString() #####" + params.toString());
		mariaMsgMapper.getMsgListExcelDown(params, excelHandler);
		
        try {
			excelHandler.writeExcelFile(response);
		} catch (IOException e) {
			log.error("##### IOException e.getMessage :: "+ e.getMessage());
			log.error("##### IOException e.toString :: "+ e.toString());
		} catch (Exception e) {
			log.error("##### Exception e.getMessage :: "+ e.getMessage());
			log.error("##### Exception e.toString :: "+ e.toString());
		}  

        log.info("##### class.method :: { MsgServiceImpl.getMsgListExcelDown } end #####");
		return;
	}

	@Override
	public MsgVO getMsgNextInfo(HashMap<String, Object> paramMap) {
		
		MsgVO params = new MsgVO();

		params.setId((String) paramMap.get("msgId"));
		params.setSearchType((String) paramMap.get("searchType"));
		params.setSearchKeyword((String) paramMap.get("searchStartDate"));
		params.setSearchStartDate((String) paramMap.get("searchStartDate"));
		params.setSearchEndDate((String) paramMap.get("searchEndDate"));
		params.setCreateDtmOrd((String) paramMap.get("creatDtmOrd"));
		
		MsgVO msgVO = mariaMsgMapper.getMsgNextInfo(params); 
		return msgVO;
	}
	
	@Override
	public MsgVO getMsgPrevInfo(HashMap<String, Object> paramMap) {
		
		MsgVO params = new MsgVO();

		params.setId((String) paramMap.get("msgId"));
		params.setSearchType((String) paramMap.get("searchType"));
		params.setSearchKeyword((String) paramMap.get("searchStartDate"));
		params.setSearchStartDate((String) paramMap.get("searchStartDate"));
		params.setSearchEndDate((String) paramMap.get("searchEndDate"));
		params.setCreateDtmOrd((String) paramMap.get("creatDtmOrd"));
		
		MsgVO msgVO = mariaMsgMapper.getMsgPrevInfo(params); 
		return msgVO;
	}

}

