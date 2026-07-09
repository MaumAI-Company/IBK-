package kr.co.ibk.web.rest;

import kr.co.ibk.common.ResponseDto;
import kr.co.ibk.domain.enums.ResultCodeType;
import kr.co.ibk.domain.enums.StatisticTargetType;
import kr.co.ibk.domain.web.LearningSchedulerInfo;
import kr.co.ibk.service.AiPrfrStatService;
import kr.co.ibk.service.LearningSchedulerService;
import kr.co.ibk.service.UserUsageStatService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.OffsetDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/o/batch")
@RequiredArgsConstructor
public class BatchRestController {

    private final LearningSchedulerService learningSchedulerService;
    private final UserUsageStatService userUsageStatService;
    private final AiPrfrStatService aiPrfrStatService;


    @GetMapping("/callSchedule")
    public ResponseEntity<?> callSchedule() {
        List<LearningSchedulerInfo> batchList = learningSchedulerService.getBatchList();

        return new ResponseEntity<>(new ResponseDto<>(ResultCodeType.SUCCESS, "성공", batchList), HttpStatus.OK);
    }

    @GetMapping("/_call/updateStatisticByRange/onlyAllType")
    public ResponseEntity<?> updateStatisticByRange(@RequestParam(value = "type", required = false) StatisticTargetType type) {
        // 수동 배치
        userUsageStatService.updateStatisticByRange();
        aiPrfrStatService.updateStatisticByRange();

        return ResponseEntity.ok(new ResponseDto<>(ResultCodeType.SUCCESS, "기간 통계 수동 배치 성공", null));
    }

    @GetMapping("/_call/updateStatisticByAll")
    public ResponseEntity<?> updateStat(@RequestParam(value = "type", required = false) StatisticTargetType type) {
        // 수동 배치
        userUsageStatService.updateStatisticByAll(type);
        aiPrfrStatService.updateStatisticByAll(type);

        // 결과 값
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("type", type == null ? "ALL" : type.name());
        data.put("timestamp", OffsetDateTime.now().toString());

        return ResponseEntity.ok(new ResponseDto<>(ResultCodeType.SUCCESS, "통계 수동 배치 성공", data));
    }

}
