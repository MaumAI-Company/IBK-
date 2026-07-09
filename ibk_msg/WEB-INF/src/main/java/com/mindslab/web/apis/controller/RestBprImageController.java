package com.mindslab.web.apis.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.mindslab.web.apis.service.RestBprImageService;
import com.mindslab.web.auth.UserPrincipal;
import com.mindslab.web.common.controller.CommonController;
import com.mindslab.web.common.error.CommonErrorCode;
import com.mindslab.web.common.error.MindsLabRestException;
import com.mindslab.web.common.service.CommonService;
import com.mindslab.web.common.support.utils.CustomMap;
import com.mindslab.web.common.vo.ResponseVO;
import com.mindslab.web.vo.MemberVO;
import com.speno.apis.aConnect;
import com.speno.apis.aUsrBundle;

import lombok.extern.slf4j.Slf4j;

@RequestMapping(value = "mindslab/rest", produces = "application/json; charset=utf-8")
@RestController
@Slf4j
public class RestBprImageController extends CommonController{
	
	@Autowired
	private RestBprImageService restBprImageService;
	
    @Autowired
    protected CommonService commonService;	
	
	/**
	 * 
	 * @param request
	 * @param reqParam	: bprId(image키)
	 * @param userPrincipal
	 * @return
	 * 
	 * brp 이미지 다운로드 처리 후 서버의 imagepath로 이미지를 다운로드 한다.
	 * @throws Exception 
	 */
    @RequestMapping(value = "/bprImagePath", method = RequestMethod.POST)
    public ResponseVO<CustomMap> getBprImagePath(HttpServletRequest req,
    		@RequestBody Map<String, Object> reqParam,
			@AuthenticationPrincipal UserPrincipal userPrincipal) throws Exception {
    	log.info("access Url : /bprImagePath, target Method : getBprImagePath");
    	CustomMap result = new CustomMap();
    	MemberVO sessionMember = userPrincipal.getMember();
    	//result.orginPut("member", sessionMember);
    	CustomMap param = new CustomMap();
    	param.putAll(reqParam);
    	
    	try {
    		result = restBprImageService.getBprImagePath(param);
		} catch (MindsLabRestException e) {
			// TODO: handle exception
			result.orginPut("msg", e.getMessage());
			return super.makeResponseData(HttpStatus.INTERNAL_SERVER_ERROR, result);
		}
    	
        return super.makeResponseData(HttpStatus.OK, result);        
    }
    
    @RequestMapping(value = "/getMemberMenuTree", method = RequestMethod.POST)    
    public ResponseVO<HashMap<String, Object>> getMemberMenuTree(HttpServletRequest req,
    		@RequestBody Map<String, Object> reqParam,
			@AuthenticationPrincipal UserPrincipal userPrincipal) throws Exception {
    	log.info("access Url : auth/memberMenu, target Method : authManagementMemberMenu()");
    	
    	MemberVO sessionMember = userPrincipal.getMember();
    	//request param을 담는다.
    	CustomMap param = new CustomMap();
    	param.putAll(reqParam);
    	param.orginPut("seq",sessionMember.getMemSeq());
    	param.orginPut("memId",sessionMember.getMemId());
    	param.orginPut("roleId",sessionMember.getRoleList().get(0).getRoleId().replace("ROLE_", ""));
    	
    	HashMap<String, Object> result = new HashMap<String, Object>();
        
    	List<CustomMap> resultMenuTree = new ArrayList();
    	//메뉴트리목록을 가져온다.
    	resultMenuTree = commonService.getMenuTree(param);    	
		result.put("menuTree", resultMenuTree);
        log.info("access Url : auth/memberMenu, target Method : authManagementMemberMenu() End View menuMainPage");
        return super.makeResponseData(HttpStatus.OK, result);
    }
}
