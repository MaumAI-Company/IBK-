package com.mindslab.web.user.service;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mindslab.web.common.support.utils.CustomMap;
import com.mindslab.web.common.support.utils.excel.ExcelHandler;
import com.mindslab.web.common.support.utils.excel.StatisticExcelDown;
import com.mindslab.web.mapper.maria.MariaStatisticMapper;
import com.mindslab.web.vo.ScanVO;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class StatisticServiceImpl implements StatisticService {
	
	@Autowired
	private MariaStatisticMapper mariaStatisticMapper;

	@Override
	public List<CustomMap> getStatisticList(HashMap<String, Object> paramMap) {
		
		if (paramMap != null) {
			if ( (paramMap.get("searchStartDate") == null || paramMap.get("searchStartDate").equals("")) 
				 && (paramMap.get("searchEndDate") == null || paramMap.get("searchEndDate").equals("")) ) {
				paramMap.put("mDiff", 3);
			}
			
			String searchStartDate = paramMap.get("searchStartDate") == null || paramMap.get("searchStartDate").equals("")
					? initialDate("start") : (String) paramMap.get("searchStartDate");
			String searchEndDate = paramMap.get("searchEndDate") == null || paramMap.get("searchEndDate").equals("") 
					? initialDate("end") : (String) paramMap.get("searchEndDate");

			paramMap.put("searchStartDate", searchStartDate);
			paramMap.put("searchEndDate", searchEndDate);
		}

		List<CustomMap> selectList = mariaStatisticMapper.getStatisticDate(paramMap);
		return selectList;
	}
	
	@Override
	public void getStatisticListExcelDown(HashMap<String, Object> paramMap, HttpServletResponse response) {
        log.info("##### class.method :: { StatisticServiceImpl.getStatisticListExcelDown } start #####");

		//엑셀생성(header 생성, 액셀헤더생성 -> 액셀바디생성 -> 액셀파일쓰기)
    	String[] header = {"DATE","REAL","PAPER","MONITOR"};
    	 
    	ExcelHandler excelHandler = new StatisticExcelDown(header, "statistic","statistic",500);
    	excelHandler.createExcelHeader();

    	mariaStatisticMapper.getStatisticListExcelDown(paramMap, excelHandler);
		
        try {
			excelHandler.writeExcelFile(response);
		} catch (IOException e) {
			log.error("##### IOException e.getMessage :: "+ e.getMessage());
			log.error("##### IOException e.toString :: "+ e.toString());
		} catch (Exception e) {
			log.error("##### Exception e.getMessage :: "+ e.getMessage());
			log.error("##### Exception e.toString :: "+ e.toString());
		}  

        log.info("##### class.method :: { StatisticServiceImpl.getStatisticListExcelDown } end #####");
		return;
	}

	
	public String initialDate(String type) {

		// 날짜 포멧 설정
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date date = new Date();
		
		// 반환 날짜 타입
		String returnDate = "";
		
		// 날자 셋팅 시작
		Calendar cal = Calendar.getInstance(Locale.KOREA);
		
		if ("start".equals(type)) {
			// 현재날짜 - 3 달전
			cal.setTime(date);
			cal.add(Calendar.MONTH, -3);
			returnDate = sdf.format(cal.getTime());
		} else if ("end".equals(type)){
			// 현재날짜
			cal.setTime(date);
			returnDate = sdf.format(cal.getTime());
		}
		
	    return returnDate;
	}
}
