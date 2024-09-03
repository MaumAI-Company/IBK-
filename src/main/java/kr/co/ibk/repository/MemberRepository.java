package kr.co.ibk.repository;

import org.springframework.stereotype.Repository;

import kr.co.ibk.domain.web.MemberInfo;


@Repository
public interface MemberRepository {

    /**
     * 사용자의 권한 포함 정보 조회 
     * 로그인 시 스프링 시큐리티 로그인에 사용 됨
     * @param String userId
     * @return
     */
	public MemberInfo getAuthInfo(String userId);

    /**
     * 사용자의 정보 조회 
     * 마이 페이지 등 개인정보 조회
     * @param String userId
     * @return
     */
    public MemberInfo getUserInfo(String userId);

    /**
     * 사용자의 패스워드 변경
     * @param MemberInfo MemberInfo
     * @return
     */
    public long setUserPassword(MemberInfo params);

    /**
     * 사용자의 패스워드 실패 카운트 조회
     * @param MemberInfo MemberInfo
     * @return
     */
    public long getPasswordFailCnt(MemberInfo params);

    /**
     * 사용자의 패스워드 실패 카운트 초기화
     * @param MemberInfo MemberInfo
     * @return
     */
    public long resetPasswordFailCnt(MemberInfo params);

    /**
     * 사용자의 패스워드 실패 카운트 변경
     * @param MemberInfo MemberInfo
     * @return
     */
    public long addPasswordFailCnt(MemberInfo params);
    
}
