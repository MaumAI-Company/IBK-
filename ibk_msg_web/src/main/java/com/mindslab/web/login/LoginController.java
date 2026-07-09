package com.mindslab.web.login;

import java.io.IOException;
import java.security.Principal;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.mindslab.web.common.controller.CommonController;
import com.mindslab.web.common.support.utils.RoleConst;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
public class LoginController extends CommonController {

    @RequestMapping(value = { "/", "/index", "/login", "/mindslab/login" })
    public ModelAndView index(HttpServletRequest req, HttpServletResponse resp) {
        log.info("##### URI :: { /, /index, /login, /mindslab/login } #####");
        
        // RSA 키 생성
    	initRsa(req);
    	
        ModelAndView mav = new ModelAndView();

        mav.setViewName("/login");
        return mav;
    }

    @RequestMapping("/mindslab/user/login")
    public ModelAndView userLogin(HttpServletRequest req, HttpServletResponse resp) {
        log.info("##### URI :: { /mindslab/user/login } #####");
        
        // RSA 키 생성
    	initRsa(req);
    	
        ModelAndView mav = new ModelAndView();
        mav.setViewName("/user/login");
        return mav;
    }

    @RequestMapping("/mindslab/admin/login")
    public ModelAndView adminLogin(HttpServletRequest req, HttpServletResponse resp) {
        log.info("##### URI :: { /mindslab/admin/login } #####");
        
        // RSA 키 생성
    	initRsa(req);
    	
        ModelAndView mav = new ModelAndView();
        mav.setViewName("/admin/login");
        return mav;
    }

    @RequestMapping("/mindslab/guest")
    public ModelAndView guestHome(HttpServletRequest req, HttpServletResponse resp) {
        log.info("##### URI :: { /mindslab/guest } #####");
        
        // RSA 키 생성
    	initRsa(req);
    	
        ModelAndView mav = new ModelAndView();
        mav.setViewName("/guest/home");
        return mav;
    }

    @RequestMapping("/home")
    public String home(Principal principal, HttpServletRequest req, HttpServletResponse resp) {
        log.info("##### URI :: { /home } #####");
        // 바로 로그인페이지로 접근한 경우 /home 에서 분기
        try {
            // RSA 키 생성
        	initRsa(req);
        	
            if (RoleConst.compareToRole(principal, RoleConst.ADMIN.getRole())) {
                resp.sendRedirect("/admin");
            } else if (RoleConst.compareToRole(principal, RoleConst.USER.getRole())) {
                resp.sendRedirect("/user");
            } else {
                resp.sendRedirect("/mindslab/guest");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}
