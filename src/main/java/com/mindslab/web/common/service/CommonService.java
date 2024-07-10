package com.mindslab.web.common.service;

import java.util.List;

import com.mindslab.web.common.support.utils.CustomMap;

public interface CommonService {

	/**
	 * 메뉴트리 조회
	 * @return
	 */
	public List<CustomMap> getMenuTree(CustomMap param);
}
