package com.mindslab.web.mapper.maria;

import com.mindslab.web.common.support.utils.CustomMap;
import com.mindslab.web.vo.MenuAuthMemberVO;

import java.util.List;

import org.springframework.stereotype.Repository;

@Repository
public interface MariaAdminAuthManagementMapper {
	/**
	 * 특정 사용자 카운트
	 * 
	 * @param String userId
	 * @return
	 * @throws Exception
	 */
	public int getUserCount(String userId);

	/**
	 * 사용자 전체 카운트
	 * 
	 * @param MemberVO params
	 * @return
	 * @throws Exception
	 */
	public int getUserTotalCount(MenuAuthMemberVO params);

	/**
	 * 사용자목록 페이징 및 검색 조회
	 * 
	 * @param MemberVO params
	 * @return
	 * @throws Exception
	 */
	public List<MenuAuthMemberVO> getUserList(MenuAuthMemberVO params);
	
	/**
	 * 메뉴트리 조회
	 * @return
	 */
	public List<CustomMap> getMenuTree(CustomMap param);
	
	/**
	 * 메뉴 등록처리
	 * @param param
	 * @return
	 */
	public int insertMemberMenu(CustomMap param);	
}
