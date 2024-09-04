package kr.co.ibk.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.co.ibk.common.utils.CustomMap;
import kr.co.ibk.repository.AdminMenuManagementRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AdminMenuManagementService {
	
	private final AdminMenuManagementRepository adminMenuManagementRepository;
	/**     adminMenuManagementRepository
	 * 메뉴 목록 조회
	 */
	
	public List<CustomMap> getMenuTree(){
		// TODO Auto-generated method stub
		List<CustomMap> result = new ArrayList();
		log.info("AdminMenuManagementServiceimpl.getMenuTree() Start");
		result = adminMenuManagementRepository.getMenuTree();
		log.info("AdminMenuManagementServiceimpl.getMenuTree() End");
		return result;
	}
	
	public List<CustomMap> getMenuTreeOrderCount(){
		// TODO Auto-generated method stub
		List<CustomMap> result = new ArrayList();
		log.info("AdminMenuManagementServiceimpl.getMenuTreeOrderCount() Start");
		result = adminMenuManagementRepository.getMenuTreeOrderCount();
		log.info("AdminMenuManagementServiceimpl.getMenuTreeOrderCount() End");
		return result;
	}
	
	public void updateHcMenuDepthLevel(){
		log.info("AdminMenuManagementServiceimpl.updateHcMenuDepthLevel() Start");
		log.info("AdminMenuManagementServiceimpl.updateHcMenuDepthLevel() result : ",adminMenuManagementRepository.updateHcMenuDepthLevel());
		log.info("AdminMenuManagementServiceimpl.updateHcMenuDepthLevel() End");		
	}
	
	public void insertHcMenuTree(CustomMap param){
		log.info("AdminMenuManagementServiceimpl.insertHcMenuTree() Start");
		log.info("AdminMenuManagementServiceimpl.insertHcMenuTree() param : {}", param);
		log.info("AdminMenuManagementServiceimpl.insertHcMenuTree() result : ",adminMenuManagementRepository.insertHcMenuTree(param));
		log.info("AdminMenuManagementServiceimpl.insertHcMenuTree() End");		
	}
	
	public void updateHcMenuTree(CustomMap param){
		// TODO Auto-generated method stub
		log.info("AdminMenuManagementServiceimpl.updateHcMenuTree() Start");
		log.info("AdminMenuManagementServiceimpl.updateHcMenuTree() param : {}", param);
		log.info("AdminMenuManagementServiceimpl.updateHcMenuTree() result : ",adminMenuManagementRepository.updateHcMenuTree(param));
		log.info("AdminMenuManagementServiceimpl.updateHcMenuTree() End");		
	}
	
	public void deleteHcMenuTree(CustomMap param){
		// TODO Auto-generated method stub
		log.info("AdminMenuManagementServiceimpl.updateHcMenuTree() Start");
		log.info("AdminMenuManagementServiceimpl.updateHcMenuTree() param : {}", param);
		
		List<Map> paramMapList = (List<Map>)param.get("menuList");
		
		for(Map paramMap : paramMapList) {
			paramMap.put("menuModId", param.get("menuModId"));
			
	    	CustomMap paramCmap = new CustomMap();
	    	paramCmap.putAll(paramMap);
	    	
			log.info("AdminMenuManagementServiceimpl.updateHcMenuTree() result : ",adminMenuManagementRepository.deleteHcMenuTree(paramCmap));
		}
		
		log.info("AdminMenuManagementServiceimpl.updateHcMenuTree() End");		
	}
}
