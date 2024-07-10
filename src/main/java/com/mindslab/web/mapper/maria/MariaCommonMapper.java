package com.mindslab.web.mapper.maria;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.mindslab.web.common.support.utils.CustomMap;

@Repository
public interface MariaCommonMapper {

	/**
	 * 메뉴트리 조회
	 * @return
	 */
	public List<CustomMap> getMenuTree(CustomMap param);
    
}
