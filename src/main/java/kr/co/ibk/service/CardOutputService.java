package kr.co.ibk.service;

import kr.co.ibk.domain.enums.InOutGbnType;
import kr.co.ibk.domain.enums.InputColumnCardType;
import kr.co.ibk.domain.enums.LearningType;
import kr.co.ibk.domain.web.CardOutputInfo;
import kr.co.ibk.domain.web.LearningModelInputInfo;
import kr.co.ibk.model.CardInputForm;
import kr.co.ibk.repository.CardInputRepository;
import kr.co.ibk.repository.CardOutputRepository;
import kr.co.ibk.repository.LearningModelInputRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CardOutputService extends _BaseService {

    private final CardInputRepository cardInputRepository;
    private final CardOutputRepository cardOutputRepository;
    private final LearningModelInputRepository learningModelInputRepository;

    public CardOutputInfo detail(CardInputForm params) {
        CardOutputInfo info = cardOutputRepository.getDetail(params);
        List<LearningModelInputInfo> inputInfos = new ArrayList<>();

        if (!ObjectUtils.isEmpty(info.getLearningModelId())) {
            inputInfos = learningModelInputRepository.getPartList(info.getLearningModelId(), InOutGbnType.INPUT.name(), LearningType.CARD.name());
        }

        if (ObjectUtils.isEmpty(inputInfos)) {
            List<LearningModelInputInfo> defaultInputList = new ArrayList<>();

            for (InputColumnCardType value : InputColumnCardType.values()) {
                LearningModelInputInfo defaultInput = new LearningModelInputInfo();
                defaultInput.setInputColumnCardType(value);
                defaultInputList.add(defaultInput);
            }
            inputInfos = defaultInputList;
        }

        Map<InputColumnCardType, LearningModelInputInfo> inputInfoMap = inputInfos.stream()
                .collect(Collectors.toMap(LearningModelInputInfo::getInputColumnCardType, infoItem -> infoItem));

        List<LearningModelInputInfo> inputList = new ArrayList<>();
        for (InputColumnCardType type : InputColumnCardType.values()) {
            LearningModelInputInfo input = new LearningModelInputInfo();
            input.setInputColumnCardType(type);
            input.setInputColumnNm(type.getName());

            if (inputInfoMap.containsKey(type)) {
                String columnName = type.getCamelColumn();
                try {
                    String getterMethodName = "get" + columnName.substring(0, 1).toUpperCase() + columnName.substring(1);
                    Method method = info.getClass().getMethod(getterMethodName);
                    Object columnValue = method.invoke(info);
                    input.setInputColumnVal(columnValue != null ? columnValue.toString() : null);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                input.setInputColumnVal("-");
            }
            inputList.add(input);
        }

        info.setInputList(inputList);


        //이전/다음글 no find
        /*for (int i = 0; i < 2; i++) {
            params.setLoadType(i == 0 ? "prev" : "next");

            if (ObjectUtils.isEmpty(params.getSorting())) {
                params.setSorting("desc");
            }
            CardInputInfo inputInfo = cardInputRepository.getInputKey(params);
            if (!ObjectUtils.isEmpty(inputInfo)) {
                params.setTstmYmd(inputInfo.getTstmYmd());
                params.setTstmNo(inputInfo.getTstmNo());
                params.setBrcd(inputInfo.getBrcd());

                CardOutputInfo prevAndNext = cardOutputRepository.getDetail(params);
                if (i == 0) {
                    info.setPrevNo(prevAndNext.getNo());
                } else {
                    info.setNextNo(prevAndNext.getNo());
                }
            }
        }*/

        return info;
    }

    @Transactional
    public void updateHitYn() {
        int updatedRows = cardOutputRepository.updateHitYn();
        log.info("[CardOutputService] 카드 updateHitYn 실행 - 업데이트된 ROW 수: {}", updatedRows);
    }

}
