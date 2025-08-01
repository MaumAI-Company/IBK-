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
import java.util.Map;
import java.util.stream.Collectors;

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
            inputInfos = learningModelInputRepository.getPartList(info.getLearningModelId(), InOutGbnType.INPUT.name(), LearningType.BILL.name());
        }

        if (ObjectUtils.isEmpty(inputInfos)) {
            List<LearningModelInputInfo> defaultInputList = new ArrayList<>();

            for (InputColumnBillType value : InputColumnBillType.values()) {
                LearningModelInputInfo defaultInput = new LearningModelInputInfo();
                defaultInput.setInputColumnBillType(value);
                defaultInputList.add(defaultInput);
            }
            inputInfos = defaultInputList;
        }

        Map<InputColumnBillType, LearningModelInputInfo> inputInfoMap = inputInfos.stream()
                .collect(Collectors.toMap(LearningModelInputInfo::getInputColumnBillType, infoItem -> infoItem));

        List<LearningModelInputInfo> inputList = new ArrayList<>();
        for (InputColumnBillType type : InputColumnBillType.values()) {
            LearningModelInputInfo input = new LearningModelInputInfo();
            input.setInputColumnBillType(type);
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
        return info;
    }

    @Transactional
    public void updateHitYn() {
        int updatedRows = billOutputRepository.updateHitYn();
        log.info("[BillOutputService] 세금계산서 updateHitYn 실행 - 업데이트된 ROW 수: {}", updatedRows);
    }

}
