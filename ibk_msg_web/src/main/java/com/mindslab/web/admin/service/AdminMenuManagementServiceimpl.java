package com.mindslab.web.admin.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mindslab.web.common.support.utils.CustomMap;
import com.mindslab.web.mapper.maria.MariaAdminMenuManagementMapper;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class AdminMenuManagementServiceimpl implements AdminMenuManagementService {
	
	@Autowired
	private MariaAdminMenuManagementMapper mariaAdminMenuManagementMapper;
	/**     MariaAdminMenuManagementMapper
	 * 메뉴 목록 조회
	 */
	@Override
	public List<CustomMap> getMenuTree(){
		// TODO Auto-generated method stub
		List<CustomMap> result = new ArrayList();
		log.info("AdminMenuManagementServiceimpl.getMenuTree() Start");
		result = mariaAdminMenuManagementMapper.getMenuTree();
		log.info("AdminMenuManagementServiceimpl.getMenuTree() End");
		return result;
	}
	@Override
	public List<CustomMap> getMenuTreeOrderCount(){
		// TODO Auto-generated method stub
		List<CustomMap> result = new ArrayList();
		log.info("AdminMenuManagementServiceimpl.getMenuTreeOrderCount() Start");
		result = mariaAdminMenuManagementMapper.getMenuTreeOrderCount();
		log.info("AdminMenuManagementServiceimpl.getMenuTreeOrderCount() End");
		return result;
	}
	@Override
	public void updateHcMenuDepthLevel(){
		log.info("AdminMenuManagementServiceimpl.updateHcMenuDepthLevel() Start");
		log.info("AdminMenuManagementServiceimpl.updateHcMenuDepthLevel() result : ",mariaAdminMenuManagementMapper.updateHcMenuDepthLevel());
		log.info("AdminMenuManagementServiceimpl.updateHcMenuDepthLevel() End");		
	}
	@Override
	public void insertHcMenuTree(CustomMap param){
		log.info("AdminMenuManagementServiceimpl.insertHcMenuTree() Start");
		log.info("AdminMenuManagementServiceimpl.insertHcMenuTree() param : {}", param);
		log.info("AdminMenuManagementServiceimpl.insertHcMenuTree() result : ",mariaAdminMenuManagementMapper.insertHcMenuTree(param));
		log.info("AdminMenuManagementServiceimpl.insertHcMenuTree() End");		
	}
	@Override
	public void updateHcMenuTree(CustomMap param){
		// TODO Auto-generated method stub
		log.info("AdminMenuManagementServiceimpl.updateHcMenuTree() Start");
		log.info("AdminMenuManagementServiceimpl.updateHcMenuTree() param : {}", param);
		log.info("AdminMenuManagementServiceimpl.updateHcMenuTree() result : ",mariaAdminMenuManagementMapper.updateHcMenuTree(param));
		log.info("AdminMenuManagementServiceimpl.updateHcMenuTree() End");		
	}
	@Override
	public void deleteHcMenuTree(CustomMap param){
		// TODO Auto-generated method stub
		log.info("AdminMenuManagementServiceimpl.updateHcMenuTree() Start");
		log.info("AdminMenuManagementServiceimpl.updateHcMenuTree() param : {}", param);
		log.info("AdminMenuManagementServiceimpl.updateHcMenuTree() result : ",mariaAdminMenuManagementMapper.deleteHcMenuTree(param));
		log.info("AdminMenuManagementServiceimpl.updateHcMenuTree() End");		
	}
	
}
