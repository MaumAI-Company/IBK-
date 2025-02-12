package kr.co.ibk_monitoring.web.controller;

import kr.co.ibk_monitoring.service.ApiService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequiredArgsConstructor
public class SystemController {

    @Value("${Globals.domain.mcc}")
    private String mccDomain;

    @Value("${Globals.domain.web}")
    private String webDomain;

    @Value("${Globals.check.mcc}")
    private Boolean mccCheck;

    @Value("${Globals.check.web}")
    private Boolean webCheck;

    private final ApiService apiService;

    @RequestMapping( "/system/monitoring")
    public String monitoring(Model model) {

        model.addAttribute("mccDomain", mccDomain);
        model.addAttribute("webDomain", webDomain);
        model.addAttribute("mccCheck", mccCheck);
        model.addAttribute("webCheck", webCheck);

        model.addAttribute("mc", "ico_dns");
        model.addAttribute("pageTitle", "모니터링");

        return "system/monitoring";

    }

    @ResponseBody
    @PostMapping( "/api/v1/o/mccServerCheck")
    public String mccServerCheck() {
        Integer code = null;
        try {
            code = apiService.mccServerCheck();
        } catch (Exception e) {
            code = 500;
        }
        return code.toString();
    }

    @ResponseBody
    @PostMapping( "/api/v1/o/webServerCheck")
    public String webServerCheck() {
        Integer code = null;
        try {
            code = apiService.webServerCheck();
        } catch (Exception e) {
            code = 500;
        }
        return code.toString();
    }
}
