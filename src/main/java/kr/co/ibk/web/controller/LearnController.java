package kr.co.ibk.web.controller;

import kr.co.ibk.common.annotation.CurrentUser;
import kr.co.ibk.domain.web.MemberInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
public class LearnController {

    @RequestMapping( "/soulGod/learn/dataManage")
    public String dataManage(Model model,
                        @CurrentUser MemberInfo memberInfo) {

        model.addAttribute("mc", "learn");
        model.addAttribute("pageTitle", "데이터 관리");

        return "/soulGod/learn/dataManage";

    }

    @RequestMapping( "/soulGod/learn/modelManage")
    public String modelManage(Model model,
                        @CurrentUser MemberInfo memberInfo) {

        model.addAttribute("mc", "learn");
        model.addAttribute("pageTitle", "모델 관리");

        return "/soulGod/learn/modelManage";

    }
    @RequestMapping( "/soulGod/learn/deployManage")
    public String deployManage(Model model,
                        @CurrentUser MemberInfo memberInfo) {

        model.addAttribute("mc", "learn");
        model.addAttribute("pageTitle", "배포 관리");

        return "/soulGod/learn/deployManage";

    }
}
