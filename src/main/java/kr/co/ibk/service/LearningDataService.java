package kr.co.ibk.service;

import kr.co.ibk.domain.enums.InOutGbnType;
import kr.co.ibk.domain.web.MemberInfo;
import kr.co.ibk.model.LearningDataForm;
import kr.co.ibk.model.TemplateForm;
import kr.co.ibk.repository.LearningDataInputRepository;
import kr.co.ibk.repository.LearningDataRepository;
import kr.co.ibk.repository.TemplateInputRepository;
import kr.co.ibk.repository.TemplateRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.util.HashMap;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class LearningDataService extends _BaseService {

    private final LearningDataRepository learningDataRepository;
    private final LearningDataInputRepository learningDataInputRepository;
    private final TemplateRepository templateRepository;
    private final TemplateInputRepository templateInputRepository;

    public HashMap<String, Object> save(LearningDataForm form, MemberInfo memberInfo) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("status", "FAIL");

        Long saveCnt = 0L;

        form.setMemId(memberInfo.getMemId());

        if (ObjectUtils.isEmpty(form.getId())) {
            //insert
            TemplateForm tempForm = new TemplateForm();
            if (!ObjectUtils.isEmpty(form.getTemplateAt()) && "Y".equals(form.getTemplateAt())) {
                //template save
                tempForm.setTemplateName(form.getTemplateName());
                tempForm.setSelectCon(form.getSearchTypeJson());
                tempForm.setHdqrBobDcd(form.getHdqrBobDcd());
                tempForm.setMemId(memberInfo.getMemId());
                templateRepository.insert(tempForm);

                saveCnt += templateInputRepository.insertList(tempForm.getId(), form.getInputArr(), InOutGbnType.INPUT.name());
                saveCnt += templateInputRepository.insertList(tempForm.getId(), form.getOutputArr(), InOutGbnType.OUTPUT.name());
                form.setTemplateId(tempForm.getId());
            }

            learningDataRepository.insert(form);
        } else {
            //update
            learningDataRepository.update(form);

            learningDataInputRepository.delete(form.getId());
        }

        saveCnt += learningDataInputRepository.insertList(form.getId(), form.getInputArr(), InOutGbnType.INPUT.name());
        saveCnt += learningDataInputRepository.insertList(form.getId(), form.getOutputArr(), InOutGbnType.OUTPUT.name());

        if (saveCnt > 0) {
            map.put("status", "SUCCESS");
        }
        return map;
    }
}
