package kr.co.ibk.repository;

import kr.co.ibk.domain.web.TemplateInfo;
import kr.co.ibk.model.TemplateForm;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TemplateRepository {
    void insert(TemplateForm form);

    int getTotalCount(TemplateForm params);

    List<TemplateInfo> getList(TemplateForm form);
}
