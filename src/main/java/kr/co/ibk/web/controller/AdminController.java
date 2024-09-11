package kr.co.ibk.web.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import kr.co.ibk.common.annotation.CurrentUser;
import kr.co.ibk.common.utils.CustomMap;
import kr.co.ibk.domain.web.DepTreetInfo;
import kr.co.ibk.domain.web.DetectionLevel;
import kr.co.ibk.domain.web.MemberInfo;
import kr.co.ibk.domain.web.MenuAuthMember;
import kr.co.ibk.model.DeptForm;
import kr.co.ibk.service.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;



@Controller
@Slf4j
@RequiredArgsConstructor
public class AdminController {
	private final AdminDeptService adminDeptService;
	private final AdminUserService adminUserService;
	private final AdminAuthManagementService adminAuthManagementService;
	private final AdminMenuManagementService adminMenuManagementService;
	private final AdminDetectLevelService adminDetectLevelService;
	
    @RequestMapping( "/soulGod/admin/department")
    public String department(Model model,
                        @CurrentUser MemberInfo memberInfo) {

    	log.info("##### URI :: { /admin/department } #####");
    	
    	List<DepTreetInfo> result = new ArrayList();
    	CustomMap param = new CustomMap();    

    	result = adminDeptService.getDeptTree();
        
        
        model.addAttribute("mc", "ico_manage");
        model.addAttribute("pageTitle", "부서 관리");

        model.addAttribute("sessionMember", memberInfo);
        model.addAttribute("roles", "");
        model.addAttribute("result", result);

        log.info(result.toString());
        return "/soulGod/admin/department";

    }

    @RequestMapping( "/soulGod/admin/user")
    public String user(Model model,
                        @CurrentUser MemberInfo memberInfo, MemberInfo nowMember) {

        model.addAttribute("mc", "ico_manage");
        model.addAttribute("pageTitle", "사용자 관리");
        
        // user 관련
        List<MemberInfo> userList = adminUserService.getUserList(nowMember);
        
        
        // 로그인된 시큐어 세션의 memberVO 정보
        //UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        //Account sessionMember = userPrincipal.getMember();
        
        //model.addAttribute("sessionMember", sessionMember);
        
        model.addAttribute("roles", "");
        model.addAttribute("userList", userList); // 사용자 목록
        model.addAttribute("params", nowMember); // 사용자페이지 관련 정보 / 페이징 정보
        model.addAttribute("totalRecordCount",nowMember.getPaginationInfo().getTotalRecordCount());
        return "/soulGod/admin/user";

    }
    @RequestMapping( "/soulGod/admin/auth")
    public String auth(Model model,
                        @CurrentUser MemberInfo memberInfo, MenuAuthMember param) {

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
        
        ModelAndView mav = new ModelAndView();
        model.addAttribute("sessionMember", memberInfo);
        model.addAttribute("roles", "");
        
        // user 관련
        List<MenuAuthMember> userList = adminAuthManagementService.getUserList(param);
        model.addAttribute("userList", userList); // 사용자 목록
        model.addAttribute("params", param); // 사용자페이지 관련 정보 / 페이징 정보

        
        model.addAttribute("mc", "ico_manage");
        model.addAttribute("pageTitle", "권한 관리");
        model.addAttribute("totalRecordCount",param.getPaginationInfo().getTotalRecordCount());
        return "/soulGod/admin/auth";

    }
    @RequestMapping( "/soulGod/admin/menu")
    public String menu(Model model,
                            @CurrentUser MemberInfo memberInfo) {

    	log.info("access Url : /menu/main, target Method : manuManagementMain()");
    	
    	List<CustomMap> resultMenuTree = new ArrayList();
    	List<CustomMap> resultMenuTreeOrderCount = new ArrayList();
    	//메뉴트리목록을 가져온다.
    	resultMenuTree = adminMenuManagementService.getMenuTree();
    	//메뉴트리목록 건수
    	resultMenuTreeOrderCount = adminMenuManagementService.getMenuTreeOrderCount();
    	
        model.addAttribute("sessionMember", memberInfo);
        model.addAttribute("roles", "");
        
        //메뉴 목록 트리를 json 데이터로 가공한다.
        JSONArray menuTree = new JSONArray(resultMenuTree);
        model.addAttribute("menuTree", resultMenuTree);
        
        //메뉴목록 최대 카운트
        JSONArray menuTreeOrderCount = new JSONArray(resultMenuTreeOrderCount);
        model.addAttribute("menuTreeOrderCount",resultMenuTreeOrderCount);

        log.info("access Url : /menu/main, target Method : manuManagementMain() End View menuMainPage");

        
        model.addAttribute("mc", "ico_manage");
        model.addAttribute("pageTitle", "메뉴 관리");

        return "/soulGod/admin/menu";

    }
    @RequestMapping( "/soulGod/admin/fakeCheck")
    public String fakeCheck(Model model,
                            @CurrentUser MemberInfo memberInfo, DetectionLevel params) {

        log.info("##### URI :: { /admin/fake_check } #####");
        
        HashMap<String, Object> resultMap = adminDetectLevelService.getDetectionLevelList(params);
		
        model.addAttribute("sessionMember", memberInfo);
        model.addAttribute("roles", "");
        model.addAttribute("detectionLevelList", resultMap.get("detectionLevelList"));
        model.addAttribute("status", resultMap.get("status"));
        
        model.addAttribute("mc", "ico_manage");
        model.addAttribute("pageTitle", "검증 수준(Threshold) 설정");

        return "/soulGod/admin/fakeCheck";

    }
    @RequestMapping( "/soulGod/admin/commonCode")
    public String commonCode(Model model,
                            @CurrentUser MemberInfo memberInfo) {

        model.addAttribute("mc", "ico_manage");
        model.addAttribute("pageTitle", "공통코드 관리");

        return "/soulGod/admin/commonCode";

    }
    
    @ResponseBody
    @RequestMapping( "/soulGod/admin/user/getDeptList")
    public HashMap<String, Object> userGetDeptList(DeptForm params) {
        log.info("##### URI :: { /admin/user/getDeptList } #####");
        log.info("##### params :: "+ params + " #####");
        HashMap<String, Object> map = new HashMap<String, Object>();
        
        // 부서
        List<DeptForm> deptList = adminDeptService.getDeptList(params);
        
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
        map.put("totalRecordCount", params.getPaginationInfo().getTotalRecordCount());
        
        // 검색 정보
        map.put("searchType", params.getSearchType());
        map.put("searchKeyword", params.getSearchKeyword());

        return map;
    }
    
	@PostMapping(value = {"/soulGod/admin/department/add"})
	@ResponseBody
    public HashMap<String, Object> departmentAdd(DeptForm params) {
        log.info("##### URI :: { /admin/department/add } #####");
        log.info("##### params :: "+ params + " #####");
        
        // user 관련
        HashMap<String, Object> map = adminDeptService.addDept(params);
        
        return map;
    }
	
	@PostMapping(value = {"/soulGod/admin/department/delete"})
	@ResponseBody
    public HashMap<String, Object> departmentDelete(DeptForm params) {
        log.info("##### URI :: { /admin/department/delete } #####");
        log.info("##### params :: "+ params + " #####");
        
        // user 관련
        HashMap<String, Object> map = adminDeptService.deleteDept(params);
        
        return map;
    }	
	
	@PostMapping(value = {"/soulGod/admin/department/mod"})
	@ResponseBody
    public HashMap<String, Object> departmentMod(DeptForm params) {
        log.info("##### URI :: { /admin/department/mod } #####");
        log.info("##### params :: "+ params + " #####");
        
        // user 관련
        HashMap<String, Object> map = adminDeptService.modDept(params);
        
        return map;
    }	
	
	@PostMapping(value = {"/soulGod/admin/user/getUserInfo"})
	@ResponseBody
    public HashMap<String, Object> userGetUserInfo(String userId) {
        log.info("##### URI :: { /admin/user/getUserInfo } #####");
        log.info("##### userId :: "+ userId + " #####");
        HashMap<String, Object> map = new HashMap<String, Object>();
        
        // user 관련
        map.put("userInfo", adminUserService.getUserInfo(userId));
        
        return map;
    }	
	
	@PostMapping(value = {"/soulGod/admin/user/checkUserId"})
	@ResponseBody
    public HashMap<String, Object> userCheckUserId(String userId) {
        log.info("##### URI :: { /admin/user/checkUserId } #####");
        log.info("##### userId :: "+ userId + " #####");
        HashMap<String, Object> map = new HashMap<String, Object>();
        
        // user 관련
        map.put("cnt", adminUserService.getUserCount(userId));
        
        return map;
    }	
	
	@PostMapping(value = {"/soulGod/admin/user/add"})
	@ResponseBody
    public HashMap<String, Object> userAdd(MemberInfo params) {
        log.info("##### URI :: { /admin/user/add } #####");
        log.info("##### params :: "+ params + " #####");
        
        // user 관련
        HashMap<String, Object> map = adminUserService.addUser(params);
        
        return map;
    }
	
	@PostMapping(value = {"/soulGod/admin/user/mod"})
	@ResponseBody
    public HashMap<String, Object> userMod(MemberInfo params) {
        log.info("##### URI :: { /admin/user/mod } #####");
        log.info("##### params :: "+ params + " #####");
        
        // user 관련
        HashMap<String, Object> map = adminUserService.modUser(params);
        
        return map;
    }	
	
	 
	@PostMapping(value = {"/soulGod/admin/user/resetPassword"})
	@ResponseBody
    public HashMap<String, Object> userResetPassword(MemberInfo params) {
        log.info("##### URI :: { /admin/user/resetPassword } #####");
        log.info("##### params :: "+ params + " #####");
        
        // user 관련
        HashMap<String, Object> map = adminUserService.resetPassword(params);
        
        return map;
    }

	@PostMapping(value = {"/soulGod/admin/user/delete"})
	@ResponseBody
    public HashMap<String, Object> userDelete(MemberInfo params) {
        log.info("##### URI :: { /admin/user/delete } #####");
        log.info("##### params :: "+ params + " #####");
        
        // user 관련
        HashMap<String, Object> map = adminUserService.deleteUser(params);
        
        return map;
    }	

	/**
	 * 권한관리 메뉴 목록 조회
	 * @param req
	 * @param userPrincipal
	 * @param requestParamMap
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = {"/soulGod/admin/auth/memberMenu"},method = RequestMethod.POST)	
    public HashMap<String, Object> authManagementMemberMenu(@CurrentUser MemberInfo memberInfo, @RequestBody HashMap<String, Object> requestParamMap) {        
    	log.info("access Url : auth/memberMenu, target Method : authManagementMemberMenu()");

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
	
	@ResponseBody
	@RequestMapping(value = {"/soulGod/admin/auth/insertMemberMenu"},method = RequestMethod.POST)	
    public HashMap<String, Object> authManagementInsertMemberMenu(@CurrentUser MemberInfo memberInfo, @RequestBody HashMap<String, Object> requestParamMap) {        
    	log.info("access Url : /menu/addMenu, target Method : authManagementInsertMemberMenu()");
    	
    	//request param을 담는다.
    	CustomMap param = new CustomMap();
    	param.putAll(requestParamMap);
    	param.orginPut("regMemId", memberInfo.getMemId());
    	
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
	
	/**
	 * 메뉴 추가 처리
	 * @param req
	 * @param userPrincipal
	 * @param requestParamMap
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = {"/soulGod/admin/menu/addMenu"},method = RequestMethod.POST)	
    public HashMap<String, Object> manuManagementAddMenu(HttpServletRequest req, @CurrentUser MemberInfo memberInfo, @RequestBody HashMap<String, Object> requestParamMap) {        
    	log.info("access Url : /menu/addMenu, target Method : manuManagementAddMenu()");
    	
    	//request param을 담는다.
    	CustomMap param = new CustomMap();
    	param.putAll(requestParamMap);
    	param.orginPut("menuRegId", memberInfo.getMemId());
    	param.orginPut("menuModId", memberInfo.getMemId());
    	
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
	@RequestMapping(value = {"/soulGod/admin/menu/updateMenu"},method = RequestMethod.POST)	
    public HashMap<String, Object> manuManagementUpdateMenu(HttpServletRequest req, @CurrentUser MemberInfo memberInfo, @RequestBody HashMap<String, Object> requestParamMap) {        
    	log.info("access Url : /menu/addMenu, target Method : manuManagementUpdateMenu()");
    	
    	//request param을 담는다.
    	CustomMap param = new CustomMap();
    	param.putAll(requestParamMap);
    	param.orginPut("menuModId", memberInfo.getMemId());
    	
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
	@RequestMapping(value = {"/soulGod/admin/menu/deleteMenu"},method = RequestMethod.POST)	
    public HashMap<String, Object> manuManagementDeleteMenu(HttpServletRequest req, @CurrentUser MemberInfo memberInfo, @RequestBody HashMap<String, Object> requestParamMap) {        
    	log.info("access Url : /menu/addMenu, target Method : manuManagementDeleteMenu()");
    	
    	//request param을 담는다.
    	CustomMap param = new CustomMap();
    	param.putAll(requestParamMap);
    	param.orginPut("menuModId", memberInfo.getMemId());
    	
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
	
	@PostMapping(value = {"/soulGod/admin/detection_level/change"})
	@ResponseBody
    public HashMap<String, Object> detectionLevelChange(@RequestBody HashMap<String, Object> params, HttpServletRequest request) {
        log.info("##### URI :: { /admin/detection_level/change } #####");
        log.info("##### params :: "+ params + " #####");
        
        HashMap<String, Object> map = adminDetectLevelService.setDetectionLevel(params, request);
        
        return map;
    }	
}
