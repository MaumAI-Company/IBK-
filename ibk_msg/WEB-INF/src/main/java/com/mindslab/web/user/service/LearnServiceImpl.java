package com.mindslab.web.user.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.mindslab.web.common.support.utils.excel.ExcelUtil;
import com.mindslab.web.mapper.maria.MariaLearnMapper;
import com.mindslab.web.paging.PaginationInfo;
import com.mindslab.web.properties.MindsLabProperties;
import com.mindslab.web.vo.LearnVO;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class LearnServiceImpl implements LearnService {

	@Autowired
	protected MindsLabProperties mindsLabProperties;	
	
	@Autowired
	private MariaLearnMapper mariaLearnMapper;
	
	@Autowired
	ExcelUtil excelUtil;
	
	@Override
	public int getLearnTotalCount(LearnVO params) {
		return mariaLearnMapper.getLearnTotalCount(params);
	}

	@Override
	public List<LearnVO> getLearnList(LearnVO params) {
		List<LearnVO> learnList = Collections.emptyList();
		
		int learnTotalCount = mariaLearnMapper.getLearnTotalCount(params);
		
		PaginationInfo paginationInfo = new PaginationInfo(params);
		paginationInfo.setTotalRecordCount(learnTotalCount);
		
		params.setPaginationInfo(paginationInfo);
		
		if (learnTotalCount > 0) {
			learnList = mariaLearnMapper.getLearnList(params);
		}
			
		return learnList;
	}
	
	@Override
	public HashMap<String, Object> learnDatafileupload(MultipartFile file, LearnVO learnVO) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		
		String result = "fail";
		String msg = "실패"; 
        String fileName = "";
        String learnName = "";
        int pos = -1;
		
		try {

	        String uploadPath = mindsLabProperties.getLearnExcelPath() + calcPath();  
	        String originalFileExtension = ".xlsx";
	        String newFileName = Long.toString(System.nanoTime()) + originalFileExtension;
	        
	        fileName = file.getOriginalFilename();
	        pos = fileName.lastIndexOf(".");
	        if (pos > 0) {
	        	learnName = fileName.substring(0, pos);	
	        }

	        // 중복된 학습명인 경우 _(숫자) 추가
	        int nameCnt = mariaLearnMapper.getLearnNameCount(learnName);
	        if (nameCnt > 0) {
	        	learnName = learnName + "_" + Integer.toString(nameCnt+1);
	        }
	        
			// 파일 존재하지 않는 경우
			if (file.isEmpty()) {
				result = "fail";
				msg = "Excel(.xlsx) 파일을 선택해주세요.";
			}
			
			String contentType = file.getContentType();
			long fileSize = file.getSize();
			if(!contentType.equals("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")) {
				result = "fail";
				msg = "Excel(.xlsx) 파일을 선택해주세요.";
			}
			
			// 엑셀의 셀데이터를 가져와서 VO에 담기
			List<Map<String, Object>> listColumnMap = excelUtil.getListColumn(file, 3);
			List<Map<String, Object>> listDataMap = excelUtil.getListData(file, 1, 3);

			// 컬럼명 검증
			for (Map<String, Object> m : listColumnMap) {
				//UserVO userInfo = new UserVO();
			
				// 각 셀의 데이터를 VO에 set한다.
				//userInfo.setUserId(m.get("1").toString());
				//userInfo.setPassword(m.get("2").toString());
				//userInfo.setUserName(m.get("3").toString());
				/*
				System.out.println("##### 구 분 :: " + m.get("0").toString());
				System.out.println("##### 제 목 :: " + m.get("1").toString());
				System.out.println("##### 내 용 :: " + m.get("2").toString());
				System.out.println("##### 분 류 :: " + m.get("3").toString());
				
				if ("구 분".equals(m.get("0").toString())) {
					map.put("result", "");
					map.put("msg", msg);
					return map;
				}*/
			}
			
			// 데이터 검증
			for (Map<String, Object> m : listDataMap) {
				//UserVO userInfo = new UserVO();
			
				// 각 셀의 데이터를 VO에 set한다.
				//userInfo.setUserId(m.get("1").toString());
				//userInfo.setPassword(m.get("2").toString());
				//userInfo.setUserName(m.get("3").toString());
			}
			
			// 파일검증이 끝낫다면 업로드
	        File uploadDir = new File(uploadPath);
	        if(!uploadDir.exists()) {
        		FileUtils.forceMkdir(uploadDir);
	        }        	
			log.debug("newFileName ##### "+ newFileName);
			log.debug("uploadPath ##### "+ uploadPath);
			log.debug("getOriginalFilename ##### "+ fileName);// file.getOriginalFilename()
			log.debug("getSize ##### "+ fileSize);// file.getSize()
			log.debug("getContentType ##### "+ contentType);
			
			String savePath = uploadPath+"/"+newFileName;// 원본파일 저장경로 + 파일명
			File uploadFile = new File(savePath);
			
			byte[] bytes = file.getBytes();
			OutputStream out = new FileOutputStream(uploadFile);
			out.write(bytes);
			//file.transferTo(uploadFile);
			
			// 예외 없이 정상적으로 파일 업로드 되었다면 학습 테이블에 INSERT INTO
			//LearnVO learnVO = new LearnVO();
			learnVO.setLearnName(learnName); // 학습명(확장자 뺀 파일명)
			learnVO.setLearnFileName(fileName);// 원본 파일명
			learnVO.setLearnFileSaveName(newFileName);// 원본 파일 저장 명
			learnVO.setLearnFileSize(fileSize);// 원본파일 사이즈
			learnVO.setLearnFileUrl(savePath);// 원본파일 저장경로 + 파일명
			learnVO.setLearnStatus("2");// 0: 등록안됨, 1: 오류, 2: 등록완료, 3: 학습중, 4: 학습완료
			
			int cnt = mariaLearnMapper.learnDatafileupload(learnVO);
			
			result = "success";
			msg = "업로드 성공";
		} catch(Exception e) {
			result = "error";
			msg = "업로드 실패";
			
			try{
				fileName = file.getOriginalFilename();
				pos = fileName.lastIndexOf(".");
				if (pos > 0) {
					learnName = fileName.substring(0, pos);	
				}
				learnVO.setLearnName(learnName); // 학습명(확장자 뺀 파일명)
				learnVO.setLearnStatus("1");// 0: 등록안됨, 1: 오류, 2: 등록완료, 3: 학습중, 4: 학습완료
				
				// int cnt = mariaLearnMapper.learnDatafileupload(learnVO);
			} catch (Exception e1){
				result = "error";
				msg = "DB 업데이트 실패";
			}
		}
		
		map.put("result", result);
		map.put("msg", msg);
		return map;
	}
	
	@Override
	public HashMap<String, Object> answerDatafileupload(MultipartFile file, LearnVO learnVO) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		
		String result = "fail";
		String msg = "실패"; 
        String fileName = "";
        String learnName = "";
        LearnVO info = null;
        int pos = -1;
        
		try {
			String uploadPath = mindsLabProperties.getLearnExcelPath() + calcPath();
	        String originalFileExtension = ".xlsx";
	        String newFileName = Long.toString(System.nanoTime()) + originalFileExtension;
	        
	        fileName = file.getOriginalFilename();
	        pos = fileName.lastIndexOf(".");
	        if (pos > 0) {
	        	learnName = fileName.substring(0, pos);	
	        }
	        
			// 파일 존재하지 않는 경우
			if (file.isEmpty()) {
				result = "fail";
				msg = "Excel(.xlsx) 파일을 선택해주세요.";
			}
			
			String contentType = file.getContentType();
			long fileSize = file.getSize();
			if(!contentType.equals("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")) {
				result = "fail";
				msg = "Excel(.xlsx) 파일을 선택해주세요.";
			}
			
			// 엑셀의 셀데이터를 가져와서 VO에 담기
			List<Map<String, Object>> listColumnMap = excelUtil.getListColumn(file, 3);
			List<Map<String, Object>> listDataMap = excelUtil.getListData(file, 1, 3);

			// 컬럼명 검증
			for (Map<String, Object> m : listColumnMap) {
				//UserVO userInfo = new UserVO();
				// 각 셀의 데이터를 VO에 set한다.
				//userInfo.setUserId(m.get("1").toString());
				//userInfo.setPassword(m.get("2").toString());
				//userInfo.setUserName(m.get("3").toString());
			}
			
			// 데이터 검증
			for (Map<String, Object> m : listDataMap) {
			}
			
			// 파일검증이 끝낫다면 업로드
	        File uploadDir = new File(uploadPath);
	        if(!uploadDir.exists()) {
        		FileUtils.forceMkdir(uploadDir);
	        }        	
			log.debug("newFileName ##### "+ newFileName);
			log.debug("uploadPath ##### "+ uploadPath);
			log.debug("getOriginalFilename ##### "+ fileName);// file.getOriginalFilename()
			log.debug("getSize ##### "+ fileSize);// file.getSize()
			log.debug("getContentType ##### "+ contentType);
			
			String savePath = uploadPath+"/"+newFileName;// 원본파일 저장경로 + 파일명
			//File uploadFile = new File(savePath);
			File uploadFile = new File(uploadDir.getAbsolutePath(), newFileName);
			
			byte[] bytes = file.getBytes();
			OutputStream out = new FileOutputStream(uploadFile);
			out.write(bytes);
			//file.transferTo(uploadFile);
			
			// 예외 없이 정상적으로 파일 업로드 되었다면 학습 테이블에 UPDATE SET
			//LearnVO learnVO = new LearnVO();
			learnVO.setAnswerFileName(fileName);// 원본 파일명
			learnVO.setAnswerFileSaveName(newFileName);// 원본 파일 저장 명
			learnVO.setAnswerFileSize(fileSize);// 원본파일 사이즈
			learnVO.setAnswerFileUrl(savePath);// 원본파일 저장경로 + 파일명
			learnVO.setAnswerStatus("2");// 0: 등록안됨, 1: 오류, 2: 등록완료, 3: 학습중, 4: 학습완료
			int cnt = mariaLearnMapper.answerDatafileupload(learnVO);
			
			info = mariaLearnMapper.getLearnInfo(learnVO);
			
			result = "success";
			msg = "업로드 성공";
		} catch(Exception e) {
			result = "error";
			msg = "업로드 실패";
		}
		
		map.put("result", result);
		map.put("info", info);
		map.put("msg", msg);
		return map;
	}

	@Override
	public HashMap<String, Object> learningStatusUpdate(LearnVO learnVO) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		
		String result = "fail";
		String msg = "실패"; 
        LearnVO info = null;
        
		try {
			
			int cnt = mariaLearnMapper.learningStatusUpdate(learnVO);
			
			info = mariaLearnMapper.getLearnInfo(learnVO);
			
			result = "success";
			msg = "업데이트 성공";
		} catch(Exception e) {
			result = "error";
			msg = "업데이트 실패";
		}
		
		map.put("result", result);
		map.put("info", info);
		map.put("msg", msg);
		return map;
	}
	
	@Override
	public HashMap<String, Object> deleteLearn(LearnVO learnVO) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		
		String result = "fail";
		String msg = "실패"; 
        
		try {
			int cnt = mariaLearnMapper.deleteLearn(learnVO);
			
			result = "success";
			msg = "삭제 성공";
		} catch(Exception e) {
			result = "error";
			msg = "삭제 실패";
		}
		
		map.put("result", result);
		map.put("msg", msg);
		return map;
	}
	
	@Override
	public int getLearningCount(LearnVO params) {
		return mariaLearnMapper.getLearningCount(params);
	}
	
	// 날짜별 디렉토리 추출
	public static String calcPath() {
        Calendar cal = Calendar.getInstance();
        // File.separator : 디렉토리 구분자(\\)
        // 연도, ex) \\2022 
        String yearPath = File.separator + cal.get(Calendar.YEAR);
        System.out.println(yearPath);
        // 월, ex) \\2022\\10
        String monthPath = yearPath + File.separator + new DecimalFormat("00").format(cal.get(Calendar.MONTH) + 1);
        // 날짜, ex) \\2022\\10\\01
        String datePath = monthPath + File.separator + new DecimalFormat("00").format(cal.get(Calendar.DATE));
        
        return monthPath;
    }
}
