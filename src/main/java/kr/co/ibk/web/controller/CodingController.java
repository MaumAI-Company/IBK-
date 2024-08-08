package kr.co.ibk.web.controller;

import kr.co.ibk.common.annotation.CurrentUser;
import kr.co.ibk.domain.web.Account;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
@RequiredArgsConstructor
public class CodingController {


    @RequestMapping({"/coding", "/coding/index"})
    public String index(Model model,
                        @CurrentUser Account account) {

        model.addAttribute("mc", "main");

        return "/coding/index";

    }

}
