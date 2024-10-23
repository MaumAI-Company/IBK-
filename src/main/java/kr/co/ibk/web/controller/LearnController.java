package kr.co.ibk.web.controller;

import kr.co.ibk.common.annotation.CurrentUser;
import kr.co.ibk.domain.web.MemberInfo;
import kr.co.ibk.model.LearningModelForm;
import kr.co.ibk.service.LearningModelService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;

@Controller
@RequiredArgsConstructor
public class LearnController {

    private final LearningModelService learningModelService;

    @RequestMapping("/soulGod/learn/dataManage")
    public String dataManage(Model model,
                             @CurrentUser MemberInfo memberInfo) {

        model.addAttribute("mc", "ico_database");
        model.addAttribute("pageTitle", "데이터 관리");

        return "/soulGod/learn/dataManage";

    }

    @RequestMapping("/soulGod/learn/modelManage")
    public String modelManage(Model model,
                              @CurrentUser MemberInfo memberInfo) {

        model.addAttribute("mc", "ico_database");
        model.addAttribute("pageTitle", "모델 관리");

        return "/soulGod/learn/modelManage";

    }

    @RequestMapping("/soulGod/learn/deployManage")
    public String deployManage(Model model,
                               @CurrentUser MemberInfo memberInfo) {

        model.addAttribute("mc", "ico_database");
        model.addAttribute("pageTitle", "배포 관리");

        return "/soulGod/learn/deployManage";

    }

    @ResponseBody
    @PostMapping(value = {"/soulGod/learnData/save"})
    public HashMap<String, Object> learnDataSave(@RequestBody LearningModelForm form, @CurrentUser MemberInfo memberInfo) {

        return learningModelService.save(form, memberInfo);
    }
}
