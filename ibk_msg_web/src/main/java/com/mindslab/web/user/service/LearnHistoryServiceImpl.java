package com.mindslab.web.user.service;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mindslab.web.mapper.maria.MariaLearnHistoryMapper;
import com.mindslab.web.paging.PaginationInfo;
import com.mindslab.web.vo.LearnHistoryVO;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class LearnHistoryServiceImpl implements LearnHistoryService {
	
	@Autowired
	private MariaLearnHistoryMapper mariaLearnHistoryMapper;

	@Override
	public int getLearnHistoryTotalCount(LearnHistoryVO params) {
		return mariaLearnHistoryMapper.getLearnHistoryTotalCount(params);
	}

	@Override
	public List<LearnHistoryVO> getLearnHistoryList(LearnHistoryVO params) {
		List<LearnHistoryVO> learnHistoryList = Collections.emptyList();
		
		int learnHistoryTotalCount = mariaLearnHistoryMapper.getLearnHistoryTotalCount(params);
		
		PaginationInfo paginationInfo = new PaginationInfo(params);
		paginationInfo.setTotalRecordCount(learnHistoryTotalCount);
		
		params.setPaginationInfo(paginationInfo);
		
		if (learnHistoryTotalCount > 0) {
			learnHistoryList = mariaLearnHistoryMapper.getLearnHistoryList(params);
		}
			
		return learnHistoryList;
	}

    @Override
    public HashMap<String, Object> addLearnHistory(LearnHistoryVO params) {
        HashMap<String, Object> map = new HashMap<String, Object>();
        String result = "fail";
        String msg = "실패";
        
        if ("1".equals(params.getUseStatus())) {
            stopLearnHistory(params);
        }
        
        int historyCnt = mariaLearnHistoryMapper.addLearnHistory(params);
        
        if (historyCnt > 0) {
            result = "success";
            msg = "성공";
        }
        
        map.put("result", result);
        map.put("msg", msg);
        return map;
    }

    @Override
    public HashMap<String, Object> stopLearnHistory(LearnHistoryVO params) {
        HashMap<String, Object> map = new HashMap<String, Object>();
        String result = "fail";
        String msg = "실패"; 
        int cnt = mariaLearnHistoryMapper.stopLearnHistory(params);

        if (cnt > 0) {
            result = "success";
            msg = "성공";
        }
        
        map.put("result", result);
        map.put("msg", msg);
        return map;
    }
	
}
