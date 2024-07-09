package com.mindslab.web.vo;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.mindslab.web.common.support.utils.CustomMap;
import com.mindslab.web.common.support.utils.StringUtil;
import com.mindslab.web.paging.PageDTO;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
public class MemberVO extends PageDTO implements Serializable{

    /* HC_MEMBER */
    private String memSeq; // '회원일련번호'
    private String deptId; // '부서아이디'
    private String memId; // '회원아이디'
    private String memName; // '회원명'
    private String memType; // 'OAUTH 연동시 사용'
    private String memStat; // 'NORMAL, REST, STOP, DELETE 등 상태'
    private String memBefoPwd; // '이전 비밀번호'
    private String memPwd; // '현재 비밀번호'
    private String memGender; // 'M: 남자, W:여자'
    private String memBirthday; // 'YYYYMMDD 8자리'
    private String memPhone; // '010-0000-0000 13자리'
    private String memRank; // '직급'
    private String memZipcode; // '우편번호'
    private String memAddr; // '주소'
    private String memAddrDetail; // '상세주소'
    private String memEmail; // '이메일'
    private String memRegId; // '등록자'
    private Date memRegDt; // '등록일시'
    private String memModId; // '변경자'
    private Date memModDt; // '변경일시'
    private Date memLstDt; // '마지막접속일자'
    private int memFailCnt; // '비밀번호실패횟수'
    private Date memFailDt; // '비밀번호실패일자'
    private String memColumn1; // 커스텀컬럼
    private String memColumn2; // 커스텀컬럼
    private String memColumn3; // 커스텀컬럼
    private String memColumn4; // 커스텀컬럼
    private String memColumn5; // 커스텀컬럼
    
    // 조회
    private String roleId; // 권한 ID
	private String deptCode; //  '부서코드'
	private String deptName; //  '부서명'
	private String deptEngName; //  '부서명(영문)'
    
    private List<RoleVO> roleList; // 역할(권한) 목록
    
    @Override
    public String toString() {
    	CustomMap map = new CustomMap();
    	map.put("deptId", StringUtil.nvl(this.deptId,""));
    	map.put("memId", StringUtil.nvl(this.memId,""));
    	map.put("memName", StringUtil.nvl(this.memName,""));
    	map.put("memType", StringUtil.nvl(this.memType,""));
    	map.put("memStat", StringUtil.nvl(this.memStat,""));
    	map.put("memRank", StringUtil.nvl(this.memRank,""));
    	map.put("deptCode", StringUtil.nvl(this.deptCode,""));
    	map.put("deptName", StringUtil.nvl(this.deptName,""));
    	
    	if (roleList != null) {
        	for(int i=0; i < this.roleList.size(); i++) {
        		RoleVO role = this.roleList.get(i);
        		map.put("roleId_"+Integer.toString(i), StringUtil.nvl(role.getRoleId(),""));
        		map.put("roleType_"+Integer.toString(i), StringUtil.nvl(role.getRoleType(),""));
        		map.put("roleName_"+Integer.toString(i), StringUtil.nvl(role.getRoleName(),""));
        	}	
    	}
    	return map.toString();
    }

}
