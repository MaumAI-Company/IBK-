package com.mindslab.web.admin.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mindslab.web.common.support.utils.CustomMap;
import com.mindslab.web.mapper.maria.MariaAdminDeptMapper;
import com.mindslab.web.paging.PaginationInfo;
import com.mindslab.web.vo.DeptVO;
import com.mindslab.web.vo.MemberVO;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class AdminDeptServiceImpl implements AdminDeptService {
	
	@Autowired
	private MariaAdminDeptMapper mariaAdminDeptMapper;

	@Override
	public List<CustomMap> getDeptTree() {
		// TODO Auto-generated method stub
		List<CustomMap> result = new ArrayList();
		log.info("AdminDeptServiceImpl.getDeptTree() Start");
		result = mariaAdminDeptMapper.getDeptTree();
		log.info("AdminDeptServiceImpl.getDeptTree() End");
		return result;
	}

	@Override
	public int getDeptTotalCount(DeptVO params) {
		return mariaAdminDeptMapper.getDeptTotalCount(params);
	}

	@Override
	public List<DeptVO> getDeptList(DeptVO params) {

		List<DeptVO> deptList = Collections.emptyList();
		
		int deptTotalCount = mariaAdminDeptMapper.getDeptTotalCount(params);
		
		PaginationInfo paginationInfo = new PaginationInfo(params);
		paginationInfo.setTotalRecordCount(deptTotalCount);
		
		params.setPaginationInfo(paginationInfo);
		
		if (deptTotalCount > 0) {
			deptList = mariaAdminDeptMapper.getDeptList(params);
		}
		return deptList;
	}

	@Override
	public DeptVO getDeptInfo(DeptVO params) {
		String deptId = params.getDeptId();
		
		if (deptId == null) {
			params.setDeptId("system");
		}
		
		DeptVO result = mariaAdminDeptMapper.getDeptInfo(params); 
		return result;
	}

	@Override
	public HashMap<String, Object> addDept(DeptVO params) {
		HashMap<String, Object> result = new HashMap<String, Object>();
		String status="ERROR";
		String msg="에러";
		
		// params
		int codeCnt = mariaAdminDeptMapper.getDeptCodeCount(params); 
		if (codeCnt > 0) {
			status = "EXIST";
			msg = "이미 사용중인 부서코드 입니다.";
		} else {
			int modCnt = mariaAdminDeptMapper.addDept(params);
			if (modCnt >0) {
				status = "SUCCESS";
				msg = "성공적으로 추가되었습니다.";
			} else {
				status = "FAIL";
				msg = "추가 실패하였습니다.";
			}
		}

		result.put("status", status);
		result.put("msg", msg);
		
		return result;
	}

	@Override
	public HashMap<String, Object> modDept(DeptVO params) {
		HashMap<String, Object> result = new HashMap<String, Object>();
		String status="ERROR";
		String msg="에러";
		
		// params
		int codeCnt = mariaAdminDeptMapper.getDeptCodeCount(params); 
		if (codeCnt > 0) {
			status = "EXIST";
			msg = "이미 사용중인 코드 입니다.";
		} else {
			int modCnt = mariaAdminDeptMapper.modDept(params);
			if (modCnt >0) {
				status = "SUCCESS";
				msg = "성공적으로 수정되었습니다.";
			} else {
				status = "FAIL";
				msg = "수정 실패하였습니다.";
			}
		}

		result.put("status", status);
		result.put("msg", msg);
		
		return result;
	}

	@Override
	public HashMap<String, Object> deleteDept(DeptVO params) {
		HashMap<String, Object> map = new HashMap<String, Object>();// 반환 데이터
		String status = "ERROR"; // 상태
		String msg = "에러";// 메시지
		
		// 삭제 카운트
		int deleteCnt = 0;
		
		// 해당 부서를 상위로 사용하고있는 부서의 수
		int parCnt = mariaAdminDeptMapper.getParDeptCount(params); 
		
		if(parCnt > 0) {
			status = "PARDEPT";
			msg = "하위 부서가 존재하여 삭제할 수 없습니다.";
		} else {
			// 상태값 삭제
			params.setDeptStat("delete");
			
			deleteCnt = mariaAdminDeptMapper.deleteDept(params);
			
			if (deleteCnt >= 1) {
				status = "SUCCESS";
				msg = "정상적으로 삭제되었습니다.";
			} else if (deleteCnt != 1) {
				status = "FAIL";
				msg = "삭제를 실패하였습니다.";
			}  
		}

		map.put("status", status);
		map.put("msg", msg);
		
		return map;
	}
	
}
