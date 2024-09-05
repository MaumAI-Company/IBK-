package com.mindslab.web.admin.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mindslab.web.common.support.utils.CustomMap;
import com.mindslab.web.config.EncryptConfig;
import com.mindslab.web.mapper.maria.MariaAdminAuthManagementMapper;
import com.mindslab.web.paging.PaginationInfo;
import com.mindslab.web.vo.MenuAuthMemberVO;

import lombok.extern.slf4j.Slf4j;


@Service
@Slf4j
public class AdminAuthManagementServiceImpl implements AdminAuthManagementService{
	
	@Autowired
	private MariaAdminAuthManagementMapper mariaAdminAuthManagementMapper;
	
	@Autowired
	private EncryptConfig encryptConfig;

	@Override
	public int getUserCount(String userId){
		return mariaAdminAuthManagementMapper.getUserCount(userId);
	}

	@Override
	public int getUserTotalCount(MenuAuthMemberVO params){
		return mariaAdminAuthManagementMapper.getUserTotalCount(params);
	}

	@Override
	public List<MenuAuthMemberVO> getUserList(MenuAuthMemberVO params){
		List<MenuAuthMemberVO> userList = Collections.emptyList();
		
		int userTotalCount = mariaAdminAuthManagementMapper.getUserTotalCount(params);
		
		PaginationInfo paginationInfo = new PaginationInfo(params);
		paginationInfo.setTotalRecordCount(userTotalCount);
		
		params.setPaginationInfo(paginationInfo);
		
		if (userTotalCount > 0) {
			userList = mariaAdminAuthManagementMapper.getUserList(params);
		}
			
		return userList;
	}
	
	@Override
	public List<CustomMap> getMenuTree(CustomMap param){
		// TODO Auto-generated method stub
		List<CustomMap> result = new ArrayList();
		log.info("mariaAdminAuthManagementMapper.getMenuTree() Start");
		result = mariaAdminAuthManagementMapper.getMenuTree(param);
		log.info("mariaAdminAuthManagementMapper.getMenuTree() End");
		return result;
	}

	@Override
	public void insertMemberMenu(CustomMap param){
		log.info("mariaAdminAuthManagementMapper.insertMemberMenu() Start");
		log.info("mariaAdminAuthManagementMapper.insertMemberMenu() param : {}", param);
		log.info("mariaAdminAuthManagementMapper.insertMemberMenu() result : ",mariaAdminAuthManagementMapper.insertMemberMenu(param));
		log.info("mariaAdminAuthManagementMapper.insertMemberMenu() End");		
	}
}
