package kr.co.ibk.web.controller;

import kr.co.ibk.common.annotation.CurrentUser;
import kr.co.ibk.common.utils.CustomMap;
import kr.co.ibk.domain.web.MemberInfo;
import kr.co.ibk.service.CommonService;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.ArrayList;
import java.util.List;

@ControllerAdvice
public class GlobalControllerAdvice {

	private final CommonService commonService;
	
	public GlobalControllerAdvice(CommonService commonService) {
		this.commonService = commonService;
	}
	
    @ModelAttribute("leftMenuList")
    public List<CustomMap> addGlobalData(@CurrentUser MemberInfo memberInfo) {
    	
    	CustomMap param = new CustomMap();
    	List<CustomMap> menuTree = new ArrayList();
		List<CustomMap> newTree = new ArrayList<>();
    	
    	if (memberInfo != null) {
    		param.orginPut("seq", memberInfo.getMemSeq());
    		param.orginPut("memId", memberInfo.getMemId());
    		param.orginPut("roleId", memberInfo.getRoleId());
    	
    		//메뉴트리목록을 가져온다.
    		menuTree = commonService.getMenuTree(param);

			if (menuTree != null && !menuTree.isEmpty()) {
				for (CustomMap customMap : menuTree) {
					if ("system".equals(customMap.getString("parId"))) {
						newTree.add(customMap);
						List<CustomMap> results = makeCustomList(menuTree, customMap);
						customMap.put("children", results);
					}
				}
			}
    	}
    	
    	return newTree;
    }

	private List<CustomMap> makeCustomList(List<CustomMap> menuTree, CustomMap parent) {
		List<CustomMap> result = new ArrayList<>();
        for (CustomMap customMap : menuTree) {
            if (parent.getString("menuId").equals(customMap.getString("parId"))) {
                customMap.put("children", makeCustomList(menuTree, customMap));
                result.add(customMap);
            }
        }
        return result;
	}
}
