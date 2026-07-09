package com.mindslab.web.admin.service;

import java.util.HashMap;
import java.util.List;

import com.mindslab.web.paging.Criteria;
import com.mindslab.web.vo.MemberVO;

public interface AdminUserService {

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
	public int getUserTotalCount(MemberVO params);

	/**
	 * 사용자목록 페이징 및 검색 조회
	 * 
	 * @param null
	 * @return
	 * @throws Exception
	 */
	public List<MemberVO> getUserList(MemberVO params);

	/**
	 * 사용자정보 조회
	 * 
	 * @param null
	 * @return
	 * @throws Exception
	 */
	public MemberVO getUserInfo(String userId);

	/**
	 * 사용자 추가
	 * 
	 * @param null
	 * @return
	 * @throws Exception
	 */
	public HashMap<String, Object> addUser(MemberVO params);

	/**
	 * 사용자 수정
	 * 
	 * @param null
	 * @return
	 * @throws Exception
	 */
	public HashMap<String, Object> modUser(MemberVO params);

    /**
     * 패스워드 초기화
     * @param MemberVO memberVO
     * @return
     */
    public HashMap<String, Object> resetPassword(MemberVO params);

	/**
	 * 사용자 삭제
	 * 
	 * @param MemberVO params
	 * @return
	 * @throws Exception
	 */
	public HashMap<String, Object> deleteUser(MemberVO params);
	

}
