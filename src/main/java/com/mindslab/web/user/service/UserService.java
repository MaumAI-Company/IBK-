package com.mindslab.web.user.service;

import java.util.HashMap;

import com.mindslab.web.vo.MemberVO;

public interface UserService{
	
    /**
     * # 사용자 정보 조회
     * @param userId
     * @return Map<String,String>
     **/
	public HashMap<String, Object> getUserInfo(String userId); 
	
    /**
     * # 패스워드 변경 처리
     * @param userId
     * @return Map<String,String>
     **/
	public HashMap<String, Object> changeUserPassword(String userId, String chkPwd ,String newPwd);
	
	/**
     * # 실패 카운트 초기화
     * @param userId
     * @return Map<String,String>
     **/
	public HashMap<String, Object> resetPasswordFailCnt(String userId);

	
	/**
     * # 실패 카운트 증가.
     * @param userId
     * @return Map<String,String>
     **/
	public HashMap<String, Object> addPasswordFailCnt(String userId);
	
	

}
