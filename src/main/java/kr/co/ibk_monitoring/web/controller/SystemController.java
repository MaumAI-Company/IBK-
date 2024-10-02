package kr.co.ibk_monitoring.web.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
public class SystemController {

    @RequestMapping( "/system/monitoring")
    public String monitoring(Model model) {

        model.addAttribute("mc", "ico_dns");
        model.addAttribute("pageTitle", "모니터링");

        return "/system/monitoring";

    }
}
