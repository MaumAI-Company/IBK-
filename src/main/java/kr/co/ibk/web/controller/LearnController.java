package kr.co.ibk.web.controller;

import kr.co.ibk.common.annotation.CurrentUser;
import kr.co.ibk.domain.enums.InputColumnType;
import kr.co.ibk.domain.enums.OutputColumnType;
import kr.co.ibk.domain.web.CardLearningDataInfo;
import kr.co.ibk.domain.web.LearningModelInfo;
import kr.co.ibk.domain.web.MemberInfo;
import kr.co.ibk.model.CardLearningDataForm;
import kr.co.ibk.model.LearningModelForm;
import kr.co.ibk.service.CardLearningDataService;
import kr.co.ibk.service.LearningModelService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class LearnController {

    private final LearningModelService learningModelService;
    private final CardLearningDataService cardLearningDataService;

    @RequestMapping("/soulGod/learn/dataManage")
    public String dataManage(Model model,
                             @ModelAttribute CardLearningDataForm form) {
        List<CardLearningDataInfo> list = cardLearningDataService.getList(form);

        model.addAttribute("list", list);
        model.addAttribute("mc", "ico_database");
        model.addAttribute("pageTitle", "데이터 관리");

        return "/soulGod/learn/dataManage";

    }

    @RequestMapping("/soulGod/learn/modelManage")
    public String modelManage(Model model,
                              @ModelAttribute LearningModelForm form,
                              @CurrentUser MemberInfo memberInfo) {

        List<LearningModelInfo> list = learningModelService.getList(form);

        model.addAttribute("list", list);
        model.addAttribute("params", form);
        model.addAttribute("mc", "ico_database");
        model.addAttribute("pageTitle", "모델 관리");

        return "/soulGod/learn/modelManage";

    }

    @RequestMapping("/soulGod/learn/deployManage")
    public String deployManage(Model model,
                               @ModelAttribute LearningModelForm form,
                               @CurrentUser MemberInfo memberInfo) {

        List<LearningModelInfo> list = learningModelService.getList(form);

        model.addAttribute("list", list);
        model.addAttribute("params", form);
        model.addAttribute("mc", "ico_database");
        model.addAttribute("pageTitle", "배포 관리");

        return "/soulGod/learn/deployManage";

    }

    @ResponseBody
    @PostMapping(value = {"/soulGod/learnData/save"})
    public HashMap<String, Object> learnDataSave(@RequestBody LearningModelForm form, @CurrentUser MemberInfo memberInfo) {

        return learningModelService.save(form, memberInfo);
    }

    @ResponseBody
    @PostMapping(value = {"/soulGod/learnData/learning"})
    public List<HashMap<String, Object>> dataLearning(@RequestBody LearningModelForm form, @CurrentUser MemberInfo memberInfo) {

        List<HashMap<String, Object>> rtnList = new ArrayList<>();
        if (form.getIdArr() != null) {
            for (Integer id : form.getIdArr()) {
                form.setId(id);
                HashMap<String, Object> learning = learningModelService.learning(form);
                rtnList.add(learning);
            }
        }
        return rtnList;
    }

    @ResponseBody
    @PostMapping(value = {"/soulGod/learnData/detail"})
    public HashMap<String, Object> learnDataDetail(@RequestBody LearningModelForm form) {
        HashMap<String, Object> returnMap = new HashMap<>();

        returnMap.put("data", learningModelService.getLoad(form));
        returnMap.put("inputArr", InputColumnType.values());
        returnMap.put("outputArr", OutputColumnType.values());

        return returnMap;
    }

}
