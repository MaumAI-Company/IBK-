package kr.co.ibk.service;

import kr.co.ibk.domain.enums.InputColumnBillType;
import kr.co.ibk.domain.web.BillOutputInfo;
import kr.co.ibk.domain.web.LearningModelInputInfo;
import kr.co.ibk.model.BillInputForm;
import kr.co.ibk.repository.BillOutputRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BillOutputService extends _BaseService {

    private final BillOutputRepository billOutputRepository;

    public BillOutputInfo detail(BillInputForm params) {
        BillOutputInfo info = billOutputRepository.getDetail(params);

        List<LearningModelInputInfo> defaultInputInfos = new ArrayList<>();
        for (InputColumnBillType value : InputColumnBillType.values()) {
            LearningModelInputInfo defaultInput = new LearningModelInputInfo();
            defaultInput.setInputColumnBillType(value);
            defaultInputInfos.add(defaultInput);
        }

        defaultInputInfos.forEach(input -> {
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
        info.setInputList(defaultInputInfos);
        return info;
    }

}
