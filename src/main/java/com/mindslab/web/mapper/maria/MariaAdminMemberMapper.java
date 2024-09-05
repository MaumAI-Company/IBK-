
package com.mindslab.web.mapper.maria;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.mindslab.web.paging.Criteria;
import com.mindslab.web.vo.MemberVO;
import com.mindslab.web.vo.RoleVO;

@Repository
public interface MariaAdminMemberMapper{
	

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
	public int getUserTotalCount(MemberVO params);

	/**
	 * 사용자목록 페이징 및 검색 조회
	 * 
	 * @param MemberVO params
	 * @return
	 * @throws Exception
	 */
	public List<MemberVO> getUserList(MemberVO params);

	/**
	 * 사용자정보 조회
	 * 
	 * @param String userId
	 * @return
	 * @throws Exception
	 */
	public MemberVO getUserInfo(String userId);
	
	/**
	 * 사용자 추가
	 * 
	 * @param MemberVO params
	 * @return
	 * @throws Exception
	 */
	public long addUser(MemberVO params);

	/**
	 * 사용자 권한 추가
	 * 
	 * @param MemberVO params
	 * @return
	 * @throws Exception
	 */
	public long addUserRole(MemberVO params);

	/**
	 * 사용자 권한 조회
	 * 
	 * @param String userId
	 * @return
	 * @throws Exception
	 */
	public RoleVO getUserRole(String userId);

	/**
	 * 사용자 권한 수정
	 * 
	 * @param String userId
	 * @return
	 * @throws Exception
	 */
	public long modUserRole(MemberVO params);

	/**
	 * 사용자 권한 삭제
	 * 
	 * @param MemberVO params
	 * @return
	 * @throws Exception
	 */
	public long deleteUserRole(MemberVO params);

	/**
	 * 사용자 수정
	 * 
	 * @param MemberVO params
	 * @return
	 * @throws Exception
	 */
	public long modUser(MemberVO params);

	/**
	 * 사용자 삭제
	 * 
	 * @param MemberVO params
	 * @return
	 * @throws Exception
	 */
	public long deleteUser(MemberVO params);
	
    /**
     * 사용자의 패스워드 변경
     * @param MemberVO memberVO
     * @return
     */
    public long setUserPassword(MemberVO params);
	
}
