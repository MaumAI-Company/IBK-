package com.mindslab.web.admin.service;

import java.util.List;

import com.mindslab.web.common.support.utils.CustomMap;
import com.mindslab.web.vo.DeptVO;

public interface AdminMenuManagementService {
	
	/**
	 * 메뉴목록 조회
	 * @return
	 */
    public List<CustomMap> getMenuTree();
    
    /**
     * 목록에 사용되는 최대 order count
     * @return
     */
    public List<CustomMap> getMenuTreeOrderCount();
    
    /**
     * 메뉴 depth레벨을 현행 시스템에 맞춰 업데이트
     */
    public void updateHcMenuDepthLevel();
    
    /**
     * 메뉴를 추가한다.
     */
    public void insertHcMenuTree(CustomMap param);
    
    /**
     * 메뉴를 수정한다.
     * @param param
     */
    public void updateHcMenuTree(CustomMap param);
    
    /**
     * 메뉴 삭제
     * @param param
     */
    public void deleteHcMenuTree(CustomMap param);
}
