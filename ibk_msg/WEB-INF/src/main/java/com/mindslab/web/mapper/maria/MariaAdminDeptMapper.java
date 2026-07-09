package com.mindslab.web.mapper.maria;

import java.util.List;

import com.mindslab.web.common.support.utils.CustomMap;
import com.mindslab.web.paging.Criteria;
import com.mindslab.web.vo.DeptVO;

import org.springframework.stereotype.Repository;

@Repository
public interface MariaAdminDeptMapper {

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
     * 부서코드 카운트 조회
     * 
     * @param DeptVO params
     * @return
     * @throws Exception
     */
    public int getDeptCodeCount(DeptVO params);
    
    /**
     * 부서정보 추가
     * 
     * @param DeptVO params
     * @return
     * @throws Exception
     */
    public int addDept(DeptVO params);
    
    /**
     * 부서정보 수정
     * 
     * @param DeptVO params
     * @return
     * @throws Exception
     */
    public int modDept(DeptVO params);

    /**
     * 부서 삭제
     * 
     * @param DeptVO params
     * @return
     * @throws Exception
     */
    public int deleteDept(DeptVO params);

    /**
     * 상위부서 ID로 사용중인지 확인 
     * 
     * @param DeptVO params
     * @return
     * @throws Exception
     */
    public int getParDeptCount(DeptVO params);
    
    
}
