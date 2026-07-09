package com.mindslab.web.admin.service;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mindslab.web.config.EncryptConfig;
import com.mindslab.web.mapper.maria.MariaAdminMemberMapper;
import com.mindslab.web.mapper.maria.MariaMemberMapper;
import com.mindslab.web.paging.PaginationInfo;
import com.mindslab.web.vo.MemberVO;
import com.mindslab.web.vo.RoleVO;

import lombok.extern.slf4j.Slf4j;


@Service
@Slf4j
public class AdminUserServiceImpl implements AdminUserService{
	
	// 초기화 및 아이디 생성시 초기 비밀번호 설정 ibk!1234 -> Kz$1d9ah@
	final String RESET_PASSWORD = "Kz$1d9ah@";
	
	@Autowired
	private MariaAdminMemberMapper mariaAdminMemberMapper;
	
	@Autowired
	private MariaMemberMapper mariaMemberMapper;
	
	@Autowired
	private EncryptConfig encryptConfig;

	@Override
	public int getUserCount(String userId){
		return mariaAdminMemberMapper.getUserCount(userId);
	}

	@Override
	public int getUserTotalCount(MemberVO params){
		return mariaAdminMemberMapper.getUserTotalCount(params);
	}

	@Override
	public List<MemberVO> getUserList(MemberVO params){
		List<MemberVO> userList = Collections.emptyList();
		
		int userTotalCount = mariaAdminMemberMapper.getUserTotalCount(params);
		
		PaginationInfo paginationInfo = new PaginationInfo(params);
		paginationInfo.setTotalRecordCount(userTotalCount);
		
		params.setPaginationInfo(paginationInfo);
		
		if (userTotalCount > 0) {
			userList = mariaAdminMemberMapper.getUserList(params);
		}
			
		return userList;
	}

	@Override
	public MemberVO getUserInfo(String userId){
		
		// 사용자 정보 조회
		MemberVO memberVO = mariaAdminMemberMapper.getUserInfo(userId);
		// 사용자의 로그인 권한 조회
		RoleVO roleVO = mariaAdminMemberMapper.getUserRole(userId);
		if (roleVO != null) {
			memberVO.setRoleId(roleVO.getRoleId());
		}
		
		return memberVO;
	}
	
	@Override
	public HashMap<String, Object> addUser(MemberVO params){
		HashMap<String, Object> map = new HashMap<String, Object>();// 반환 데이터
		String status = "ERROR"; // 상태
		String msg = "에러";// 메시지
		
		// 사용자 추가 / 권한 추가
		long userAddCnt = 0;
		long roleAddCnt = 0;
		
		// 사용자 초기 등록시 비밀번호 RESET_PASSWORD 로 초기화 > 추후 암호화하는 변경작업 있어야 함
		String newPwd = RESET_PASSWORD;
		String encPwd = encryptConfig.passwordEncoder("bcrypt").encode(newPwd);
		params.setMemPwd(encPwd);
		params.setMemBefoPwd(encPwd);
		
		userAddCnt = mariaAdminMemberMapper.addUser(params);
		
		if (userAddCnt==1L) {
			// 사용자 추가에 성공하였다면 권한 추가 실행
			// 사용자 권한 추가
			// 사용자 정보 조회
			MemberVO memberVO = mariaAdminMemberMapper.getUserInfo(params.getMemId());
			params.setMemSeq(memberVO.getMemSeq());
			roleAddCnt = mariaAdminMemberMapper.addUserRole(params);
		}
		
		if (userAddCnt==1L && roleAddCnt == 1L) {
			status = "SUCCESS";
			msg = "정상적으로 추가되었습니다.";
		} else if (userAddCnt==1L && roleAddCnt != 1L) {
			status = "FAIL";
			msg = "사용자 권한 추가를 실패하였습니다.";
		}  

		map.put("status", status);
		map.put("msg", msg);
		
		return map;
	}

	@Override
	public HashMap<String, Object> modUser(MemberVO params){
		HashMap<String, Object> map = new HashMap<String, Object>();// 반환 데이터
		String status = "ERROR"; // 상태
		String msg = "에러";// 메시지
		
		// 사용자 추가 / 권한 추가
		long userModCnt = 0;
		long roleModCnt = 0;
		
		userModCnt = mariaAdminMemberMapper.modUser(params);
		
		if (userModCnt==1L) {
			// 사용자 추가에 성공하였다면 권한 추가 실행
			// 사용자 권한 추가
			roleModCnt = mariaAdminMemberMapper.modUserRole(params);
		}
		
		if (userModCnt==1L && roleModCnt == 1L) {
			status = "SUCCESS";
			msg = "정상적으로 수정되었습니다.";
		} else if (userModCnt==1L && roleModCnt != 1L) {
			status = "FAIL";
			msg = "사용자 권한 추가를 실패하였습니다.";
		}  

		map.put("status", status);
		map.put("msg", msg);
		
		return map;
	}

	@Override
	public HashMap<String, Object> resetPassword(MemberVO params) {
		HashMap<String, Object> map = new HashMap<String, Object>();// 반환 데이터
		String status = "ERROR"; // 상태
		String msg = "에러";// 메시지
		
		// 리셋
		long resetCnt = 0;
		
		// 사용자 비밀번호 초기화시 RESET_PASSWORD 로 초기화 > 추후 암호화하는 변경작업 있어야 함
		String newPwd = RESET_PASSWORD;
		String encPwd = encryptConfig.passwordEncoder("bcrypt").encode(newPwd);
		params.setMemPwd(encPwd);
		
		resetCnt = mariaAdminMemberMapper.setUserPassword(params);
		
		if (resetCnt==1L ) {
			status = "SUCCESS";
			msg = "정상적으로 초기화 되었습니다.";
			mariaMemberMapper.resetPasswordFailCnt(params);
		} else if (resetCnt != 1L) {
			status = "FAIL";
			msg = "패스워드 초기화를 실패하였습니다.";
		}  

		map.put("status", status);
		map.put("msg", msg);
		
		return map;
	}

	@Override
	public HashMap<String, Object> deleteUser(MemberVO params) {
		HashMap<String, Object> map = new HashMap<String, Object>();// 반환 데이터
		String status = "ERROR"; // 상태
		String msg = "에러";// 메시지
		
		// 삭제 카운트
		long deleteCnt = 0;
		
		// 상태값 삭제
		params.setMemStat("delete");
		
		deleteCnt = mariaAdminMemberMapper.deleteUser(params);
		
		if (deleteCnt >= 1L) {
			status = "SUCCESS";
			msg = "정상적으로 삭제되었습니다.";
		} else if (deleteCnt != 1L) {
			status = "FAIL";
			msg = "삭제를 실패하였습니다.";
		}  

		map.put("status", status);
		map.put("msg", msg);
		
		return map;
	}
	
}
