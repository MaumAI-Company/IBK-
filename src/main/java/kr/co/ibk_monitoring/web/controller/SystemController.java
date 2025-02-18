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

    @Value("${Globals.domain.mcc1}")
    private String mccDomain1;
    @Value("${Globals.domain.mcc2}")
    private String mccDomain2;

    @Value("${Globals.domain.web}")
    private String webDomain;

    @Value("${Globals.check.mcc1}")
    private Boolean mccCheck1;
    @Value("${Globals.check.mcc2}")
    private Boolean mccCheck2;

    @Value("${Globals.check.web}")
    private Boolean webCheck;

    private final ApiService apiService;

    @RequestMapping( "/system/monitoring")
    public String monitoring(Model model) {

        model.addAttribute("mccDomain1", mccDomain1);
        model.addAttribute("mccDomain2", mccDomain2);
        model.addAttribute("webDomain", webDomain);
        model.addAttribute("mccCheck1", mccCheck1);
        model.addAttribute("mccCheck2", mccCheck2);
        model.addAttribute("webCheck", webCheck);

        model.addAttribute("mc", "ico_dns");
        model.addAttribute("pageTitle", "모니터링");

        return "system/monitoring";

    }

    @ResponseBody
    @PostMapping( "/api/v1/o/mccServerCheck1")
    public String mccServerCheck1() {
        Integer code = null;
        try {
            code = apiService.mccServerCheck1();
        } catch (Exception e) {
            code = null;
        }
        return code == null ? "" : code.toString();
    }

    @ResponseBody
    @PostMapping( "/api/v1/o/mccServerCheck2")
    public String mccServerCheck2() {
        Integer code = null;
        try {
            code = apiService.mccServerCheck2();
        } catch (Exception e) {
            code = null;
        }
        return code == null ? "" : code.toString();
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
        return code == null ? "500" : code.toString();
    }
}
