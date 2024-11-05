package kr.co.ibk.web.controller;

import kr.co.ibk.common.annotation.CurrentUser;
import kr.co.ibk.domain.web.CardInputInfo;
import kr.co.ibk.domain.web.CardOutputInfo;
import kr.co.ibk.domain.web.MemberInfo;
import kr.co.ibk.model.CardInputForm;
import kr.co.ibk.model.CardOutputForm;
import kr.co.ibk.service.CardInputService;
import kr.co.ibk.service.CardOutputService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class ReportController {

    private final CardInputService cardInputService;
    private final CardOutputService cardOutputService;

    @RequestMapping("/soulGod/report/card")
    public String card(Model model,
                       @ModelAttribute CardInputForm params) {
        if (ObjectUtils.isEmpty(params.getSearchStartDate()) || ObjectUtils.isEmpty(params.getSearchEndDate())) {
            params.setSearchStartDate(String.valueOf(LocalDate.now().minusMonths(1)).replaceAll("-", "."));
            params.setSearchEndDate(String.valueOf(LocalDate.now()).replaceAll("-", "."));
        }

        List<CardInputInfo> excelList = cardInputService.list(params);
        model.addAttribute("excelList", excelList);

        List<CardInputInfo> list = cardInputService.page(params);

        model.addAttribute("list", list);
        model.addAttribute("pagingInfo", params.getPaginationInfo());
        model.addAttribute("params", params);
        model.addAttribute("mc", "ico_chart");
        model.addAttribute("pageTitle", "BC카드 지급결의 내역 조회");

        return "/soulGod/report/card";

    }

    @RequestMapping("/soulGod/report/taxInvoice")
    public String taxInvoice(Model model,
                             @CurrentUser MemberInfo memberInfo) {

        model.addAttribute("mc", "ico_chart");
        model.addAttribute("pageTitle", "세금계산서 지급결의 내역 조회");

        return "/soulGod/report/taxInvoice";

    }

    @RequestMapping("/soulGod/report/statistic")
    public String statistic(Model model,
                            @CurrentUser MemberInfo memberInfo) {

        model.addAttribute("mc", "ico_chart");
        model.addAttribute("pageTitle", "기간별 통계");

        return "/soulGod/report/statistic";

    }

    @ResponseBody
    @PostMapping(value = {"/soulGod/report/card/detail"})
    public CardOutputInfo cardDtail(@RequestBody CardOutputForm form) {
        return cardOutputService.detail(form);
    }
}
