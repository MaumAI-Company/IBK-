package com.mindslab.web.admin.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mindslab.web.admin.service.AdminAuthManagementService;
import com.mindslab.web.admin.service.AdminDeptService;
import com.mindslab.web.admin.service.AdminDetectLevelService;
import com.mindslab.web.admin.service.AdminMenuManagementService;
import com.mindslab.web.admin.service.AdminUserService;
import com.mindslab.web.auth.UserPrincipal;
import com.mindslab.web.common.controller.CommonController;
import com.mindslab.web.common.support.utils.CustomMap;
import com.mindslab.web.user.service.MsgService;
import com.mindslab.web.vo.DeptVO;
import com.mindslab.web.vo.DetectionLevelVO;
import com.mindslab.web.vo.MemberVO;
import com.mindslab.web.vo.MenuAuthMemberVO;
import com.mindslab.web.vo.MsgVO;

import lombok.extern.slf4j.Slf4j;


@Controller
@Slf4j
@RequestMapping("/admin")
public class AdminController extends CommonController {
	
	@Autowired
	private AdminUserService adminUserService; 
	
	@Autowired
	private AdminDeptService adminDeptService;

	@Autowired
	private AdminDetectLevelService adminDetectLevelService;

	@Autowired
	private MsgService msgService;
	
	//메뉴관리 서비스
	@Autowired
	private AdminMenuManagementService adminMenuManagementService;	
		
	//메뉴권한관리 서비스
	@Autowired
	private AdminAuthManagementService adminAuthManagementService;
	
    
	@RequestMapping(value = {"", "/", "home"})
    public ModelAndView admin(Authentication authentication, MsgVO params) {
        log.info("##### URI :: { /admin, /admin/, /admin/home } ######");

        List<MsgVO> msgList = msgService.getMsgList(params);
        
        // 로그인된 시큐어 세션의 memberVO 정보
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        MemberVO sessionMember = userPrincipal.getMember();
        
        ModelAndView mav = new ModelAndView();
        mav.addObject("sessionMember", sessionMember);
        mav.addObject("roles", userPrincipal.getAuthorities());
        mav.addObject("msgList", msgList); // 사용자 목록
        mav.addObject("params", params); // 사용자페이지 관련 정보 / 페이징 정보
        mav.setViewName("redirect:/user/pre_verification");
        return mav;
    }

	@RequestMapping(value = {"department"})
    public ModelAndView department(Authentication authentication, DeptVO params) {
        log.info("##### URI :: { /admin/department } #####");
        
        // 로그인된 시큐어 세션의 memberVO 정보
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        MemberVO sessionMember = userPrincipal.getMember();
        
    	List<CustomMap> result = new ArrayList();
    	CustomMap param = new CustomMap();    

    	result = adminDeptService.getDeptTree();
        JSONArray arr = new JSONArray(result);
        
        ModelAndView mav = new ModelAndView();
        mav.addObject("sessionMember", sessionMember);
        mav.addObject("roles", userPrincipal.getAuthorities());
        //메뉴트리목록을 가져온다.
        mav.addObject("result", arr);
        mav.setViewName("/admin/department");
        
        log.info("result :: {}",result.toString());
        log.info("access Url : /admin/department, target Method : department() End View department");
        return mav;
    }
	@PostMapping(value = {"department/mod"})
	@ResponseBody
    public HashMap<String, Object> departmentMod(DeptVO params) {
        log.info("##### URI :: { /admin/department/mod } #####");
        log.info("##### params :: "+ params + " #####");
        
        // user 관련
        HashMap<String, Object> map = adminDeptService.modDept(params);
        
        return map;
    }
	@PostMapping(value = {"department/add"})
	@ResponseBody
    public HashMap<String, Object> departmentAdd(DeptVO params) {
        log.info("##### URI :: { /admin/department/add } #####");
        log.info("##### params :: "+ params + " #####");
        
        // user 관련
        HashMap<String, Object> map = adminDeptService.addDept(params);
        
        return map;
    }
	@PostMapping(value = {"department/delete"})
	@ResponseBody
    public HashMap<String, Object> departmentDelete(DeptVO params) {
        log.info("##### URI :: { /admin/department/delete } #####");
        log.info("##### params :: "+ params + " #####");
        
        // user 관련
        HashMap<String, Object> map = adminDeptService.deleteDept(params);
        
        return map;
    }

	@RequestMapping(value = {"user"})
    public ModelAndView user(Authentication authentication, MemberVO params) {
        log.info("##### URI :: { /admin/user } #####");
        log.info("##### params :: "+ params + " #####");
        
        // user 관련
        List<MemberVO> userList = adminUserService.getUserList(params);
        
        
        // 로그인된 시큐어 세션의 memberVO 정보
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        MemberVO sessionMember = userPrincipal.getMember();
        
        ModelAndView mav = new ModelAndView();
        mav.addObject("sessionMember", sessionMember);
        mav.addObject("roles", userPrincipal.getAuthorities());
        mav.addObject("userList", userList); // 사용자 목록
        mav.addObject("params", params); // 사용자페이지 관련 정보 / 페이징 정보
        mav.setViewName("/admin/user");
        return mav;
    }
	@PostMapping(value = {"user/add"})
	@ResponseBody
    public HashMap<String, Object> userAdd(MemberVO params) {
        log.info("##### URI :: { /admin/user/add } #####");
        log.info("##### params :: "+ params + " #####");
        
        // user 관련
        HashMap<String, Object> map = adminUserService.addUser(params);
        
        return map;
    }
	@PostMapping(value = {"user/mod"})
	@ResponseBody
    public HashMap<String, Object> userMod(MemberVO params) {
        log.info("##### URI :: { /admin/user/mod } #####");
        log.info("##### params :: "+ params + " #####");
        
        // user 관련
        HashMap<String, Object> map = adminUserService.modUser(params);
        
        return map;
    }

	@PostMapping(value = {"user/checkUserId"})
	@ResponseBody
    public HashMap<String, Object> userCheckUserId(String userId) {
        log.info("##### URI :: { /admin/user/checkUserId } #####");
        log.info("##### userId :: "+ userId + " #####");
        HashMap<String, Object> map = new HashMap<String, Object>();
        
        // user 관련
        map.put("cnt", adminUserService.getUserCount(userId));
        
        return map;
    }
	
	@PostMapping(value = {"user/getUserInfo"})
	@ResponseBody
    public HashMap<String, Object> userGetUserInfo(String userId) {
        log.info("##### URI :: { /admin/user/getUserInfo } #####");
        log.info("##### userId :: "+ userId + " #####");
        HashMap<String, Object> map = new HashMap<String, Object>();
        
        // user 관련
        map.put("userInfo", adminUserService.getUserInfo(userId));
        
        return map;
    }
	
	@PostMapping(value = {"user/getDeptList"})
	@ResponseBody
    public HashMap<String, Object> userGetDeptList(DeptVO params) {
        log.info("##### URI :: { /admin/user/getDeptList } #####");
        log.info("##### params :: "+ params + " #####");
        HashMap<String, Object> map = new HashMap<String, Object>();
        
        // 부서
        List<DeptVO> deptList = adminDeptService.getDeptList(params);
        
        // 리턴
        map.put("deptList", deptList);
        
        // 페이지 정보
        map.put("currentPageNo", params.getCurrentPageNo());
        map.put("recordsPerPage",params.getRecordsPerPage());
        map.put("pageSize",params.getPageSize());
        map.put("hasPreviousPage", params.getPaginationInfo().isHasPreviousPage());
        map.put("firstPage", params.getPaginationInfo().getFirstPage());
        map.put("hasNextPage", params.getPaginationInfo().isHasNextPage());
        map.put("lastPage", params.getPaginationInfo().getLastPage());
        map.put("totalPageCount", params.getPaginationInfo().getTotalPageCount());

        // 검색 정보
        map.put("searchType", params.getSearchType());
        map.put("searchKeyword", params.getSearchKeyword());

        return map;
    }
	
	@PostMapping(value = {"user/resetPassword"})
	@ResponseBody
    public HashMap<String, Object> userResetPassword(MemberVO params) {
        log.info("##### URI :: { /admin/user/resetPassword } #####");
        log.info("##### params :: "+ params + " #####");
        
        // user 관련
        HashMap<String, Object> map = adminUserService.resetPassword(params);
        
        return map;
    }

	@PostMapping(value = {"user/delete"})
	@ResponseBody
    public HashMap<String, Object> userDelete(MemberVO params) {
        log.info("##### URI :: { /admin/user/delete } #####");
        log.info("##### params :: "+ params + " #####");
        
        // user 관련
        HashMap<String, Object> map = adminUserService.deleteUser(params);
        
        return map;
    }
	
	/**
	 * admin 메뉴 관리 목록
	 * @param req
	 * @param reqParam
	 * @param userPrincipal
	 * @return
	 */
	@RequestMapping(value = {"menu/main"})
    public ModelAndView manuManagementMain(HttpServletRequest req, @AuthenticationPrincipal UserPrincipal userPrincipal) {        
    	log.info("access Url : /menu/main, target Method : manuManagementMain()");
    	
    	MemberVO sessionMember = userPrincipal.getMember();    
    	List<CustomMap> resultMenuTree = new ArrayList();
    	List<CustomMap> resultMenuTreeOrderCount = new ArrayList();
    	//메뉴트리목록을 가져온다.
    	resultMenuTree = adminMenuManagementService.getMenuTree();
    	//메뉴트리목록 건수
    	resultMenuTreeOrderCount = adminMenuManagementService.getMenuTreeOrderCount();
    	
        ModelAndView mav = new ModelAndView();
        mav.addObject("sessionMember", sessionMember);
        mav.addObject("roles", userPrincipal.getAuthorities());
        //메뉴 목록 트리를 json 데이터로 가공한다.
        JSONArray menuTree = new JSONArray(resultMenuTree);
        mav.addObject("menuTree", menuTree);
        
        //메뉴목록 최대 카운트
        JSONArray menuTreeOrderCount = new JSONArray(resultMenuTreeOrderCount);
        mav.addObject("menuTreeOrderCount",menuTreeOrderCount);
        mav.setViewName("/admin/menuMainPage");
        log.info("access Url : /menu/main, target Method : manuManagementMain() End View menuMainPage");
        return mav;
    }
	
	/**
	 * 메뉴 추가 처리
	 * @param req
	 * @param userPrincipal
	 * @param requestParamMap
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = {"menu/addMenu"},method = RequestMethod.POST)	
    public HashMap<String, Object> manuManagementAddMenu(HttpServletRequest req, @AuthenticationPrincipal UserPrincipal userPrincipal, @RequestBody HashMap<String, Object> requestParamMap) {        
    	log.info("access Url : /menu/addMenu, target Method : manuManagementAddMenu()");
    	
    	MemberVO sessionMember = userPrincipal.getMember();
    	//request param을 담는다.
    	CustomMap param = new CustomMap();
    	param.putAll(requestParamMap);
    	param.orginPut("menuRegId", sessionMember.getMemId());
    	param.orginPut("menuModId", sessionMember.getMemId());
    	
    	adminMenuManagementService.insertHcMenuTree(param);
    	adminMenuManagementService.updateHcMenuDepthLevel();
    	
    	HashMap<String, Object> result = new HashMap<String, Object>();
        
    	List<CustomMap> resultMenuTree = new ArrayList();
    	List<CustomMap> resultMenuTreeOrderCount = new ArrayList();
    	//메뉴트리목록을 가져온다.
    	resultMenuTree = adminMenuManagementService.getMenuTree();
    	resultMenuTreeOrderCount = adminMenuManagementService.getMenuTreeOrderCount();    	
		result.put("menuTree", resultMenuTree);
		result.put("menuTreeOrderCount",resultMenuTreeOrderCount);
        log.info("access Url : /menu/main, target Method : manuManagementMain() End View menuMainPage");
        return result;
    }
	
	/**
	 * 메뉴 수정 처리
	 * @param req
	 * @param userPrincipal
	 * @param requestParamMap
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = {"menu/updateMenu"},method = RequestMethod.POST)	
    public HashMap<String, Object> manuManagementUpdateMenu(HttpServletRequest req, @AuthenticationPrincipal UserPrincipal userPrincipal, @RequestBody HashMap<String, Object> requestParamMap) {        
    	log.info("access Url : /menu/addMenu, target Method : manuManagementUpdateMenu()");
    	
    	MemberVO sessionMember = userPrincipal.getMember();
    	//request param을 담는다.
    	CustomMap param = new CustomMap();
    	param.putAll(requestParamMap);
    	param.orginPut("menuModId", sessionMember.getMemId());
    	
    	adminMenuManagementService.updateHcMenuTree(param);
    	adminMenuManagementService.updateHcMenuDepthLevel();
    	
    	HashMap<String, Object> result = new HashMap<String, Object>();
        
    	List<CustomMap> resultMenuTree = new ArrayList();
    	List<CustomMap> resultMenuTreeOrderCount = new ArrayList();
    	//메뉴트리목록을 가져온다.
    	resultMenuTree = adminMenuManagementService.getMenuTree();
    	resultMenuTreeOrderCount = adminMenuManagementService.getMenuTreeOrderCount();    	
		result.put("menuTree", resultMenuTree);
		result.put("menuTreeOrderCount",resultMenuTreeOrderCount);
        log.info("access Url : /menu/main, target Method : manuManagementUpdateMenu() End View menuMainPage");
        return result;
    }
	
	/**
	 * 메뉴 삭제 처리
	 * @param req
	 * @param userPrincipal
	 * @param requestParamMap
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = {"menu/deleteMenu"},method = RequestMethod.POST)	
    public HashMap<String, Object> manuManagementDeleteMenu(HttpServletRequest req, @AuthenticationPrincipal UserPrincipal userPrincipal, @RequestBody HashMap<String, Object> requestParamMap) {        
    	log.info("access Url : /menu/addMenu, target Method : manuManagementDeleteMenu()");
    	
    	MemberVO sessionMember = userPrincipal.getMember();
    	//request param을 담는다.
    	CustomMap param = new CustomMap();
    	param.putAll(requestParamMap);
    	param.orginPut("menuModId", sessionMember.getMemId());
    	
    	log.info("AdminController.manuManagementDeleteMenu() requestParamMap :: {}", requestParamMap);
    	
    	adminMenuManagementService.deleteHcMenuTree(param);
    	adminMenuManagementService.updateHcMenuDepthLevel();
    	
    	HashMap<String, Object> result = new HashMap<String, Object>();
        
    	List<CustomMap> resultMenuTree = new ArrayList();
    	List<CustomMap> resultMenuTreeOrderCount = new ArrayList();
    	//메뉴트리목록을 가져온다.
    	resultMenuTree = adminMenuManagementService.getMenuTree();
    	resultMenuTreeOrderCount = adminMenuManagementService.getMenuTreeOrderCount();    	
		result.put("menuTree", resultMenuTree);
		result.put("menuTreeOrderCount",resultMenuTreeOrderCount);
        log.info("access Url : /menu/main, target Method : manuManagementDeleteMenu() End View menuMainPage");
        return result;
    }

	/**
	 * 메뉴목록 권한 처리
	 * @param authentication
	 * @return
	 */
	@RequestMapping(value = {"auth/main"})
    public ModelAndView auth(Authentication authentication, @AuthenticationPrincipal UserPrincipal userPrincipal, MenuAuthMemberVO param) {
        log.info("##### URI :: { /admin/auth/main } #####");
        ObjectMapper mapper = new ObjectMapper();
        String menuAuthMemberVO;
		try{
			menuAuthMemberVO = mapper.writeValueAsString(param);
			log.info("authMainPage params : {}",menuAuthMemberVO);
		} catch (JsonProcessingException e){
			// TODO Auto-generated catch block
			e.printStackTrace();
		}        
        
        // 로그인된 시큐어 세션의 memberVO 정보
        MemberVO sessionMember = userPrincipal.getMember();
        
        ModelAndView mav = new ModelAndView();
        mav.addObject("sessionMember", sessionMember);
        mav.addObject("roles", userPrincipal.getAuthorities());
        mav.setViewName("/admin/authMainPage");
        
        // user 관련
        List<MenuAuthMemberVO> userList = adminAuthManagementService.getUserList(param);
        mav.addObject("userList", userList); // 사용자 목록
        mav.addObject("params", param); // 사용자페이지 관련 정보 / 페이징 정보
        
        return mav;
    }
	
	/**
	 * 권한관리 메뉴 목록 조회
	 * @param req
	 * @param userPrincipal
	 * @param requestParamMap
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = {"auth/memberMenu"},method = RequestMethod.POST)	
    public HashMap<String, Object> authManagementMemberMenu(HttpServletRequest req, @AuthenticationPrincipal UserPrincipal userPrincipal, @RequestBody HashMap<String, Object> requestParamMap) {        
    	log.info("access Url : auth/memberMenu, target Method : authManagementMemberMenu()");
    	
    	MemberVO sessionMember = userPrincipal.getMember();
    	//request param을 담는다.
    	CustomMap param = new CustomMap();
    	param.putAll(requestParamMap);
    	
    	HashMap<String, Object> result = new HashMap<String, Object>();
        
    	List<CustomMap> resultMenuTree = new ArrayList();
    	//메뉴트리목록을 가져온다.
    	resultMenuTree = adminAuthManagementService.getMenuTree(param);    	
		result.put("menuTree", resultMenuTree);
        log.info("access Url : auth/memberMenu, target Method : authManagementMemberMenu() End View menuMainPage");
        return result;
    }
	
	/**
	 * 메뉴 삭제 처리
	 * @param req
	 * @param userPrincipal
	 * @param requestParamMap
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = {"auth/insertMemberMenu"},method = RequestMethod.POST)	
    public HashMap<String, Object> authManagementInsertMemberMenu(HttpServletRequest req, @AuthenticationPrincipal UserPrincipal userPrincipal, @RequestBody HashMap<String, Object> requestParamMap) {        
    	log.info("access Url : /menu/addMenu, target Method : authManagementInsertMemberMenu()");
    	
    	MemberVO sessionMember = userPrincipal.getMember();
    	//request param을 담는다.
    	CustomMap param = new CustomMap();
    	param.putAll(requestParamMap);
    	param.orginPut("regMemId", sessionMember.getMemId());
    	
    	log.info("AdminController.authManagementInsertMemberMenu() param :: {}", param);
    	adminAuthManagementService.insertMemberMenu(param);
    	
    	HashMap<String, Object> result = new HashMap<String, Object>();
        
    	List<CustomMap> resultMenuTree = new ArrayList();
    	//메뉴트리목록을 가져온다.
    	resultMenuTree = adminAuthManagementService.getMenuTree(param);    	
		result.put("menuTree", resultMenuTree);
        log.info("access Url : /menu/main, target Method : authManagementInsertMemberMenu() End View menuMainPage");
        return result;
    }	

	@RequestMapping(value = {"fake_check"})
    public ModelAndView fakeCheck(Authentication authentication, DetectionLevelVO params) {
        log.info("##### URI :: { /admin/fake_check } #####");
        
        HashMap<String, Object> resultMap = adminDetectLevelService.getDetectionLevelList(params);
		
        // 로그인된 시큐어 세션의 memberVO 정보
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        MemberVO sessionMember = userPrincipal.getMember();
        
        ModelAndView mav = new ModelAndView();
        mav.addObject("sessionMember", sessionMember);
        mav.addObject("roles", userPrincipal.getAuthorities());
        mav.addObject("detectionLevelList", resultMap.get("detectionLevelList"));
        mav.addObject("status", resultMap.get("status"));
        mav.setViewName("/admin/fake_check");
        return mav;
    }


	@PostMapping(value = {"detection_level/change"})
	@ResponseBody
    public HashMap<String, Object> detectionLevelChange(@RequestBody HashMap<String, Object> params, HttpServletRequest request) {
        log.info("##### URI :: { /admin/detection_level/change } #####");
        log.info("##### params :: "+ params + " #####");
        
        HashMap<String, Object> map = adminDetectLevelService.setDetectionLevel(params, request);
        
        return map;
    }
	

}
 
