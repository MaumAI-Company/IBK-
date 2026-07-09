package com.mindslab.web.user.controller;
import java.security.PrivateKey;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.mindslab.web.admin.service.AdminDetectLevelService;
import com.mindslab.web.auth.UserPrincipal;
import com.mindslab.web.common.controller.CommonController;
import com.mindslab.web.properties.MindsLabProperties;
import com.mindslab.web.user.service.LearnHistoryService;
import com.mindslab.web.user.service.LearnModelService;
import com.mindslab.web.user.service.LearnService;
import com.mindslab.web.user.service.MsgService;
import com.mindslab.web.user.service.MsgStatisticService;
import com.mindslab.web.user.service.StatisticService;
import com.mindslab.web.user.service.UserService;
import com.mindslab.web.vo.DeptVO;
import com.mindslab.web.vo.LearnHistoryVO;
import com.mindslab.web.vo.LearnModelVO;
import com.mindslab.web.vo.LearnVO;
import com.mindslab.web.vo.MemberVO;
import com.mindslab.web.vo.MsgVO;

import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
@RequestMapping("/user")
public class UserController extends CommonController {

	@Autowired
	protected MindsLabProperties mindsLabProperties;
	
	@Autowired
	private UserService userService;
		
	@Autowired
	private MsgService msgService;

    @Autowired
    private LearnService learnService;
    
    @Autowired
    private LearnModelService learnModelService;

	@Autowired
	private LearnHistoryService learnHistoryService;
	
	@Autowired
	private StatisticService statisticService;
	
	@Autowired
	private MsgStatisticService msgStatisticService;
	
	@Autowired
	private AdminDetectLevelService adminDetectLevelService; 
    
	@RequestMapping(value = {"", "/", "home"})
    public ModelAndView user(Authentication authentication, MsgVO params) {
        log.info("##### URI :: { /user, /user/, /user/home } #####");
        /*
         *  method :: user(Authentication authentication,, Principal principal HttpServletRequest req, HttpServletResponse resp)
            ModelAndView mav = new ModelAndView();
            mav.addObject("user", principal.getName());
            mav.addObject("roles", ((UsernamePasswordAuthenticationToken) principal).getAuthorities());
            mav.setViewName("/user/home");
            return mav;
        */

        return preVerification(authentication, params);
    }
	
	@RequestMapping(value = {"pre_verification"})
    public ModelAndView preVerification(Authentication authentication, MsgVO params) {
        log.info("##### URI :: { pre_verification } #####");
        
        List<MsgVO> msgList = msgService.getMsgList(params);
        
        // 로그인된 시큐어 세션의 memberVO 정보
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        MemberVO sessionMember = userPrincipal.getMember();
        
        ModelAndView mav = new ModelAndView();
        mav.addObject("sessionMember", sessionMember);
        mav.addObject("roles", userPrincipal.getAuthorities());
        mav.addObject("msgList", msgList); // 사용자 목록
        mav.addObject("params", params); // 사용자페이지 관련 정보 / 페이징 정보
        mav.setViewName("/user/pre_verification");
        return mav;
    }

	@RequestMapping(value = {"pre_verification_test"})
    public ModelAndView preVerificationTest(Authentication authentication) {
        log.info("##### URI :: { pre_verification } #####");
        
        
        // 로그인된 시큐어 세션의 memberVO 정보
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        MemberVO sessionMember = userPrincipal.getMember();
        
        ModelAndView mav = new ModelAndView();
        
        mav.addObject("sessionMember", sessionMember);
        mav.addObject("roles", userPrincipal.getAuthorities());

        String apiUrl = mindsLabProperties.getMindsLabApiUrl(); 
        mav.addObject("apiUrl", apiUrl);
        
        mav.setViewName("/user/pre_verification_test");
        return mav;
    }
	
	@RequestMapping(value = {"pre_verification/next"})
	@ResponseBody
    public HashMap<String, Object> preVerificationNext(@RequestBody HashMap<String, Object> paramMap) {
        log.info("##### URI :: { user/pre_verification/next } #####");   

        // 스캔 정보 조회
        MsgVO msgInfo = msgService.getMsgNextInfo(paramMap);
        
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("msgInfo", msgInfo);
        return map;
    }
    
	@RequestMapping(value = {"pre_verification/prev"})
	@ResponseBody
    public HashMap<String, Object> preVerificationPrev(@RequestBody HashMap<String, Object> paramMap) {
        log.info("##### URI :: { user/pre_verification/prev } #####");   

        // 스캔 정보 조회
        MsgVO msgInfo = msgService.getMsgPrevInfo(paramMap);
        
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("msgInfo", msgInfo);
        return map;
    }
    
	@RequestMapping(value = {"pre_verification/excel_down"})
    public void  detectScanExcelDown(HttpServletRequest request, HttpServletResponse response, MsgVO params) {
        log.info("##### URI :: { pre_verification/excel_down } #####");
        
        msgService.getMsgListExcelDown(params, response);
    }

	@RequestMapping(value = {"learn_data_manage"})
    public ModelAndView learnDataManage(Authentication authentication, LearnVO params) {
        log.info("##### URI :: { learn_data_manage } #####");
        
        List<LearnVO> learnList = learnService.getLearnList(params);
        int learningCount = learnService.getLearningCount(params);
        
        // 로그인된 시큐어 세션의 memberVO 정보
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        MemberVO sessionMember = userPrincipal.getMember();
        
        ModelAndView mav = new ModelAndView();
        mav.addObject("sessionMember", sessionMember);
        mav.addObject("roles", userPrincipal.getAuthorities());
        mav.addObject("learnList", learnList); // 사용자 목록
        mav.addObject("learningCount", learningCount); // 학습중인 건 수
        mav.addObject("params", params); // 사용자페이지 관련 정보 / 페이징 정보

        String apiUrl = mindsLabProperties.getMindsLabApiUrl(); 
        mav.addObject("apiUrl", apiUrl);
        mav.setViewName("/user/learn_data_manage");
        return mav;
    }
	
	@RequestMapping(value = {"learn_model_manage"})
    public ModelAndView learnModelManage(Authentication authentication, LearnModelVO params) {
        log.info("##### URI :: { learn_model_manage } #####");
        
        List<LearnModelVO> learnModelList = learnModelService.getLearnModelList(params);
        int deployingCount = learnModelService.getDeployingCount(params);
        
        // 로그인된 시큐어 세션의 memberVO 정보
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        MemberVO sessionMember = userPrincipal.getMember();
        
        ModelAndView mav = new ModelAndView();
        mav.addObject("sessionMember", sessionMember);
        mav.addObject("roles", userPrincipal.getAuthorities());
        mav.addObject("learnModelList", learnModelList); // 사용자 목록
        mav.addObject("deployingCount", deployingCount); // 배포중인 건 수
        mav.addObject("params", params); // 사용자페이지 관련 정보 / 페이징 정보
        
        mav.addObject("threshold", adminDetectLevelService.getDetectionLevel()); //스레스 홀드정보

        String apiUrl = mindsLabProperties.getMindsLabApiUrl(); 
        mav.addObject("apiUrl", apiUrl);
        mav.setViewName("/user/learn_model_manage");
        return mav;
    }

    @PostMapping(value = {"deploy_status_update"})
    @ResponseBody
    public  HashMap<String, Object> deployStatusUpdate(Authentication authentication, @RequestBody LearnModelVO learnModelVO ) {
        log.info("##### URI :: { deploy_status_update } #####");
        // 로그인된 시큐어 세션의 memberVO 정보
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        MemberVO sessionMember = userPrincipal.getMember(); 
        HashMap<String, Object> sessionMap = userService.getUserInfo(sessionMember.getMemId()); 
        MemberVO memberVO = (MemberVO) sessionMap.get("memberVO");  
        
        HashMap<String, Object> map = new HashMap<String, Object>();
        //LearnVO learnVO = new LearnVO();
        learnModelVO.setModId(memberVO.getMemId());// 수정자
        
        String apiUrl = mindsLabProperties.getMindsLabApiUrl(); 
        map = learnModelService.deployStatusUpdate(learnModelVO);
        map.put("apiUrl", apiUrl);
        return map;
    }
    
    @PostMapping(value = {"add_learn_history"})
    @ResponseBody
    public  HashMap<String, Object> addLearnHistory(Authentication authentication, @RequestBody LearnHistoryVO learnHistoryVO ) {
        log.info("##### URI :: { add_learn_history } #####");
        // 로그인된 시큐어 세션의 memberVO 정보
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        MemberVO sessionMember = userPrincipal.getMember(); 
        HashMap<String, Object> sessionMap = userService.getUserInfo(sessionMember.getMemId()); 
        MemberVO memberVO = (MemberVO) sessionMap.get("memberVO");  
        
        HashMap<String, Object> map = new HashMap<String, Object>();
        learnHistoryVO.setRegId(memberVO.getMemId());// 수정자
        learnHistoryVO.setModId(memberVO.getMemId());// 수정자
        
        map = learnHistoryService.addLearnHistory(learnHistoryVO);
        return map;
    }
    
	
	@RequestMapping(value = {"learn_deploy_manage"})
    public ModelAndView learnDeployManage(Authentication authentication, LearnHistoryVO params) {
        log.info("##### URI :: { learn_deploy_manage } #####");
        
        List<LearnHistoryVO> learnHistoryList = learnHistoryService.getLearnHistoryList(params);
        
        // 로그인된 시큐어 세션의 memberVO 정보
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        MemberVO sessionMember = userPrincipal.getMember();
        
        ModelAndView mav = new ModelAndView();
        mav.addObject("sessionMember", sessionMember);
        mav.addObject("roles", userPrincipal.getAuthorities());
        mav.addObject("learnHistoryList", learnHistoryList); // 사용자 목록
        mav.addObject("params", params); // 사용자페이지 관련 정보 / 페이징 정보

        String apiUrl = mindsLabProperties.getMindsLabApiUrl(); 
        mav.addObject("apiUrl", apiUrl);
        mav.setViewName("/user/learn_deploy_manage");
        return mav;
    }
	
	@PostMapping(value = {"learn_data_fileupload"})
	@ResponseBody
    public  HashMap<String, Object> learnDatafileupload(Authentication authentication, MultipartFile file, LearnVO learnVO) {
        log.info("##### URI :: { learnDatafileupload } #####");
        // 로그인된 시큐어 세션의 memberVO 정보
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        MemberVO sessionMember = userPrincipal.getMember();
        HashMap<String, Object> sessionMap = userService.getUserInfo(sessionMember.getMemId()); 
        MemberVO memberVO = (MemberVO) sessionMap.get("memberVO");  
        
        HashMap<String, Object> map = new HashMap<String, Object>();
		learnVO.setRegId(memberVO.getMemId());// 등록자
		learnVO.setModId(memberVO.getMemId());// 수정자
        map = learnService.learnDatafileupload(file, learnVO); // 파일 업로드 및 데이터 저장 
        return map;
    }
	
	@PostMapping(value = {"answer_data_fileupload"})
	@ResponseBody
    public  HashMap<String, Object> answerDatafileupload(Authentication authentication, MultipartFile file, LearnVO learnVO ) {
        log.info("##### URI :: { answerDatafileupload } #####");
        // 로그인된 시큐어 세션의 memberVO 정보
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        MemberVO sessionMember = userPrincipal.getMember(); 
        HashMap<String, Object> sessionMap = userService.getUserInfo(sessionMember.getMemId()); 
        MemberVO memberVO = (MemberVO) sessionMap.get("memberVO");  
        
        HashMap<String, Object> map = new HashMap<String, Object>();
        //LearnVO learnVO = new LearnVO();
		learnVO.setModId(memberVO.getMemId());// 수정자
        map = learnService.answerDatafileupload(file, learnVO); // 파일 업로드 및 데이터 저장
        
        return map;
    }

	@PostMapping(value = {"learning_status_update"})
	@ResponseBody
    public  HashMap<String, Object> learningStatusUpdate(Authentication authentication, @RequestBody LearnVO learnVO ) {
        log.info("##### URI :: { learning_status_update } #####");
        // 로그인된 시큐어 세션의 memberVO 정보
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        MemberVO sessionMember = userPrincipal.getMember(); 
        HashMap<String, Object> sessionMap = userService.getUserInfo(sessionMember.getMemId()); 
        MemberVO memberVO = (MemberVO) sessionMap.get("memberVO");  
        
        HashMap<String, Object> map = new HashMap<String, Object>();
        //LearnVO learnVO = new LearnVO();
		learnVO.setModId(memberVO.getMemId());// 수정자
        
        String apiUrl = mindsLabProperties.getMindsLabApiUrl(); 
        map = learnService.learningStatusUpdate(learnVO);
        map.put("apiUrl", apiUrl);
        return map;
    }


	@PostMapping(value = {"delete_learn"})
	@ResponseBody
    public  HashMap<String, Object> deleteLearn(Authentication authentication, @RequestBody LearnVO learnVO ) {
        log.info("##### URI :: { delete_learn } #####");
        // 로그인된 시큐어 세션의 memberVO 정보
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        MemberVO sessionMember = userPrincipal.getMember(); 
        HashMap<String, Object> sessionMap = userService.getUserInfo(sessionMember.getMemId()); 
        MemberVO memberVO = (MemberVO) sessionMap.get("memberVO");  
        
        HashMap<String, Object> map = new HashMap<String, Object>();
		learnVO.setModId(memberVO.getMemId());// 수정자
        
        map = learnService.deleteLearn(learnVO);
        return map;
    }
	
	@RequestMapping(value = {"profile"})
    public ModelAndView profile(HttpServletRequest req, Authentication authentication) {
        log.info("##### URI :: { profile } #####");
        // RSA 키 생성
     	initRsa(req);
     	
        // 로그인된 시큐어 세션의 memberVO 정보
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        MemberVO sessionMember = userPrincipal.getMember();
        
        HashMap<String, Object> map = userService.getUserInfo(sessionMember.getMemId()); 
        MemberVO memberVO = (MemberVO) map.get("memberVO");  
        DeptVO deptVO = (DeptVO) map.get("deptVO");  
        
        ModelAndView mav = new ModelAndView();
        mav.addObject("sessionMember", sessionMember);
        mav.addObject("roles", userPrincipal.getAuthorities());
        mav.addObject("memberVO", memberVO);
        mav.addObject("deptVO", deptVO);
        
        mav.setViewName("/user/profile");
        return mav;
    }

	@RequestMapping(value = {"pre_verification_statistic"})
    public ModelAndView preVerificationStatistic(Authentication authentication) {
        log.info("##### URI :: { learn_model_manage } #####");
        
        
        // 로그인된 시큐어 세션의 memberVO 정보
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        MemberVO sessionMember = userPrincipal.getMember();
        
        ModelAndView mav = new ModelAndView();
        mav.addObject("sessionMember", sessionMember);
        mav.addObject("roles", userPrincipal.getAuthorities());

        String apiUrl = mindsLabProperties.getMindsLabApiUrl(); 
        mav.addObject("apiUrl", apiUrl);
        mav.setViewName("/user/pre_verification_statistic");
        return mav;
    }

	@RequestMapping(value = {"pre_verification_statistic/search"})
	@ResponseBody
    public HashMap<String, Object> preVerificationStatisticSearch(@RequestBody HashMap<String, Object> paramMap) {
        log.info("##### URI :: { user/pre_verification_statistic/search } #####");   

        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("list", msgStatisticService.getStatistic(paramMap));
        map.put("params", paramMap); // 사용자페이지 관련 정보 / 페이징 정보
        return map;
    }

	@RequestMapping(value = {"pre_verification_statistic/excel_down"})
    public void  preVerificationStatisticExcelDown(HttpServletRequest request, HttpServletResponse response, @RequestParam HashMap<String, Object> paramMap) {
        log.info("##### URI :: { user/pre_verification_statistic/excel_down } #####");
        
        msgStatisticService.getStatisticExcelDown(paramMap, response);
    }
	

	@RequestMapping(value = {"monitoring"})
    public ModelAndView monitoring(Authentication authentication) {
        log.info("##### URI :: { monitoring } #####");
        
        // 로그인된 시큐어 세션의 memberVO 정보
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        MemberVO sessionMember = userPrincipal.getMember();
        
        ModelAndView mav = new ModelAndView();
        mav.addObject("sessionMember", sessionMember);
        mav.addObject("roles", userPrincipal.getAuthorities());
        mav.addObject("subMenu", subMenu(sessionMember));
        mav.setViewName("/user/monitoring");
        return mav;
    }

	@RequestMapping(value = {"statistic"})
    public ModelAndView statistic(Authentication authentication) {
        log.info("##### URI :: { statistic } #####");
        
        // 로그인된 시큐어 세션의 memberVO 정보
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        MemberVO sessionMember = userPrincipal.getMember();
        
        ModelAndView mav = new ModelAndView();
        mav.addObject("sessionMember", sessionMember);
        mav.addObject("roles", userPrincipal.getAuthorities());
        //mav.addObject("list", statisticService.getStatisticList(paramMap));
        mav.setViewName("/user/statistic");
        return mav;
    }
 
	@RequestMapping(value = {"statistic/excel_down"})
    public void  statisticListExcelDown(HttpServletRequest request, HttpServletResponse response, @RequestParam HashMap<String, Object> paramMap) {
        log.info("##### URI :: { user/statistic/excel_down } #####");
        
        statisticService.getStatisticListExcelDown(paramMap, response);
    }
	
	@RequestMapping(value = {"statistic/search"})
	@ResponseBody
    public HashMap<String, Object> statisticSearch(@RequestBody HashMap<String, Object> paramMap) {
        log.info("##### URI :: { user/statistic/search } #####");   

        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("list", statisticService.getStatisticList(paramMap));
        map.put("params", paramMap); // 사용자페이지 관련 정보 / 페이징 정보
        return map;
    }
	
	@PostMapping(value= {"changeUserPassword"})
	@ResponseBody
	public HashMap<String, Object> changeUserPassword(@RequestBody HashMap<String, Object> paramMap) {
        log.info("##### URI :: { changeUserPassword } #####");

		// RSA 복호화
		String userIdRSA = (String) paramMap.get("userId");
		String chkPwdRSA = (String) paramMap.get("chkPwd");
		String newPwdRSA = (String) paramMap.get("newPwd");
		
		// 데이터 
		String userId = "";
		String chkPwd = "";
		String newPwd = "";
		
		PrivateKey privateKey = (PrivateKey) CommonController.RSA_PRIVATE_KEY;// 복호화
		
		try {
			userId = CommonController.decryptRsa(privateKey, userIdRSA);
			chkPwd = CommonController.decryptRsa(privateKey, chkPwdRSA);
			newPwd = CommonController.decryptRsa(privateKey, newPwdRSA);
		} catch (Exception e) {
		}
		// 비밀번호 변경로직 실행 후 결과반환
		HashMap<String, Object> map = userService.changeUserPassword(userId, chkPwd, newPwd);
		
		return map;
	}
    
}
