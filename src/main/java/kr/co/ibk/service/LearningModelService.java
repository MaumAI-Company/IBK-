package kr.co.ibk.service;

import kr.co.ibk.domain.web.MemberInfo;
import kr.co.ibk.model.LearningModelForm;
import kr.co.ibk.repository.LearningModelRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.time.LocalDateTime;
import java.util.HashMap;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class LearningModelService extends _BaseService {

    private final LearningModelRepository learningModelRepository;

    public HashMap<String, Object> save(LearningModelForm form, MemberInfo memberInfo) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("status", "FAIL");

        Long saveCnt = 0L;

        if (ObjectUtils.isEmpty(form.getId())) {
            //insert
            form.setRegId(memberInfo.getMemId());
            form.setRegDt(LocalDateTime.now());
            form.setModId(memberInfo.getMemId());
            form.setModDt(LocalDateTime.now());
            //LearningModelInfo info = learningModelRepository.register(form);

            //rel input insert


        } else {
            //update

            //rel input delete > insert
        }

        if (saveCnt > 0) {
            map.put("status", "SUCCESS");
        }
        return map;
    }

}
