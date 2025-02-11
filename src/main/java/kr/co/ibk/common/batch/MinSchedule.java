package kr.co.ibk.common.batch;

import kr.co.ibk.domain.web.LearningSchedulerInfo;
import kr.co.ibk.model.LearningModelForm;
import kr.co.ibk.service.LearningModelService;
import kr.co.ibk.service.LearningSchedulerService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;


@Component
@RequiredArgsConstructor
public class MinSchedule {

    private final LearningSchedulerService learningSchedulerService;
    private final LearningModelService learningModelService;

    //@Scheduled(fixedRate = 60000) // 60초마다 실행
    public void schedulerBatch() {
        List<LearningSchedulerInfo> batchList = learningSchedulerService.getBatchList();
        // 학습 api 실행
        /*if (!batchList.isEmpty()) {
            for (Integer schedId : batchList) {
                LearningModelForm form = new LearningModelForm();
                form.setLearningId(schedId);
                learningModelService.learning(form);
            }
        }*/
    }
}
