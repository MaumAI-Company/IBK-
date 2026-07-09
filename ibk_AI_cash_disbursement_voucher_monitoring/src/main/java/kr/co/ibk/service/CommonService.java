package kr.co.ibk.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.cache.annotation.Cacheable;

import kr.co.ibk.common.utils.CustomMap;
import kr.co.ibk.repository.CommonRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommonService extends _BaseService{
	private final CommonRepository commonRepository;
	
	@Cacheable("menu")
	public List<CustomMap> getMenuTree(CustomMap param) {
		// TODO Auto-generated method stub
		List<CustomMap> result = new ArrayList();
		log.info("mariaAdminAuthManagementMapper.getMenuTree() Start");
		result = commonRepository.getMenuTree(param);
		log.info("mariaAdminAuthManagementMapper.getMenuTree() End");
		return result;
	}	
}
