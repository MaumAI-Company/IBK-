package kr.co.ibk.service;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.co.ibk.common.utils.PasswordGenerator;
import kr.co.ibk.config.EncryptConfig;
import kr.co.ibk.domain.web.MemberInfo;
import kr.co.ibk.domain.web.RoleInfo;
import kr.co.ibk.model.paging.PaginationInfo;
import kr.co.ibk.repository.AdminMemberRepository;
import kr.co.ibk.repository.MemberRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AdminUserService extends _BaseService{
	// 초기화 및 아이디 생성시 초기 비밀번호 설정 ibk!1234 -> Kz$1d9ah@
	final String RESET_PASSWORD = "Kz$1d9ah@";
	
	private final AdminMemberRepository adminMemberRepository;
	
	private final MemberRepository memberRepository;
	
	private final EncryptConfig encryptConfig;

	
	public int getUserCount(String userId){
		return adminMemberRepository.getUserCount(userId);
	}

	
	public int getUserTotalCount(MemberInfo params){
		return adminMemberRepository.getUserTotalCount(params);
	}

	
	public List<MemberInfo> getUserList(MemberInfo params){
		List<MemberInfo> userList = Collections.emptyList();
		
		int userTotalCount = adminMemberRepository.getUserTotalCount(params);
		
		PaginationInfo paginationInfo = new PaginationInfo(params);
		paginationInfo.setTotalRecordCount(userTotalCount);
		
		params.setPaginationInfo(paginationInfo);
		
		if (userTotalCount > 0) {
			userList = adminMemberRepository.getUserList(params);
		}
			
		return userList;
	}

	
	public MemberInfo getUserInfo(String userId){
		
		// 사용자 정보 조회
		MemberInfo MemberInfo = adminMemberRepository.getUserInfo(userId);
		// 사용자의 로그인 권한 조회
		RoleInfo roleInfo = adminMemberRepository.getUserRole(userId);
		if (roleInfo != null) {
			MemberInfo.setRoleId(roleInfo.getRoleId());
		}
		
		return MemberInfo;
	}
	
	
	public HashMap<String, Object> addUser(MemberInfo params){
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
		
		userAddCnt = adminMemberRepository.addUser(params);
		
		if (userAddCnt==1L) {
			// 사용자 추가에 성공하였다면 권한 추가 실행
			// 사용자 권한 추가
			// 사용자 정보 조회
			MemberInfo MemberInfo = adminMemberRepository.getUserInfo(params.getMemId());
			params.setMemSeq(MemberInfo.getMemSeq());
			roleAddCnt = adminMemberRepository.addUserRole(params);
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

	
	public HashMap<String, Object> modUser(MemberInfo params){
		HashMap<String, Object> map = new HashMap<String, Object>();// 반환 데이터
		String status = "ERROR"; // 상태
		String msg = "에러";// 메시지
		
		// 사용자 추가 / 권한 추가
		long userModCnt = 0;
		long roleModCnt = 0;
		
		userModCnt = adminMemberRepository.modUser(params);
		
		if (userModCnt==1L) {
			// 사용자 추가에 성공하였다면 권한 추가 실행
			// 사용자 권한 추가
			roleModCnt = adminMemberRepository.modUserRole(params);
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

	
	public HashMap<String, Object> resetPassword(MemberInfo params) {
		HashMap<String, Object> map = new HashMap<String, Object>();// 반환 데이터
		String status = "ERROR"; // 상태
		String msg = "에러";// 메시지
		
		// 리셋
		long resetCnt = 0;
		
		// 사용자 비밀번호 초기화시 RESET_PASSWORD 로 초기화 > 추후 암호화하는 변경작업 있어야 함
		
		String newPwd = PasswordGenerator.generatePassword(10);
		
		String encPwd = encryptConfig.passwordEncoder("bcrypt").encode(newPwd);
		params.setMemPwd(encPwd);
		
		resetCnt = adminMemberRepository.setUserPassword(params);
		
		if (resetCnt==1L ) {
			status = "SUCCESS";
			msg = "비밀번호가 [ "+ newPwd + " ] 으로 초기화 되었습니다.";
			memberRepository.resetPasswordFailCnt(params);
		} else if (resetCnt != 1L) {
			status = "FAIL";
			msg = "패스워드 초기화를 실패하였습니다.";
		}  

		map.put("status", status);
		map.put("msg", msg);
		
		return map;
	}
	
	public Map<String,String> setSuperAdmin(Map<String,String> paramMap) {
		Map<String, String> map = new HashMap<String, String>();// 반환 데이터
		String status = "ERROR"; // 상태
		String msg = "에러";// 메시지		
		
		// 설정 해제시 - 마지막 사용자는 해제 못하게
		if(!"SUPER".equals(paramMap.get("auth"))) {
			long result = memberRepository.countSuperAdmin();
			
			if (result == 1L) {
				status = "FAIL";
				msg = "마지막 SUPER ADMIN 권한자는 해제 할수 없습니다.";
				
				map.put("status", status);
				map.put("msg", msg);
				
				return map;
			}
		}
		
		// superAdmin 설정/해제
		long result = memberRepository.setSuperAdmin(paramMap);
		
		if (result==1L ) {
			status = "SUCCESS";
			msg = "정상적으로 설정 되었습니다.";
		} else if (result != 1L) {
			status = "FAIL";
			msg = "SUPER ADMIN 설정에 실패하였습니다.";
		}  
		
		map.put("status", status);
		map.put("msg", msg);
		
		return map;
	}

	
	public HashMap<String, Object> deleteUser(MemberInfo params) {
		HashMap<String, Object> map = new HashMap<String, Object>();// 반환 데이터
		String status = "ERROR"; // 상태
		String msg = "에러";// 메시지
		
		// 삭제 카운트
		long deleteCnt = 0;
		
		// 상태값 삭제
		params.setMemStat("delete");
		
		deleteCnt = adminMemberRepository.deleteUser(params);
		
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
