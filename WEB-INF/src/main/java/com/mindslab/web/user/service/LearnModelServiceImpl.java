package com.mindslab.web.user.service;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mindslab.web.common.support.utils.excel.ExcelUtil;
import com.mindslab.web.mapper.maria.MariaLearnModelMapper;
import com.mindslab.web.paging.PaginationInfo;
import com.mindslab.web.properties.MindsLabProperties;
import com.mindslab.web.vo.LearnModelVO;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class LearnModelServiceImpl implements LearnModelService {

	@Autowired
	protected MindsLabProperties mindsLabProperties;	
	
	@Autowired
	private MariaLearnModelMapper mariaLearnModelMapper;
		
	@Override
	public int getLearnModelTotalCount(LearnModelVO params) {
		return mariaLearnModelMapper.getLearnModelTotalCount(params);
	}

	@Override
	public List<LearnModelVO> getLearnModelList(LearnModelVO params) {
		List<LearnModelVO> learnList = Collections.emptyList();
		
		int learnTotalCount = mariaLearnModelMapper.getLearnModelTotalCount(params);
		
		PaginationInfo paginationInfo = new PaginationInfo(params);
		paginationInfo.setTotalRecordCount(learnTotalCount);
		
		params.setPaginationInfo(paginationInfo);
		
		if (learnTotalCount > 0) {
			learnList = mariaLearnModelMapper.getLearnModelList(params);
		}
			
		return learnList;
	}

	@Override
	public HashMap<String, Object> deployStatusUpdate(LearnModelVO learnModelVO) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		
		String result = "fail";
		String msg = "실패"; 
        LearnModelVO info = null;
        
		try {
			
			int cnt = mariaLearnModelMapper.deployStatusUpdate(learnModelVO);
			
			info = mariaLearnModelMapper.getLearnModelInfo(learnModelVO);
			
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
	public int getDeployingCount(LearnModelVO params) {
		return mariaLearnModelMapper.getDeployingCount(params);
	}

}
