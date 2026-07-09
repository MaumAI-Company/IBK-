package com.mindslab.web.sso.controller;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.mindslab.web.common.controller.CommonController;
import com.mindslab.web.properties.MindsLabProperties;
import com.mindslab.web.sso.service.MsgSsoService;
import com.mindslab.web.vo.MsgVO;

import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
@RequestMapping("/sso")
public class SsoController extends CommonController {

	@Autowired
	protected MindsLabProperties mindsLabProperties;
	
	@Autowired
	private MsgSsoService msgSsoService;
	
    
	@RequestMapping(value = {"", "/", "home"})
    public ModelAndView user(Authentication authentication, MsgVO params, HttpServletRequest request) {
        log.info("##### URI :: { /sso, /sso/, /sso/home } #####");

        return preVerification(authentication, params, request);
    }
	
	@RequestMapping(value = {"pre_verification"})
    public ModelAndView preVerification(Authentication authentication, MsgVO params, HttpServletRequest request) {
        log.info("##### URI :: { sso/pre_verification } #####");
        HttpSession session = request.getSession();
        String resultCode = session.getAttribute("resultCode") == null ? "" : session.getAttribute("resultCode").toString();
        String resultMessage = session.getAttribute("resultMessage") == null ? "" : session.getAttribute("resultMessage").toString();
        String id = session.getAttribute("id") == null ? "" : session.getAttribute("id").toString();
        String error = session.getAttribute("error") == null ? "" : session.getAttribute("error").toString();

        ModelAndView mav = new ModelAndView();
        if ("000000".equals(resultCode)) { // SSO 테스트 기간동안에는 SSO "" 여도 실행.  || "".equals(resultCode) 
            if ("".equals(id)) {
                mav.addObject("isError", "true");
                mav.addObject("errorMessage", "SSO 계정 정보가 없습니다.");
                mav.addObject("resultCode", resultCode); // SSO 결과코드
                mav.addObject("resultMessage", resultMessage); // SSO 결과메시지
                mav.setViewName("/login");
            } else {                
                params.setEmCode(id);
                List<MsgVO> msgList = msgSsoService.getMsgList(params);
                mav.addObject("msgList", msgList); // 사용자 목록
                mav.addObject("params", params); // 사용자페이지 관련 정보 / 페이징 정보
                mav.setViewName("/sso/pre_verification");
            }
        } else {
            mav.addObject("isError", "true");
            if ("network".equals(error)) {
                mav.addObject("errorMessage", "SSO Network Error");
            } else {
                mav.addObject("errorMessage", "SSO 인증에 실패하였습니다.");
            }
            mav.addObject("resultCode", resultCode); // SSO 결과코드
            mav.addObject("resultMessage", resultMessage); // SSO 결과메시지
            mav.setViewName("/login");
        }
        
        return mav;
    }
	
	@RequestMapping(value = {"pre_verification/next"})
	@ResponseBody
    public HashMap<String, Object> preVerificationNext(@RequestBody HashMap<String, Object> paramMap) {
        log.info("##### URI :: { sso/pre_verification/next } #####");   

        // 스캔 정보 조회
        MsgVO msgInfo = msgSsoService.getMsgNextInfo(paramMap);
        
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("msgInfo", msgInfo);
        return map;
    }
    
	@RequestMapping(value = {"pre_verification/prev"})
	@ResponseBody
    public HashMap<String, Object> preVerificationPrev(@RequestBody HashMap<String, Object> paramMap) {
        log.info("##### URI :: { sso/pre_verification/prev } #####");   

        // 스캔 정보 조회
        MsgVO msgInfo = msgSsoService.getMsgPrevInfo(paramMap);
        
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("msgInfo", msgInfo);
        return map;
    }
    
	@RequestMapping(value = {"pre_verification/excel_down"})
    public void  detectScanExcelDown(HttpServletRequest request, HttpServletResponse response, MsgVO params) {
        log.info("##### URI :: { pre_verification/excel_down } #####");
        HttpSession session = request.getSession();
        String resultCode = session.getAttribute("resultCode") == null ? "" : session.getAttribute("resultCode").toString();
        String resultMessage = session.getAttribute("resultMessage") == null ? "" : session.getAttribute("resultMessage").toString();
        String id = session.getAttribute("id") == null ? "" : session.getAttribute("id").toString();        
        params.setEmCode(id);
        msgSsoService.getMsgListExcelDown(params, response);
    }

}
