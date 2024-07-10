package com.mindslab.web.admin.service;

import java.util.HashMap;
import java.util.List;

import com.mindslab.web.common.support.utils.CustomMap;
import com.mindslab.web.paging.Criteria;
import com.mindslab.web.vo.MemberVO;
import com.mindslab.web.vo.MenuAuthMemberVO;

public interface AdminAuthManagementService {

	/**
	 * 특정 사용자 카운트
	 * 
	 * @param null
	 * @return
	 * @throws Exception
	 */
	public int getUserCount(String userId);
	
	/**
	 * 사용자 전체 카운트
	 * 
	 * @param null
	 * @return
	 * @throws Exception
	 */
	public int getUserTotalCount(MenuAuthMemberVO params);

	/**
	 * 사용자목록 페이징 및 검색 조회
	 * 
	 * @param null
	 * @return
	 * @throws Exception
	 */
	public List<MenuAuthMemberVO> getUserList(MenuAuthMemberVO params);
	
	/**
	 * 메뉴목록 조회
	 * @return
	 */
    public List<CustomMap> getMenuTree(CustomMap param);

    public void insertMemberMenu(CustomMap param);
}
