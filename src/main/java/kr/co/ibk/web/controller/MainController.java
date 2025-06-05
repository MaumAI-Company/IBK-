package kr.co.ibk.web.controller;

import kr.co.ibk.common.annotation.CurrentUser;
import kr.co.ibk.domain.web.MemberInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
public class MainController {


    @RequestMapping({"/", "/index"})
    public String index(Model model,
                        @CurrentUser MemberInfo memberInfo) {

        model.addAttribute("mc", "main");
        if (memberInfo == null) {
            return "redirect:/login";
        } else {
            return "redirect:/soulGod/index";
        }
//        return "redirect:/soulGod/index";
    }

    /*@RequestMapping({"/soulGod", "/soulGod/index"})
    public String managerIndex(Model model,
                               HttpServletRequest request,
                               HttpSession session) {

        model.addAttribute("mc", "main");
        return "redirect:/index";
    }*/

    @GetMapping("/soulGod/home")
    public String home() {
        return "/soulGod/home";
    }

    @GetMapping({"/soulGod", "/soulGod/index", "/soulGod/main"})
    public String main(Model model){
//        model.addAttribute("loginDtm", LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd(E) HH:mm:ss")));
        model.addAttribute("pageTitle", "대시보드");

//        return "/soulGod/main";
        return "redirect:/soulGod/report/card";
    }
}
