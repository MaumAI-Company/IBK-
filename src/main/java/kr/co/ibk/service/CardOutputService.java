package kr.co.ibk.service;

import kr.co.ibk.domain.enums.InOutGbnType;
import kr.co.ibk.domain.enums.InputColumnType;
import kr.co.ibk.domain.web.CardInputInfo;
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
            inputInfos = learningModelInputRepository.getPartList(info.getLearningModelId(), InOutGbnType.INPUT.name());
        }
        if (ObjectUtils.isEmpty(inputInfos)) {
            List<LearningModelInputInfo> defaultInputList = new ArrayList<>();

            for (InputColumnType value : InputColumnType.values()) {
                LearningModelInputInfo defaultInput = new LearningModelInputInfo();
                defaultInput.setInputColumnType(value);
                defaultInputList.add(defaultInput);
            }
            inputInfos = defaultInputList;
        }

        inputInfos.forEach(input -> {
            input.setInputColumnNm(input.getInputColumnType().getName());

            String columnName = input.getInputColumnType().getCamelColumn();

            // info 객체의 해당 메서드 호출
            try {
                // 메서드 이름 생성
                String getterMethodName = "get" + columnName.substring(0, 1).toUpperCase() + columnName.substring(1);
                Method method = info.getClass().getMethod(getterMethodName);

                // 메서드 호출하여 값 가져오기
                Object columnValue = method.invoke(info);
                input.setInputColumnVal(columnValue != null ? columnValue.toString() : null);

            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        info.setInputList(inputInfos);


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

}
