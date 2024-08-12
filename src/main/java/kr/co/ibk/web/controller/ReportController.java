package kr.co.ibk.web.controller;

import kr.co.ibk.common.annotation.CurrentUser;
import kr.co.ibk.domain.web.Account;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
public class ReportController {

    @RequestMapping( "/soulGod/report/card")
    public String card(Model model,
                        @CurrentUser Account account) {

        model.addAttribute("mc", "report");
        model.addAttribute("pageTitle", "BC카드 지급결의 내역 조회");

        return "/soulGod/report/card";

    }

    @RequestMapping( "/soulGod/report/taxInvoice")
    public String taxInvoice(Model model,
                        @CurrentUser Account account) {

        model.addAttribute("mc", "report");
        model.addAttribute("pageTitle", "세금계산서 지급결의 내역 조회");

        return "/soulGod/report/taxInvoice";

    }
    @RequestMapping( "/soulGod/report/statistic")
    public String statistic(Model model,
                        @CurrentUser Account account) {

        model.addAttribute("mc", "report");
        model.addAttribute("pageTitle", "기간별 통계");

        return "/soulGod/report/statistic";

    }
}
