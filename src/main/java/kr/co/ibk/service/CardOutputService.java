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

import java.lang.reflect.Method;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CardOutputService extends _BaseService {

    private final CardOutputRepository cardOutputRepository;
    private final LearningModelInputRepository learningModelInputRepository;

    public CardOutputInfo detail(CardOutputForm params) {
        CardOutputInfo info = cardOutputRepository.getDetail(params);

        if (!ObjectUtils.isEmpty(info) && !ObjectUtils.isEmpty(info.getLearningModelId())) {
            List<LearningModelInputInfo> inputInfos = learningModelInputRepository.getPartList(info.getLearningModelId(), InOutGbnType.INPUT.name());
            if (!ObjectUtils.isEmpty(inputInfos)) {
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
            }
        }
        return info;
    }

}
