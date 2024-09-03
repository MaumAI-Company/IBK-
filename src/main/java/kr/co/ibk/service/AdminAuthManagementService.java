package kr.co.ibk.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.co.ibk.common.utils.CustomMap;
import kr.co.ibk.domain.web.MenuAuthMember;
import kr.co.ibk.model.paging.PaginationInfo;
import kr.co.ibk.repository.AdminAuthManagementRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AdminAuthManagementService extends _BaseService{
	
	private final AdminAuthManagementRepository adminAuthManagementRepository;
	
	public int getUserCount(String userId){
		return adminAuthManagementRepository.getUserCount(userId);
	}

	
	public int getUserTotalCount(MenuAuthMember params){
		return adminAuthManagementRepository.getUserTotalCount(params);
	}

	
	public List<MenuAuthMember> getUserList(MenuAuthMember params){
		List<MenuAuthMember> userList = Collections.emptyList();
		
		int userTotalCount = adminAuthManagementRepository.getUserTotalCount(params);
		
		PaginationInfo paginationInfo = new PaginationInfo(params);
		paginationInfo.setTotalRecordCount(userTotalCount);
		
		params.setPaginationInfo(paginationInfo);
		
		if (userTotalCount > 0) {
			userList = adminAuthManagementRepository.getUserList(params);
		}
			
		return userList;
	}
	
	
	public List<CustomMap> getMenuTree(CustomMap param){
		// TODO Auto-generated method stub
		List<CustomMap> result = new ArrayList();
		log.info("adminAuthManagementRepository.getMenuTree() Start");
		result = adminAuthManagementRepository.getMenuTree(param);
		log.info("adminAuthManagementRepository.getMenuTree() End");
		return result;
	}

	
	public void insertMemberMenu(CustomMap param){
		log.info("adminAuthManagementRepository.insertMemberMenu() Start");
		log.info("adminAuthManagementRepository.insertMemberMenu() param : {}", param);
		
		List<Map> list = (List<Map>)param.get("memberMenuList");
		for(Map paramMap : list) {
			paramMap.put("regMemId", param.get("regMemId"));
			
	    	CustomMap paramCmap = new CustomMap();
	    	paramCmap.putAll(paramMap);
	    	
			adminAuthManagementRepository.insertMemberMenu(paramCmap);
		}
		
		//log.info("adminAuthManagementRepository.insertMemberMenu() result : ",adminAuthManagementRepository.insertMemberMenu(param));
		log.info("adminAuthManagementRepository.insertMemberMenu() End");		
	}
}
