package kr.co.ibk.web.rest;

import kr.co.ibk.common.ResponseDto;
import kr.co.ibk.domain.enums.ResultCodeType;
import kr.co.ibk.domain.web.LearningSchedulerInfo;
import kr.co.ibk.service.LearningSchedulerService;
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


    @GetMapping("/callSchedule")
    public ResponseEntity<?> callSchedule() {
        List<LearningSchedulerInfo> batchList = learningSchedulerService.getBatchList();

        return new ResponseEntity<>(new ResponseDto<>(ResultCodeType.SUCCESS, "성공", batchList), HttpStatus.OK);
    }
}
