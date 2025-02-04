package kr.co.ibk.service;

import kr.co.ibk.domain.enums.InOutGbnType;
import kr.co.ibk.domain.enums.InputColumnBillType;
import kr.co.ibk.domain.enums.LearningType;
import kr.co.ibk.domain.web.BillOutputInfo;
import kr.co.ibk.domain.web.LearningModelInputInfo;
import kr.co.ibk.model.BillInputForm;
import kr.co.ibk.repository.BillOutputRepository;
import kr.co.ibk.repository.LearningModelInputRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BillOutputService extends _BaseService {

    private final BillOutputRepository billOutputRepository;
    private final LearningModelInputRepository learningModelInputRepository;

    public BillOutputInfo detail(BillInputForm params) {
        BillOutputInfo info = billOutputRepository.getDetail(params);

        List<LearningModelInputInfo> inputInfos = new ArrayList<>();

        if (!ObjectUtils.isEmpty(info.getLearningModelId())) {
            inputInfos = learningModelInputRepository.getPartList(info.getLearningModelId(), InOutGbnType.INPUT.name(), LearningType.BILL.getName());
        }

        List<LearningModelInputInfo> defaultInputList = new ArrayList<>();
        if (ObjectUtils.isEmpty(inputInfos)) {
            for (InputColumnBillType value : InputColumnBillType.values()) {
                LearningModelInputInfo defaultInput = new LearningModelInputInfo();
                defaultInput.setInputColumnBillType(value);
                defaultInputList.add(defaultInput);
            }
            inputInfos = defaultInputList;
        } else {
            for (LearningModelInputInfo inputInfo : inputInfos) {
                for (InputColumnBillType value : InputColumnBillType.values()) {
                    if (Objects.equals(inputInfo.getColName(), value.name())) {
                        LearningModelInputInfo defaultInput = new LearningModelInputInfo();
                        defaultInput.setInputColumnBillType(value);
                        defaultInputList.add(defaultInput);
                    }
                }
            }
            inputInfos = defaultInputList;
        }

        inputInfos.forEach(input -> {
            input.setInputColumnNm(input.getInputColumnBillType().getName());
            String columnName = input.getInputColumnBillType().getCamelColumn();

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
        return info;
    }

}
