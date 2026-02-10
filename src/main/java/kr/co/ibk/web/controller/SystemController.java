package kr.co.ibk.web.controller;

import kr.co.ibk.common.annotation.MenuAuthBase;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
public class SystemController {

    @Value("${Globals.status.domain}")
    private String statusDomain;

    @MenuAuthBase("/soulGod/system/monitoring")
    @RequestMapping("/soulGod/system/monitoring")
    public String monitoring(Model model) {

        model.addAttribute("statusDomain", statusDomain);
        model.addAttribute("mc", "ico_dns");
        model.addAttribute("pageTitle", "모니터링");

        return "/soulGod/system/monitoring";

    }

    @MenuAuthBase("/soulGod/system/monitoring")
    @RequestMapping("/soulGod/system/localStatus")
    public String localStatus(Model model) {

        model.addAttribute("mc", "ico_dns");
        model.addAttribute("pageTitle", "모니터링");

        return "/soulGod/system/localStatus";

    }
}
