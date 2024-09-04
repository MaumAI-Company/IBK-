package kr.co.ibk.web.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import kr.co.ibk.common.annotation.CurrentUser;
import kr.co.ibk.common.utils.CustomMap;
import kr.co.ibk.domain.web.MemberInfo;
import kr.co.ibk.service.CommonService;

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
    	
    	if (memberInfo != null) {
    		param.orginPut("seq", memberInfo.getMemSeq());
    		param.orginPut("memId", memberInfo.getMemId());
    		param.orginPut("roleId", memberInfo.getRoleId());
    	
    		//메뉴트리목록을 가져온다.
    		menuTree = commonService.getMenuTree(param);
    	}
    	
    	return menuTree;
    }
}
