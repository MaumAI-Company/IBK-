package kr.co.ibk.web.controller;

import kr.co.ibk.common.annotation.CurrentUser;
import kr.co.ibk.domain.web.CardInputInfo;
import kr.co.ibk.domain.web.CardOutputInfo;
import kr.co.ibk.domain.web.MemberInfo;
import kr.co.ibk.model.CardInputForm;
import kr.co.ibk.service.CardInputService;
import kr.co.ibk.service.CardOutputService;
import kr.co.ibk.web.BaseCont;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.time.LocalDate;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class ReportController extends BaseCont {

    private final CardInputService cardInputService;
    private final CardOutputService cardOutputService;

    @RequestMapping("/soulGod/report/card")
    public String card(Model model,
                       @ModelAttribute CardInputForm params) {
        if (!"Y".equals(params.getSearchAllDateAt()) &&
                (ObjectUtils.isEmpty(params.getSearchStartDate()) || ObjectUtils.isEmpty(params.getSearchEndDate()))) {
            params.setSearchStartDate(String.valueOf(LocalDate.now().minusMonths(1)).replaceAll("-", "."));
            params.setSearchEndDate(String.valueOf(LocalDate.now()).replaceAll("-", "."));
        }

        if (!ObjectUtils.isEmpty(params.getSearchTypeJson())) {
            params.setSearchJsonMap(jsonToHashMap(params.getSearchTypeJson()));
        }

        List<CardInputInfo> list = cardInputService.page(params);

        model.addAttribute("list", list);
        model.addAttribute("pagingInfo", params.getPaginationInfo());
        model.addAttribute("params", params);
        model.addAttribute("mc", "ico_chart");
        model.addAttribute("pageTitle", "BC카드 지급결의 내역 조회");

        return "/soulGod/report/card";

    }

    @GetMapping("/soulGod/report/card/xlsDown")
    public void reportCardExcelDown(@RequestParam(name = "searchStartDate", required = false) String searchStartDate,
                                    @RequestParam(name = "searchEndDate", required = false) String searchEndDate,
                                    @RequestParam(name = "searchTarget", required = false) String searchTarget,
                                    @RequestParam(name = "searchTypeJson", required = false) String searchTypeJson,
                                    @RequestParam(name = "sorting", required = false) String sorting,
                                    HttpServletResponse response) throws UnsupportedEncodingException {

        CardInputForm params = new CardInputForm();
        params.setSearchStartDate(searchStartDate);
        params.setSearchEndDate(searchEndDate);
        params.setSearchTarget(searchTarget);

        if (!ObjectUtils.isEmpty(searchTypeJson)) {
            params.setSearchJsonMap(jsonToHashMap(searchTypeJson));
        }

        if (!ObjectUtils.isEmpty(sorting)) {
            params.setSorting(sorting);
        }
        List<CardInputInfo> excelList = cardInputService.list(params);
        cardInputService.reportCardExcelDown(response, excelList);
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
    public CardOutputInfo cardDtail(@RequestBody CardInputForm params) {
        if (!ObjectUtils.isEmpty(params.getSearchTypeJson())) {
            params.setSearchJsonMap(jsonToHashMap(params.getSearchTypeJson()));
        }
        return cardOutputService.detail(params);
    }
}
