package kr.co.ibk.repository;

import java.util.List;

import org.springframework.stereotype.Repository;

import kr.co.ibk.domain.web.MemberInfo;
import kr.co.ibk.domain.web.RoleInfo;


@Repository
public interface AdminMemberRepository {
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
	 * @param MemberInfo params
	 * @return
	 * @throws Exception
	 */
	public int getUserTotalCount(MemberInfo params);

	/**
	 * 사용자목록 페이징 및 검색 조회
	 * 
	 * @param MemberInfo params
	 * @return
	 * @throws Exception
	 */
	public List<MemberInfo> getUserList(MemberInfo params);

	/**
	 * 사용자정보 조회
	 * 
	 * @param String userId
	 * @return
	 * @throws Exception
	 */
	public MemberInfo getUserInfo(String userId);
	
	/**
	 * 사용자 추가
	 * 
	 * @param MemberInfo params
	 * @return
	 * @throws Exception
	 */
	public long addUser(MemberInfo params);

	/**
	 * 사용자 권한 추가
	 * 
	 * @param MemberInfo params
	 * @return
	 * @throws Exception
	 */
	public long addUserRole(MemberInfo params);

	/**
	 * 사용자 권한 조회
	 * 
	 * @param String userId
	 * @return
	 * @throws Exception
	 */
	public RoleInfo getUserRole(String userId);

	/**
	 * 사용자 권한 수정
	 * 
	 * @param String userId
	 * @return
	 * @throws Exception
	 */
	public long modUserRole(MemberInfo params);

	/**
	 * 사용자 권한 삭제
	 * 
	 * @param MemberInfo params
	 * @return
	 * @throws Exception
	 */
	public long deleteUserRole(MemberInfo params);

	/**
	 * 사용자 수정
	 * 
	 * @param MemberInfo params
	 * @return
	 * @throws Exception
	 */
	public long modUser(MemberInfo params);

	/**
	 * 사용자 삭제
	 * 
	 * @param MemberInfo params
	 * @return
	 * @throws Exception
	 */
	public long deleteUser(MemberInfo params);
	
    /**
     * 사용자의 패스워드 변경
     * @param MemberInfo MemberInfo
     * @return
     */
    public long setUserPassword(MemberInfo params);
	
}
