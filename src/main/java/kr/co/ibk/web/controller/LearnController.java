package kr.co.ibk.web.controller;

import kr.co.ibk.common.annotation.CurrentUser;
import kr.co.ibk.domain.enums.InputColumnType;
import kr.co.ibk.domain.enums.LearningType;
import kr.co.ibk.domain.enums.OutputColumnType;
import kr.co.ibk.domain.enums.SearchType;
import kr.co.ibk.domain.web.*;
import kr.co.ibk.model.*;
import kr.co.ibk.model.paging.PageDto;
import kr.co.ibk.service.*;
import kr.co.ibk.web.BaseCont;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class LearnController extends BaseCont {

    private final LearningModelService learningModelService;
    private final CardLearningDataService cardLearningDataService;
    private final BillLearningDataService billLearningDataService;
    private final TemplateService templateService;
    private final LearningDataService learningDataService;

    //학습데이터등록 : s
    @RequestMapping("/soulGod/learn/dataManage")
    public String dataManage(Model model,
                             @ModelAttribute CardLearningDataForm params) {
        /*if (!ObjectUtils.isEmpty(params.getSearchTypeJson())) {
            params.setSearchJsonMap(jsonToHashMap(params.getSearchTypeJson()));
        }

        List<CardLearningDataInfo> list = cardLearningDataService.page(params);

        model.addAttribute("list", list);
        model.addAttribute("pagingInfo", params.getPaginationInfo());*/

        params.setLearningType(LearningType.CARD);

        if (ObjectUtils.isEmpty(params.getSearchStartDate()) || ObjectUtils.isEmpty(params.getSearchEndDate())) {
            params.setSearchStartDate(String.valueOf(LocalDate.now().minusMonths(1)).substring(0, 7));
            params.setSearchEndDate(String.valueOf(LocalDate.now()).substring(0, 7));
        }

        if (ObjectUtils.isEmpty(params.getSearchTarget())) {
            params.setSearchTarget("1");
        }

        model.addAttribute("params", params);
        model.addAttribute("mc", "ico_database");
        model.addAttribute("pageTitle", "학습 데이터 등록");

        return "/soulGod/learn/dataManage";
    }

    @RequestMapping("/soulGod/learn/dataManageView")
    public String dataManageView(Model model,
                                 @ModelAttribute CardLearningDataForm params) {

        model.addAttribute("params", params);
        model.addAttribute("mc", "ico_database");
        model.addAttribute("pageTitle", "학습 데이터 조회");

        return "/soulGod/learn/dataManageView";
    }

    /**
     * 학습 데이터 목록 조회
     *
     * @param params
     * @return1
     */
    @ResponseBody
    @PostMapping("/soulGod/learn/dataManageView/list")
    public Map<String, Object> learnDataListView(@RequestBody CardLearningDataForm params) {
        if (!ObjectUtils.isEmpty(params.getSearchTypeJson())) {
            params.setSearchJsonMap(jsonToHashMap(params.getSearchTypeJson()));
        }
        List<CardLearningDataInfo> list = cardLearningDataService.page(params);

        PageDto pageDto = new PageDto(params.getPaginationInfo(), params.getRecordsPerPage());

        return Map.of(
                "list", list,
                "pageInfo", pageDto
        );
    }

    /**
     * 학습 데이터 목록 조회
     *
     * @param params
     * @return1
     */
    @ResponseBody
    @PostMapping("/soulGod/learn/dataManage/list")
    public Map<String, Object> learnDataList(@RequestBody CardLearningDataForm params) {
        if (!ObjectUtils.isEmpty(params.getSearchTypeJson())) {
            params.setSearchJsonMap(jsonToHashMap(params.getSearchTypeJson()));
        }

        params.setPagingAt("N");
        params.setLimitCnt(20);
        List<CardLearningDataInfo> list = cardLearningDataService.getList(params);

        return Map.of(
                "list", list,
                "params", params
        );
    }

    /**
     * 학습데이터명 중복 체크
     *
     * @param form
     * @return
     */
    @ResponseBody
    @PostMapping(value = {"/soulGod/learnData/nmCount"})
    public int learnDataNmCount(@RequestBody LearningDataForm form) {
        return learningDataService.learnDataNmCount(form);
    }

    @ResponseBody
    @PostMapping(value = {"/soulGod/learnData/save"})
    public HashMap<String, Object> learnDataSave(@RequestBody LearningDataForm form, @CurrentUser MemberInfo memberInfo) {

        return learningDataService.save(form, memberInfo);
    }
    //학습데이터등록 : e

    // 세금계산서 학습데이터등록 : s
    @GetMapping("/soulGod/learn/billManage")
    public String billManage(Model model,
                                 @ModelAttribute BillLearningDataForm params) {

        params.setLearningType(LearningType.BILL);

        if (ObjectUtils.isEmpty(params.getSearchStartDate()) || ObjectUtils.isEmpty(params.getSearchEndDate())) {
            params.setSearchStartDate(String.valueOf(LocalDate.now().minusMonths(1)).substring(0, 7));
            params.setSearchEndDate(String.valueOf(LocalDate.now()).substring(0, 7));
        }

        if (ObjectUtils.isEmpty(params.getSearchTarget())) {
            params.setSearchTarget("1");
        }

        model.addAttribute("params", params);
        model.addAttribute("mc", "ico_database");
        model.addAttribute("pageTitle", "세금계산서 학습 데이터 등록");

        return "/soulGod/learn/billManage";
    }

    /**
     * 세금계산서 학습데이터 등록 > 학습 데이터 목록 조회
     *
     * @param params
     * @return1
     */
    @ResponseBody
    @PostMapping("/soulGod/learn/billManage/list")
    public Map<String, Object> billLearnDataList(@RequestBody BillLearningDataForm params) {
        if (!ObjectUtils.isEmpty(params.getSearchTypeJson())) {
            params.setSearchJsonMap(jsonToHashMap(params.getSearchTypeJson()));
        }

        params.setPagingAt("N");
        params.setLimitCnt(20);
        List<BillLearningDataInfo> list = billLearningDataService.getList(params);

        return Map.of(
                "list", list,
                "params", params
        );
    }
    // 세금계산서 학습데이터등록 : e


    //학습템플릿관리 : s
    @RequestMapping("/soulGod/learn/templateManage")
    public String templateManage(Model model,
                                 @ModelAttribute TemplateForm params) {

        List<TemplateInfo> list = templateService.page(params);

        model.addAttribute("list", list);
        model.addAttribute("pagingInfo", params.getPaginationInfo());
        model.addAttribute("params", params);
        model.addAttribute("mc", "ico_database");
        model.addAttribute("pageTitle", "학습 템플릿 관리");

        return "/soulGod/learn/templateManage";
    }

    @ResponseBody
    @PostMapping(value = {"/soulGod/template/detail"})
    public HashMap<String, Object> templateDetail(@RequestBody TemplateForm form) {
        HashMap<String, Object> returnMap = new HashMap<>();
        returnMap.put("data", templateService.getLoad(form));
        return returnMap;
    }

    @ResponseBody
    @PostMapping(value = {"/soulGod/template/delete"})
    public HashMap<String, Object> templateDelete(@RequestBody TemplateForm form, @CurrentUser MemberInfo memberInfo) {
        return templateService.delete(form, memberInfo);
    }

    @ResponseBody
    @PostMapping(value = {"/soulGod/template/nmCount"})
    public int templateNmCount(@RequestBody TemplateForm form) {
        return templateService.templateNmCount(form);
    }
    //학습템플릿관리 : e


    //학습데이터관리 : s
    @RequestMapping("/soulGod/learn/learningDataManage")
    public String learningDataManage(Model model,
                                     @ModelAttribute LearningDataForm params) {

        List<LearningDataInfo> list = learningDataService.page(params);

        model.addAttribute("list", list);
        model.addAttribute("pagingInfo", params.getPaginationInfo());
        model.addAttribute("params", params);
        model.addAttribute("mc", "ico_database");
        model.addAttribute("pageTitle", "학습 데이터 관리");

        return "/soulGod/learn/learningDataManage";
    }

    @ResponseBody
    @PostMapping(value = {"/soulGod/learningDataManage/detail"})
    public HashMap<String, Object> learningDataManageDetail(@RequestBody TemplateForm form) {
        HashMap<String, Object> returnMap = new HashMap<>();
        returnMap.put("data", learningDataService.getLoad(form));
        return returnMap;
    }

    @ResponseBody
    @PostMapping(value = {"/soulGod/learningDataManage/save"})
    public HashMap<String, Object> learningDataManageSave(@RequestBody LearningModelForm form, @CurrentUser MemberInfo memberInfo) {

        return learningModelService.save(form, memberInfo);
    }
    //학습데이터관리 : e


    //모델관리 : s
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
    //모델관리 : e

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

}
