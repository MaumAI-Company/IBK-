package kr.co.ibk.domain.web;

import kr.co.ibk.common.utils.CustomMap;
import kr.co.ibk.common.utils.StringUtil;
import kr.co.ibk.model.paging.PageForm;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class MemberInfo extends PageForm implements Serializable{
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
	
	private List<RoleInfo> roleList; // 역할(권한) 목록
	
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
        		RoleInfo role = this.roleList.get(i);
        		map.put("roleId_"+Integer.toString(i), StringUtil.nvl(role.getRoleId(),""));
        		map.put("roleType_"+Integer.toString(i), StringUtil.nvl(role.getRoleType(),""));
        		map.put("roleName_"+Integer.toString(i), StringUtil.nvl(role.getRoleName(),""));
        	}	
    	}
    	return map.toString();
    }

    @Builder
    public MemberInfo(String memSeq, String deptId, String memId, String memName, String memType, String memStat, String memBefoPwd, String memPwd, String memGender, String memBirthday, String memPhone, String memRank, String memZipcode, String memAddr, String memAddrDetail, String memEmail, String memRegId, Date memRegDt, String memModId, Date memModDt, Date memLstDt, int memFailCnt, Date memFailDt, String memColumn1, String memColumn2, String memColumn3, String memColumn4, String memColumn5, String roleId, String deptCode, String deptName, String deptEngName, List<RoleInfo> roleList) {
        this.memSeq = memSeq;
        this.deptId = deptId;
        this.memId = memId;
        this.memName = memName;
        this.memType = memType;
        this.memStat = memStat;
        this.memBefoPwd = memBefoPwd;
        this.memPwd = memPwd;
        this.memGender = memGender;
        this.memBirthday = memBirthday;
        this.memPhone = memPhone;
        this.memRank = memRank;
        this.memZipcode = memZipcode;
        this.memAddr = memAddr;
        this.memAddrDetail = memAddrDetail;
        this.memEmail = memEmail;
        this.memRegId = memRegId;
        this.memRegDt = memRegDt;
        this.memModId = memModId;
        this.memModDt = memModDt;
        this.memLstDt = memLstDt;
        this.memFailCnt = memFailCnt;
        this.memFailDt = memFailDt;
        this.memColumn1 = memColumn1;
        this.memColumn2 = memColumn2;
        this.memColumn3 = memColumn3;
        this.memColumn4 = memColumn4;
        this.memColumn5 = memColumn5;
        this.roleId = roleId;
        this.deptCode = deptCode;
        this.deptName = deptName;
        this.deptEngName = deptEngName;
        this.roleList = roleList;
    }
}
