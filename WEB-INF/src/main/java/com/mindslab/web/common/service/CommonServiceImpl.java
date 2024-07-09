package com.mindslab.web.common.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mindslab.web.common.support.utils.CustomMap;
import com.mindslab.web.mapper.maria.MariaCommonMapper;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class CommonServiceImpl implements CommonService {
	
	@Autowired
	private MariaCommonMapper mariaCommonMapper;

	@Override
	public List<CustomMap> getMenuTree(CustomMap param) {
		// TODO Auto-generated method stub
		List<CustomMap> result = new ArrayList();
		log.info("mariaAdminAuthManagementMapper.getMenuTree() Start");
		result = mariaCommonMapper.getMenuTree(param);
		log.info("mariaAdminAuthManagementMapper.getMenuTree() End");
		return result;
	}

}
