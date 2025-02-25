package kr.co.ibk.service;

import kr.co.ibk.domain.web.DepTreetInfo;
import kr.co.ibk.model.DeptForm;
import kr.co.ibk.model.paging.PaginationInfo;
import kr.co.ibk.repository.AdminDeptRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AdminDeptService extends _BaseService {

    private final AdminDeptRepository adminDeptRepository;

    public List<DepTreetInfo> getDeptTree() {
        // TODO Auto-generated method stub
        List<DepTreetInfo> result = new ArrayList();
        log.info("AdminDeptServiceImpl.getDeptTree() Start");
        result = adminDeptRepository.getDeptTree();
        log.info("AdminDeptServiceImpl.getDeptTree() End");
        return result;
    }


    public int getDeptTotalCount(DeptForm params) {
        return adminDeptRepository.getDeptTotalCount(params);
    }


    public List<DeptForm> getDeptList(DeptForm params) {

        List<DeptForm> deptList = Collections.emptyList();

        int deptTotalCount = adminDeptRepository.getDeptTotalCount(params);

        PaginationInfo paginationInfo = new PaginationInfo(params);
        paginationInfo.setTotalRecordCount(deptTotalCount);

        params.setPaginationInfo(paginationInfo);

        if (deptTotalCount > 0) {
            deptList = adminDeptRepository.getDeptList(params);
        }
        return deptList;
    }


    public DeptForm getDeptInfo(DeptForm params) {
        String deptId = params.getDeptId();

        if (deptId == null) {
            params.setDeptId("system");
        }

        DeptForm result = adminDeptRepository.getDeptInfo(params);
        return result;
    }


    public HashMap<String, Object> addDept(DeptForm params) {
        HashMap<String, Object> result = new HashMap<String, Object>();
        String status = "ERROR";
        String msg = "에러";

        // params
        int codeCnt = adminDeptRepository.getDeptCodeCount(params);
        if (codeCnt > 0) {
            status = "EXIST";
            msg = "이미 사용중인 부서코드 입니다.";
        } else {
            int modCnt = adminDeptRepository.addDept(params);
            if (modCnt > 0) {
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


    public HashMap<String, Object> modDept(DeptForm params) {
        HashMap<String, Object> result = new HashMap<String, Object>();
        String status = "ERROR";
        String msg = "에러";

        // params
        int codeCnt = adminDeptRepository.getDeptCodeCount(params);
        if (codeCnt > 0) {
            status = "EXIST";
            msg = "이미 사용중인 코드 입니다.";
        } else {
            int modCnt = adminDeptRepository.modDept(params);
            if (modCnt > 0) {
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


    public HashMap<String, Object> deleteDept(DeptForm params) {
        HashMap<String, Object> map = new HashMap<String, Object>();// 반환 데이터
        String status = "ERROR"; // 상태
        String msg = "에러";// 메시지

        // 삭제 카운트
        int deleteCnt = 0;

        // 해당 부서를 상위로 사용하고있는 부서의 수
        int parCnt = adminDeptRepository.getParDeptCount(params);
        int useCnt = adminDeptRepository.checkDeptId(params);

        if (parCnt > 0) {
            status = "PARDEPT";
            msg = "하위 부서가 존재하여 삭제할 수 없습니다.";
        } else if (useCnt > 0) {
            status = "PARDEPT";
            msg = "부서를 사용중인 사용자가 있습니다. <br> 사용자를 타 부서로 이동하거나 삭제 후 다시 시도해주세요.";
        } else {
            // 상태값 삭제
            params.setDeptStat("delete");

            deleteCnt = adminDeptRepository.deleteDept(params);

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
