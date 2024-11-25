package kr.co.ibk.repository;

import kr.co.ibk.model.TemplateForm;
import org.springframework.stereotype.Repository;

@Repository
public interface TemplateRepository {
    void insert(TemplateForm form);
}
