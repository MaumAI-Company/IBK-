package kr.co.ibk.repository;

import java.util.List;

import org.springframework.stereotype.Repository;

import kr.co.ibk.common.utils.CustomMap;
import kr.co.ibk.domain.web.DepTreetInfo;
import kr.co.ibk.model.DeptForm;

@Repository
public interface AdminDeptRepository {
    /**
     * 부서트리 조회
     * 
     * @param null
     * @return
     * @throws Exception
     */
    public List<DepTreetInfo> getDeptTree();

    /**
     * 부서 카운트 조회
     * 
     * @param DeptForm params
     * @return
     * @throws Exception
     */
    public int getDeptTotalCount(DeptForm params);
    /**
     * 부서목록 조회
     * 
     * @param DeptForm params
     * @return
     * @throws Exception
     */
    public List<DeptForm> getDeptList(DeptForm params);

    /**
     * 부서정보 조회
     * 
     * @param DeptForm params
     * @return
     * @throws Exception
     */
    public DeptForm getDeptInfo(DeptForm params);
    
    /**
     * 부서코드 카운트 조회
     * 
     * @param DeptForm params
     * @return
     * @throws Exception
     */
    public int getDeptCodeCount(DeptForm params);
    
    /**
     * 부서정보 추가
     * 
     * @param DeptForm params
     * @return
     * @throws Exception
     */
    public int addDept(DeptForm params);
    
    /**
     * 부서정보 수정
     * 
     * @param DeptForm params
     * @return
     * @throws Exception
     */
    public int modDept(DeptForm params);

    /**
     * 부서 삭제
     * 
     * @param DeptForm params
     * @return
     * @throws Exception
     */
    public int deleteDept(DeptForm params);

    /**
     * 상위부서 ID로 사용중인지 확인 
     * 
     * @param DeptForm params
     * @return
     * @throws Exception
     */
    public int getParDeptCount(DeptForm params);
}
