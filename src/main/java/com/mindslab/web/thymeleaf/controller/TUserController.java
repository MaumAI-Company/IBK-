package com.mindslab.web.thymeleaf.controller;

import com.mindslab.web.common.controller.CommonThymeleafController;
import com.mindslab.web.thymeleaf.vo.UserVO;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/thymeleaf")
public class TUserController extends CommonThymeleafController{
    @GetMapping("/test")
    public String getUser(Model model) {
        UserVO user = new UserVO("userId 1234", "테스트", "web") ;
        model.addAttribute("user", user);
        return "thymeleaf/test";
    }
}
