package kr.co.ibk.service;

import kr.co.ibk.domain.enums.InOutGbnType;
import kr.co.ibk.domain.web.CardOutputInfo;
import kr.co.ibk.domain.web.LearningModelInputInfo;
import kr.co.ibk.model.CardOutputForm;
import kr.co.ibk.repository.CardOutputRepository;
import kr.co.ibk.repository.LearningModelInputRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CardOutputService extends _BaseService {

    private final CardOutputRepository cardOutputRepository;
    private final LearningModelInputRepository learningModelInputRepository;

    public CardOutputInfo detail(CardOutputForm params) {
        CardOutputInfo info = cardOutputRepository.getDetail(params);

        /*if (ObjectUtils.isEmpty(params.getLoadType())) {
            info = cardOutputRepository.getDetail(params);
        } else {
            info = cardOutputRepository.getDetail(params);
        }*/

        if (!ObjectUtils.isEmpty(info) && !ObjectUtils.isEmpty(info.getLearningModelId())) {
            List<LearningModelInputInfo> inputInfos = learningModelInputRepository.getPartList(info.getLearningModelId(), InOutGbnType.INPUT.name());
            if (!ObjectUtils.isEmpty(inputInfos)) {
                inputInfos.forEach(input -> {
                    input.setInputColumnNm(input.getInputColumnType().getName());
                });
                info.setInputList(inputInfos);
            }

            /*List<LearningModelInputInfo> outputInfos = learningModelInputRepository.getPartList(info.getLearningModelId(), InOutGbnType.OUTPUT.name());
            if (!ObjectUtils.isEmpty(outputInfos)) {
                outputInfos.forEach(output -> {
                    output.setOutputColumnNm(output.getOutputColumnType().getName());
                });
            }*/

//            info.setInputList(learningModelInputRepository.getPartList(info.getLearningModelId(), InOutGbnType.INPUT.name()));
//            info.setOutputList(learningModelInputRepository.getPartList(info.getLearningModelId(), InOutGbnType.OUTPUT.name()));
        }

        return info;
    }

}
