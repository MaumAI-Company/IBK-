package kr.co.ibk.repository;

import java.util.List;

import org.springframework.stereotype.Repository;

import kr.co.ibk.common.utils.CustomMap;



@Repository
public interface AdminMenuManagementRepository {
	public List<CustomMap> getMenuTree();
	
    /**
     * 목록에 사용되는 최대 order count
     * @return
     */
    public List<CustomMap> getMenuTreeOrderCount();
    
    /**
     * 메뉴 depth레벨을 현행 시스템에 맞춰 업데이트
     */
    public int updateHcMenuDepthLevel();
    
    /**
     * 메뉴를 신규 등로처리
     * @return
     */
    public int insertHcMenuTree(CustomMap param);
    
    /**
     * 메뉴를 수정한다.
     * @param param
     * @return
     */
    public int updateHcMenuTree(CustomMap param);    
    
    /**
     * 메뉴를 삭제한다.
     * @param param
     * @return
     */
    public int deleteHcMenuTree(CustomMap param);

}
