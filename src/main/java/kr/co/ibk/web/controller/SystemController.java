package kr.co.ibk.web.controller;

import kr.co.ibk.common.annotation.CurrentUser;
import kr.co.ibk.domain.web.Account;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
public class SystemController {

    @RequestMapping( "/soulGod/system/monitoring")
    public String monitoring(Model model,
                        @CurrentUser Account account) {

        model.addAttribute("mc", "system");
        model.addAttribute("pageTitle", "모니터링");

        return "/soulGod/system/monitoring";

    }
}
