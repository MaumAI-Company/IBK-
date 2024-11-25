package kr.co.ibk.service;

import kr.co.ibk.domain.web.TemplateInfo;
import kr.co.ibk.model.TemplateForm;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TemplateService extends _BaseService {

    public List<TemplateInfo> page(TemplateForm params) {
        return null;
    }
}
