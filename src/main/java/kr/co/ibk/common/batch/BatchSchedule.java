package kr.co.ibk.common.batch;

import kr.co.ibk.domain.enums.InOutGbnType;
import kr.co.ibk.domain.web.LearningSchedulerInfo;
import kr.co.ibk.domain.web.TemplateInfo;
import kr.co.ibk.model.LearningDataForm;
import kr.co.ibk.model.LearningModelForm;
import kr.co.ibk.model.TemplateForm;
import kr.co.ibk.repository.*;
import kr.co.ibk.service.*;
import kr.co.ibk.web.BaseCont;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Component
@RequiredArgsConstructor
public class BatchSchedule extends BaseCont {

    private final LearningModelInputRepository learningModelInputRepository;
    private final LearningModelRepository learningModelRepository;
    private final TemplateInputRepository templateInputRepository;
    private final LearningDataInputRepository learningDataInputRepository;
    private final LearningDataRepository learningDataRepository;
    private final TemplateService templateService;
    private final LearningSchedulerService learningSchedulerService;
    private final LearningModelService learningModelService;

    private final CardOutputService cardOutputService;
    private final BillOutputService billOutputService;

    private final UserUsageStatService userUsageStatService;
    private final AiPrfrStatService aiPrfrStatService;

    // 등록자 설정
    private final String REG_ID = "admin";

    @Value("${Globals.check.scheduler}")
    private Boolean schedulerCheck;

    @Value("${Globals.check.hit}")
    private Boolean hitCheck;

    @Value("${Globals.check.statistic}")
    private Boolean statisticCheck;

    private Boolean statisticByRangeCheck;
    @Value("${Globals.check.statistic.range}")

    /**
     * 학습 스케줄러 실행
     * 매 10분 마다 실행
     */
    @Scheduled(cron = "0 */10 * * * *")
    public void schedulerBatch() {
        if (schedulerCheck) {
            List<LearningSchedulerInfo> batchList = learningSchedulerService.getBatchList();
            for (LearningSchedulerInfo info : batchList) {
                // 템플릿 조회
                TemplateForm templateForm = new TemplateForm();
                templateForm.setId(info.getTemplateId());
                TemplateInfo templateInfo = templateService.getLoad(templateForm);
                List<Map<String, Object>> inputArr = templateInputInfoToMap(templateInputRepository.getPartList(info.getTemplateId(), InOutGbnType.INPUT.name()));
                List<Map<String, Object>> outputArr = templateInputInfoToMap(templateInputRepository.getPartList(info.getTemplateId(), InOutGbnType.OUTPUT.name()));

                // 학습 데이터 등록
                LearningDataForm dataForm = new LearningDataForm();
                dataForm.setMemId(REG_ID);
                dataForm.setSchedId(info.getSchedId());
                int runCnt = info.getRunCnt() + 1;
                dataForm.setDataName("[배치] " + info.getSchedNm() + "_" + String.format("%03d", runCnt));
                dataForm.setLearningType(templateInfo.getLearningType());
                dataForm.setSelectCon(templateInfo.getSelectCon());
                dataForm.setHdqrBobDcd(templateInfo.getHdqrBobDcd());
                dataForm.setTemplateId(info.getTemplateId());

                HashMap<String, String> map = jsonToHashMap(templateInfo.getSelectCon());
                map.put("searchStartDate", LocalDateTime.now().minusMonths(Long.parseLong(info.getSearchMm())).format(DateTimeFormatter.ofPattern("yyyy-MM")));
                map.put("searchEndDate", LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM")));

                String selectCon = hashMapToJson(map);
                dataForm.setSelectCon(selectCon);

                learningDataRepository.insert(dataForm);
                learningDataInputRepository.insertList(dataForm.getId(), inputArr, InOutGbnType.INPUT.name());
                learningDataInputRepository.insertList(dataForm.getId(), outputArr, InOutGbnType.OUTPUT.name());

                // 모델 등록
                LearningModelForm modelForm = new LearningModelForm();
                modelForm.setModId(REG_ID);
                modelForm.setRegId(REG_ID);
                modelForm.setDataId(dataForm.getId());
                modelForm.setLearnName("[배치] " + info.getSchedNm() + "_" + String.format("%03d", runCnt));
                modelForm.setBatchSize(info.getBatchSize());
                modelForm.setEpoch(info.getEpoch());
                modelForm.setLearningRate(info.getLearningRate());
                modelForm.setHdqrBobDcd(templateInfo.getHdqrBobDcd());
                modelForm.setLearningType(templateInfo.getLearningType());
                learningModelRepository.insert(modelForm);
                learningModelInputRepository.insertList(modelForm.getId(), inputArr, InOutGbnType.INPUT.name());
                learningModelInputRepository.insertList(modelForm.getId(), outputArr, InOutGbnType.OUTPUT.name());

                // 학습 api 실행
                LearningModelForm form = new LearningModelForm();
                form.setId(modelForm.getId());
                learningModelService.learning(form);
            }
        }
    }

    /**
     * - 설정한 스케줄 마다 기간으로 통계 업데이트
     * - hit 배치 스케줄러와 상관 없이 실행 가능
     * - 속성
     * *　　　　　　*　　　　　　  *　　　　　　*　　　　　　*
     * 0      분(0-59)　　시간(0-23)　　일(1-31)　　월(1-12)　　요일(0-7)
     */
    @Scheduled(cron = "0 ${Globals.batch.statistic.cron.min} ${Globals.batch.statistic.cron.hour} ${Globals.batch.statistic.cron.day} ${Globals.batch.statistic.cron.mon} ${Globals.batch.statistic.cron.week}")
    public void statisticByRangeBatch() {
        if (statisticByRangeCheck) {
            userUsageStatService.updateStatisticByRange();
            aiPrfrStatService.updateStatisticByRange();
        }
    }

    /**
     * - 설정한 스케줄 마다 통계 업데이트
     * - 해당 작업은 hit 배치보다 최소 30분 먼저 실행되어야 함 (적중 수 업데이트 전 NULL 조건으로 통계 업데이트 하기 위함)
     * - 속성
     * *　　　　　　*　　　　　　  *　　　　　　*　　　　　　*
     * 0      분(0-59)　　시간(0-23)　　일(1-31)　　월(1-12)　　요일(0-7)
     */
    @Scheduled(cron = "0 ${Globals.batch.statistic.cron.min} ${Globals.batch.statistic.cron.hour} ${Globals.batch.statistic.cron.day} ${Globals.batch.statistic.cron.mon} ${Globals.batch.statistic.cron.week}")
    public void statisticBatch() {
        if (statisticCheck) {
            userUsageStatService.updateStatistic();
            aiPrfrStatService.updateStatistic();
        }
    }

    /**
     * - 설정한 스케줄 마다 적중수 업데이트
     * - 속성
     * *　　　　　　*　　　　　　  *　　　　　　*　　　　　　*
     * 0      분(0-59)　　시간(0-23)　　일(1-31)　　월(1-12)　　요일(0-7)
     */
    @Scheduled(cron = "0 ${Globals.batch.hit.cron.min} ${Globals.batch.hit.cron.hour} ${Globals.batch.hit.cron.day} ${Globals.batch.hit.cron.mon} ${Globals.batch.hit.cron.week}")
    public void hitBatch() {
        if (hitCheck) {
            cardOutputService.updateHitYn();
            billOutputService.updateHitYn();
        }
    }
}
