package kr.co.ibk.web.controller;


import kr.co.ibk.common.annotation.CurrentUser;
import kr.co.ibk.domain.web.MemberInfo;
import kr.co.ibk.web.BaseCont;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@Controller
@RequiredArgsConstructor
public class LoginController extends BaseCont {

    @GetMapping("/login")
    public String Login(Model model) {
        model.addAttribute("pageTitle", "로그인");
        return "/login";
    }


    @PostMapping("/loginSuccess")
    public String LoginSuccess(@CurrentUser MemberInfo memberInfo,
                               HttpServletRequest request,
                               HttpServletResponse response) {
        boolean saveMe = request.getParameter("remember-me") != null;
        Cookie[] cookies = request.getCookies();
        if (saveMe) {
            Cookie cookie = new Cookie("loginId", memberInfo.getMemId());
            cookie.setMaxAge(60 * 60 * 24 * 30);
            cookie.setPath("/");
            response.addCookie(cookie);
        } else {
            if (cookies != null) {
                for (Cookie cookie : cookies) {
                    if (cookie.getName().equals("loginId")) {
                        cookie.setValue("");
                        cookie.setPath("/");
                        cookie.setMaxAge(0);
                        response.addCookie(cookie);
                        break;
                    }
                }
            }
        }

        //return "redirect:/soulGod/main";
        return "redirect:/soulGod/report/card";
    }

    @PostMapping("/loginFailure")
    public String loginFailure(Model model,
                               HttpServletRequest request,
                               RedirectAttributes redirectAttributes) {
        model.addAttribute("ERRORMSG", request.getAttribute("ERRORMSG"));
        redirectAttributes.addFlashAttribute("ERRORMSG", request.getAttribute("ERRORMSG"));
        return "redirect:/login";
    }

    @RequestMapping("/logout")
    public String logout(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        session.invalidate();
        return "redirect:/";
    }
}
