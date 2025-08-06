package kr.co.ibk.web.controller;

import kr.co.ibk.domain.web.*;
import kr.co.ibk.model.BillInputForm;
import kr.co.ibk.model.CardInputForm;
import kr.co.ibk.model.StatisticInfoForm;
import kr.co.ibk.model.paging.PageDto;
import kr.co.ibk.service.*;
import kr.co.ibk.web.BaseCont;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class ReportController extends BaseCont {

    private final CardInputService cardInputService;
    private final CardOutputService cardOutputService;
    private final BillInputService billInputService;
    private final BillOutputService billOutputService;
    private final StatisticService statisticService;

    // BC카드 지급결의 내역 조회 : s
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
        model.addAttribute("pagingInfo", params.getPaginationInfo());
        model.addAttribute("params", params);
        model.addAttribute("mc", "ico_chart");
        model.addAttribute("pageTitle", "BC카드 지급결의 내역 조회");

        return "/soulGod/report/card";

    }

    @ResponseBody
    @PostMapping("/soulGod/report/card/list")
    public Map<String, Object> getCardList(@RequestBody CardInputForm params) {

        if (!ObjectUtils.isEmpty(params.getSearchTypeJson())) {
            params.setSearchJsonMap(jsonToHashMap(params.getSearchTypeJson()));
            params.setSearchRegex(makeSearchQuery(params.getSearchJsonMap(), 1));
        }

        List<CardInputInfo> list = cardInputService.page(params);
        PageDto pageDto = new PageDto(params.getPaginationInfo(), params.getRecordsPerPage());

        return Map.of(
                "list", list,
                "pageInfo", pageDto
        );
    }

    @GetMapping("/soulGod/report/card/xlsDown")
    public void reportCardExcelDown(@RequestParam(name = "searchStartDate", required = false) String searchStartDate,
                                    @RequestParam(name = "searchEndDate", required = false) String searchEndDate,
                                    @RequestParam(name = "searchTarget", required = false) String searchTarget,
                                    @RequestParam(name = "searchTypeJson", required = false) String searchTypeJson,
                                    @RequestParam(name = "sorting", required = false) String sorting,
                                    HttpServletResponse response) throws IOException {

        CardInputForm params = new CardInputForm();
        params.setSearchStartDate(searchStartDate);
        params.setSearchEndDate(searchEndDate);
        params.setSearchTarget(searchTarget);

        if (!ObjectUtils.isEmpty(searchTypeJson)) {
            params.setSearchJsonMap(jsonToHashMap(searchTypeJson));
            params.setSearchRegex(makeSearchQuery(params.getSearchJsonMap(), 1));
        }

        if (!ObjectUtils.isEmpty(sorting)) {
            params.setSorting(sorting);
        }
        List<CardInputInfo> excelList = cardInputService.list(params);
        cardInputService.reportCardExcelDown(response, excelList);
    }

    @ResponseBody
    @PostMapping(value = {"/soulGod/report/card/detail"})
    public CardOutputInfo cardDtail(@RequestBody CardInputForm params) {
        if (!ObjectUtils.isEmpty(params.getSearchTypeJson())) {
            params.setSearchJsonMap(jsonToHashMap(params.getSearchTypeJson()));
        }
        return cardOutputService.detail(params);
    }
    // BC카드 지급결의 내역 조회 : e

    // 세금계산서 지급결의 내역 조회 : s
    @RequestMapping("/soulGod/report/taxInvoice")
    public String taxInvoice(Model model,
                             @ModelAttribute BillInputForm params) {
        if (!"Y".equals(params.getSearchAllDateAt()) &&
                (ObjectUtils.isEmpty(params.getSearchStartDate()) || ObjectUtils.isEmpty(params.getSearchEndDate()))) {
            params.setSearchStartDate(String.valueOf(LocalDate.now().minusMonths(1)).replaceAll("-", "."));
            params.setSearchEndDate(String.valueOf(LocalDate.now()).replaceAll("-", "."));
        }

        if (!ObjectUtils.isEmpty(params.getSearchTypeJson())) {
            params.setSearchJsonMap(jsonToHashMap(params.getSearchTypeJson()));
        }

        model.addAttribute("pagingInfo", params.getPaginationInfo());
        model.addAttribute("params", params);
        model.addAttribute("mc", "ico_chart");
        model.addAttribute("pageTitle", "세금계산서 지급결의 내역 조회");

        return "/soulGod/report/taxInvoice";
    }

    @ResponseBody
    @PostMapping("/soulGod/report/bill/list")
    public Map<String, Object> getBillList(@RequestBody BillInputForm params) {

        if (!ObjectUtils.isEmpty(params.getSearchTypeJson())) {
            params.setSearchJsonMap(jsonToHashMap(params.getSearchTypeJson()));
            params.setSearchRegex(makeSearchQuery(params.getSearchJsonMap(), 2));
        }

        List<BillInputInfo> list = billInputService.page(params);
        PageDto pageDto = new PageDto(params.getPaginationInfo(), params.getRecordsPerPage());

        return Map.of(
                "list", list,
                "pageInfo", pageDto
        );
    }

    @ResponseBody
    @PostMapping(value = {"/soulGod/report/bill/detail"})
    public BillOutputInfo billDetail(@RequestBody BillInputForm params) {
        if (!ObjectUtils.isEmpty(params.getSearchTypeJson())) {
            params.setSearchJsonMap(jsonToHashMap(params.getSearchTypeJson()));
        }
        return billOutputService.detail(params);
    }

    @GetMapping("/soulGod/report/bill/xlsDown")
    public void reportBillExcelDown(@RequestParam(name = "searchStartDate", required = false) String searchStartDate,
                                    @RequestParam(name = "searchEndDate", required = false) String searchEndDate,
                                    @RequestParam(name = "searchTarget", required = false) String searchTarget,
                                    @RequestParam(name = "searchTypeJson", required = false) String searchTypeJson,
                                    @RequestParam(name = "sorting", required = false) String sorting,
                                    HttpServletResponse response) throws IOException {

        BillInputForm params = new BillInputForm();
        params.setSearchStartDate(searchStartDate);
        params.setSearchEndDate(searchEndDate);
        params.setSearchTarget(searchTarget);

        if (!ObjectUtils.isEmpty(searchTypeJson)) {
            params.setSearchJsonMap(jsonToHashMap(searchTypeJson));
            params.setSearchRegex(makeSearchQuery(params.getSearchJsonMap(), 2));
        }

        if (!ObjectUtils.isEmpty(sorting)) {
            params.setSorting(sorting);
        }
        List<BillInputInfo> excelList = billInputService.list(params);
        billInputService.reportBillExcelDown(response, excelList);
    }
    // 세금계산서 지급결의 내역 조회 : e

    // 기간별 통계 : s
    @RequestMapping("/soulGod/report/statistic")
    public String statistic(Model model,
                            @ModelAttribute StatisticInfoForm params) {

        if (ObjectUtils.isEmpty(params.getSearchStartDate()) || ObjectUtils.isEmpty(params.getSearchEndDate())) {
            params.setSearchStartDate(LocalDate.now().withDayOfMonth(1).format(DateTimeFormatter.ofPattern("yyyy.MM.dd")));
            params.setSearchEndDate(LocalDate.now().withDayOfMonth(LocalDate.now().lengthOfMonth()).format(DateTimeFormatter.ofPattern("yyyy.MM.dd")));
        }

        /*if (ObjectUtils.isEmpty(params.getSearchLearningType())) {
            params.setSearchLearningType("CARD");
        }*/

        model.addAttribute("mc", "ico_chart");
        model.addAttribute("params", params);
        model.addAttribute("pageTitle", "기간별 통계");

        return "/soulGod/report/statistic";

    }

    /**
     * 기간별 통계 > 추론 결과 수(INPUT 데이터 기반 OUTPUT 데이터 생성 개수)
     */
    @ResponseBody
    @PostMapping("/soulGod/report/statistic/input")
    public List<StatisticInfo> inferResultStatistic(@RequestBody StatisticInfoForm form) {
        return statisticService.inferResultStatistic(form);
    }

    /**
     * 기간별 통계 > 사용자 활용 현황(INPUT 데이터 기반 OUTPUT 데이터 생성 개수)
     */
    @ResponseBody
    @PostMapping("/soulGod/report/statistic/output")
    public List<StatisticInfo> usageStatistic(@RequestBody StatisticInfoForm form) {
        return statisticService.usageStatistic(form);
    }

    /**
     * 기간별 통계 > AI 사용 지급결의 사용 개수 (지급결의일자 기준)
     */
    @ResponseBody
    @PostMapping("/soulGod/report/statistic/aiPrfr")
    public List<StatisticInfo> aiPrfrStatistic(@RequestBody StatisticInfoForm form) {
        return statisticService.aiPrfrStatistic(form);
    }
    // 기간별 통계 : e
}

