package kr.co.ibk.web.controller;

import kr.co.ibk.common.annotation.CurrentUser;
import kr.co.ibk.domain.web.CardLearningDataInfo;
import kr.co.ibk.domain.web.MemberInfo;
import kr.co.ibk.model.CardLearningDataForm;
import kr.co.ibk.service.CardLearningDataService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class ReportController {

    private final CardLearningDataService cardLearningDataService;

    @RequestMapping("/soulGod/report/card")
    public String card(Model model,
                       @ModelAttribute CardLearningDataForm form) {

        List<CardLearningDataInfo> list = cardLearningDataService.getList(form);

        model.addAttribute("list", list);
        model.addAttribute("params", form);
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
