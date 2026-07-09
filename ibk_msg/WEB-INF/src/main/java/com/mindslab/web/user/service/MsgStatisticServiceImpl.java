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
import com.mindslab.web.common.support.utils.excel.MsgBrStatisticExcelDown;
import com.mindslab.web.common.support.utils.excel.MsgMatchStatisticExcelDown;
import com.mindslab.web.common.support.utils.excel.MsgResultStatisticExcelDown;
import com.mindslab.web.mapper.maria.MariaMsgMatchStatisticMapper;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class MsgStatisticServiceImpl implements MsgStatisticService {
	
	@Autowired
	private MariaMsgMatchStatisticMapper mariaMsgMatchStatisticMapper;

	@Override
	public List<CustomMap> getStatistic(HashMap<String, Object> paramMap) {
		List<CustomMap> selectList = null;
		if (paramMap != null) {
			if ( (paramMap.get("searchStartDate") == null || paramMap.get("searchStartDate").equals("")) 
				 && (paramMap.get("searchEndDate") == null || paramMap.get("searchEndDate").equals("")) ) {
				paramMap.put("diff", 2);
			}
			
			String searchStartDate = paramMap.get("searchStartDate") == null || paramMap.get("searchStartDate").equals("")
					? initialDate("start") : (String) paramMap.get("searchStartDate");
			String searchEndDate = paramMap.get("searchEndDate") == null || paramMap.get("searchEndDate").equals("") 
					? initialDate("end") : (String) paramMap.get("searchEndDate");

			paramMap.put("searchStartDate", searchStartDate);
			paramMap.put("searchEndDate", searchEndDate);

			// 실행조건  미사용
            /* 
			if (paramMap.get("searchCondition") != null && paramMap.get("searchCondition").equals("date")) {
				selectList = mariaMsgMatchStatisticMapper.getStatisticDate(paramMap);
			} else if (paramMap.get("searchCondition") != null && paramMap.get("searchCondition").equals("br")) {
				selectList = mariaMsgMatchStatisticMapper.getStatisticBr(paramMap);
			} else if (paramMap.get("searchCondition") != null && paramMap.get("searchCondition").equals("result")) {
				// select1 기존계약유지
				// select2 고객관리(감사인사,기념일축하)
				// select3 심의필마케팅(일반,카드)
				// select4 미심의 광고문자
				selectList = mariaMsgMatchStatisticMapper.getStatisticResult(paramMap);
			}
			*/
			
			
			//String searchBr = paramMap.get("searchBr") == null ? "" : (String) paramMap.get("searchBr");
            selectList = mariaMsgMatchStatisticMapper.getStatisticBrResult(paramMap);
            			
			
		}
		
		return selectList;
	}
	
	@Override
	public void getStatisticExcelDown(HashMap<String, Object> paramMap, HttpServletResponse response) {
        log.info("##### class.method :: { StatisticServiceImpl.getStatisticExcelDown } start #####");

		//엑셀생성(header 생성, 액셀헤더생성 -> 액셀바디생성 -> 액셀파일쓰기)
        ExcelHandler excelHandler = null;
        /*
		if (paramMap.get("searchCondition") != null && paramMap.get("searchCondition").equals("date")) {
			String[] header = {"일자","전체","일치","불일치"};
	    	excelHandler = new MsgMatchStatisticExcelDown(header, "statistic","statistic",500);
	    	excelHandler.createExcelHeader();

	    	mariaMsgMatchStatisticMapper.getStatisticDateExcelDown(paramMap, excelHandler);
		} else if (paramMap.get("searchCondition") != null && paramMap.get("searchCondition").equals("br")) {
			String[] header = {"부점코드","전체","일치","불일치"};
	    	excelHandler = new MsgBrStatisticExcelDown(header, "statistic","statistic",500);
	    	excelHandler.createExcelHeader();

	    	mariaMsgMatchStatisticMapper.getStatisticBrExcelDown(paramMap, excelHandler);
		} else if (paramMap.get("searchCondition") != null && paramMap.get("searchCondition").equals("result")) {
			// select1 기존계약유지
			// select2 고객관리(감사인사,기념일축하)
			// select3 심의필 마케팅(일반,카드)
			// select4 미심의 광고문자
			String[] header = {"일자","전체","기존계약유지","고객관리","심의필 마케팅", "미심의 광고문자"};
	    	excelHandler = new MsgResultStatisticExcelDown(header, "statistic","statistic",500);
	    	excelHandler.createExcelHeader();

	    	mariaMsgMatchStatisticMapper.getStatisticResultExcelDown(paramMap, excelHandler);
		}
        */

        // select1 기존계약유지
        // select2 고객관리(감사인사,기념일축하)
        // select3 심의필 마케팅(일반,카드)
        // select4 미심의 광고문자
        String[] header = {"일자","전체","기존계약유지","고객관리","심의필 마케팅", "미심의 광고문자"};
        excelHandler = new MsgResultStatisticExcelDown(header, "statistic","statistic",500);
        excelHandler.createExcelHeader();
        mariaMsgMatchStatisticMapper.getStatisticBrResultExcelDown(paramMap, excelHandler);
		
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
			cal.add(Calendar.MONTH, -2);
			returnDate = sdf.format(cal.getTime());
		} else if ("end".equals(type)){
			// 현재날짜
			cal.setTime(date);
			returnDate = sdf.format(cal.getTime());
		}
		
	    return returnDate;
	}
}
