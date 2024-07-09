package com.mindslab.web.mapper.maria;

import org.springframework.stereotype.Repository;

import com.mindslab.web.vo.MemberVO;

@Repository
public interface MariaMemberMapper {

    /**
     * 사용자의 권한 포함 정보 조회 
     * 로그인 시 스프링 시큐리티 로그인에 사용 됨
     * @param String userId
     * @return
     */
	public MemberVO getAuthInfo(String userId);

    /**
     * 사용자의 정보 조회 
     * 마이 페이지 등 개인정보 조회
     * @param String userId
     * @return
     */
    public MemberVO getUserInfo(String userId);

    /**
     * 사용자의 패스워드 변경
     * @param MemberVO memberVO
     * @return
     */
    public long setUserPassword(MemberVO params);

    /**
     * 사용자의 패스워드 실패 카운트 조회
     * @param MemberVO memberVO
     * @return
     */
    public long getPasswordFailCnt(MemberVO params);

    /**
     * 사용자의 패스워드 실패 카운트 초기화
     * @param MemberVO memberVO
     * @return
     */
    public long resetPasswordFailCnt(MemberVO params);

    /**
     * 사용자의 패스워드 실패 카운트 변경
     * @param MemberVO memberVO
     * @return
     */
    public long addPasswordFailCnt(MemberVO params);
    
}
