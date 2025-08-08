package kr.co.ibk.web.rest;

import kr.co.ibk.common.ResponseDto;
import kr.co.ibk.domain.enums.ResultCodeType;
import kr.co.ibk.domain.web.LearningSchedulerInfo;
import kr.co.ibk.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/o/batch")
@RequiredArgsConstructor
public class TestRestController {

    private final LearningSchedulerService learningSchedulerService;
    private final CardOutputService cardOutputService;
    private final BillOutputService billOutputService;
    private final UserUsageStatService userUsageStatService;
    private final AiPrfrStatService aiPrfrStatService;


    @GetMapping("/callSchedule")
    public ResponseEntity<?> callSchedule() {
        List<LearningSchedulerInfo> batchList = learningSchedulerService.getBatchList();

        return new ResponseEntity<>(new ResponseDto<>(ResultCodeType.SUCCESS, "성공", batchList), HttpStatus.OK);
    }

    @GetMapping("/updateStat")
    public ResponseEntity<?> updateStat() {
        userUsageStatService.updateStatistic();
        aiPrfrStatService.updateStatistic();
        return new ResponseEntity<>(new ResponseDto<>(ResultCodeType.SUCCESS, "성공", null), HttpStatus.OK);
    }

    @GetMapping("/updateStatisticByRange")
    public ResponseEntity<?> updateStatisticByRange() {
        userUsageStatService.updateStatisticByRange();
        aiPrfrStatService.updateStatisticByRange();
        return new ResponseEntity<>(new ResponseDto<>(ResultCodeType.SUCCESS, "성공", null), HttpStatus.OK);
    }

    @GetMapping("/updateHit")
    public ResponseEntity<?> updateHit() {
        cardOutputService.updateHitYn();
        billOutputService.updateHitYn();
        return new ResponseEntity<>(new ResponseDto<>(ResultCodeType.SUCCESS, "성공", null), HttpStatus.OK);
    }

}
