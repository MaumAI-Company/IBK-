package kr.co.ibk.web.controller;

import kr.co.ibk.common.annotation.CurrentUser;
import kr.co.ibk.domain.web.CardInputInfo;
import kr.co.ibk.domain.web.MemberInfo;
import kr.co.ibk.model.CardInputForm;
import kr.co.ibk.service.CardInputService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class ReportController {

    private final CardInputService cardInputService;

    @RequestMapping("/soulGod/report/card")
    public String card(Model model,
                       @ModelAttribute CardInputForm params) {

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
}
