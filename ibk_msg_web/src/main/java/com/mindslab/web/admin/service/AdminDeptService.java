package com.mindslab.web.admin.service;

import java.util.HashMap;
import java.util.List;

import com.mindslab.web.common.support.utils.CustomMap;
import com.mindslab.web.vo.DeptVO;

public interface AdminDeptService {
	
    /**
     * 부서트리 조회
     * 
     * @param null
     * @return
     * @throws Exception
     */
    public List<CustomMap> getDeptTree();

    /**
     * 부서 카운트 조회
     * 
     * @param DeptVO params
     * @return
     * @throws Exception
     */
    public int getDeptTotalCount(DeptVO params);
    /**
     * 부서목록 조회
     * 
     * @param DeptVO params
     * @return
     * @throws Exception
     */
    public List<DeptVO> getDeptList(DeptVO params);

    /**
     * 부서정보 조회
     * 
     * @param DeptVO params
     * @return
     * @throws Exception
     */
    public DeptVO getDeptInfo(DeptVO params);
    
    /**
     * 부서정보 추가
     * 
     * @param DeptVO params
     * @return
     * @throws Exception
     */
    public HashMap<String, Object> addDept(DeptVO params);
    
    /**
     * 부서정보 수정
     * 
     * @param DeptVO params
     * @return
     * @throws Exception
     */
    public HashMap<String, Object> modDept(DeptVO params);
    
    /**
     * 부서정보 수정
     * 
     * @param DeptVO params
     * @return
     * @throws Exception
     */
    public HashMap<String, Object> deleteDept(DeptVO params);
    
}
