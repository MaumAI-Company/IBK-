package com.mindslab.web.user.service;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mindslab.web.common.support.utils.excel.ExcelHandler;
import com.mindslab.web.common.support.utils.excel.ScanExcelDown;
import com.mindslab.web.mapper.maria.MariaScanMapper;
import com.mindslab.web.paging.PaginationInfo;
import com.mindslab.web.vo.ScanVO;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ScanServiceImpl implements ScanService {
	
	@Autowired
	private MariaScanMapper mariaScanMapper;

	@Override
	public int getScanTotalCount(ScanVO params) {
		return mariaScanMapper.getScanTotalCount(params);
	}

	@Override
	public List<ScanVO> getScanList(ScanVO params) {
		List<ScanVO> scanList = Collections.emptyList();
		
		int scanTotalCount = mariaScanMapper.getScanTotalCount(params);
		
		PaginationInfo paginationInfo = new PaginationInfo(params);
		paginationInfo.setTotalRecordCount(scanTotalCount);
		
		params.setPaginationInfo(paginationInfo);
		
		if (scanTotalCount > 0) {
			scanList = mariaScanMapper.getScanList(params);
		}
			
		return scanList;
	}
	
	@Override
	public void getScanListExcelDown(ScanVO params, HttpServletResponse response) {
        log.info("##### class.method :: { ScanServiceImpl.getScanListExcelDown } start #####");

		//엑셀생성(header 생성, 액셀헤더생성 -> 액셀바디생성 -> 액셀파일쓰기)
    	String[] header = {"FILE_NAME", "CREATE_DTM", "EDPS_CSN", "LEVEL", "DETECT_RESULT", "REAL_CELL", "MONITOR_CELL", "PAPER_CELL", "DETECT_LOG", "DOC_ID"};
    	 
    	ExcelHandler excelHandler = new ScanExcelDown(header, "detect_result","scan",500);
    	excelHandler.createExcelHeader();
    	 log.info("##### class.method :: { ScanServiceImpl.getScanListExcelDown } params.toString() #####" + params.toString());
		mariaScanMapper.getScanListExcelDown(params, excelHandler);
		
        try {
			excelHandler.writeExcelFile(response);
		} catch (IOException e) {
			log.error("##### IOException e.getMessage :: "+ e.getMessage());
			log.error("##### IOException e.toString :: "+ e.toString());
		} catch (Exception e) {
			log.error("##### Exception e.getMessage :: "+ e.getMessage());
			log.error("##### Exception e.toString :: "+ e.toString());
		}  

        log.info("##### class.method :: { ScanServiceImpl.getScanListExcelDown } end #####");
		return;
	}

	@Override
	public ScanVO getScanNextInfo(HashMap<String, Object> paramMap) {
		
		ScanVO params = new ScanVO();

		params.setId((String) paramMap.get("scanId"));
		params.setSearchType((String) paramMap.get("searchType"));
		params.setSearchKeyword((String) paramMap.get("searchStartDate"));
		params.setSearchStartDate((String) paramMap.get("searchStartDate"));
		params.setSearchEndDate((String) paramMap.get("searchEndDate"));
		params.setCreateDtmOrd((String) paramMap.get("creatDtmOrd"));
		
		ScanVO scanVO = mariaScanMapper.getScanNextInfo(params); 
		return scanVO;
	}
	
	@Override
	public ScanVO getScanPrevInfo(HashMap<String, Object> paramMap) {
		
		ScanVO params = new ScanVO();

		params.setId((String) paramMap.get("scanId"));
		params.setSearchType((String) paramMap.get("searchType"));
		params.setSearchKeyword((String) paramMap.get("searchStartDate"));
		params.setSearchStartDate((String) paramMap.get("searchStartDate"));
		params.setSearchEndDate((String) paramMap.get("searchEndDate"));
		params.setCreateDtmOrd((String) paramMap.get("creatDtmOrd"));
		
		ScanVO scanVO = mariaScanMapper.getScanPrevInfo(params); 
		return scanVO;
	}

}
