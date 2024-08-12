package kr.co.ibk.web.controller;

import kr.co.ibk.common.annotation.CurrentUser;
import kr.co.ibk.domain.web.Account;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
public class AdminController {

    @RequestMapping( "/soulGod/admin/department")
    public String department(Model model,
                        @CurrentUser Account account) {

        model.addAttribute("mc", "admin");
        model.addAttribute("pageTitle", "부서 관리");

        return "/soulGod/admin/department";

    }

    @RequestMapping( "/soulGod/admin/user")
    public String user(Model model,
                        @CurrentUser Account account) {

        model.addAttribute("mc", "admin");
        model.addAttribute("pageTitle", "사용자 관리");

        return "/soulGod/admin/user";

    }
    @RequestMapping( "/soulGod/admin/auth")
    public String auth(Model model,
                        @CurrentUser Account account) {

        model.addAttribute("mc", "admin");
        model.addAttribute("pageTitle", "권한 관리");

        return "/soulGod/admin/auth";

    }
    @RequestMapping( "/soulGod/admin/menu")
    public String menu(Model model,
                            @CurrentUser Account account) {

        model.addAttribute("mc", "admin");
        model.addAttribute("pageTitle", "메뉴 관리");

        return "/soulGod/admin/menu";

    }
    @RequestMapping( "/soulGod/admin/fakeCheck")
    public String fakeCheck(Model model,
                            @CurrentUser Account account) {

        model.addAttribute("mc", "admin");
        model.addAttribute("pageTitle", "검증 수준(Threshold) 설정");

        return "/soulGod/admin/fakeCheck";

    }
    @RequestMapping( "/soulGod/admin/commonCode")
    public String commonCode(Model model,
                            @CurrentUser Account account) {

        model.addAttribute("mc", "admin");
        model.addAttribute("pageTitle", "공통코드 관리");

        return "/soulGod/admin/commonCode";

    }
}
