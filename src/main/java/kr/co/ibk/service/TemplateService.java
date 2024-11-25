package kr.co.ibk.service;

import kr.co.ibk.domain.web.TemplateInfo;
import kr.co.ibk.model.TemplateForm;
import kr.co.ibk.model.paging.PaginationInfo;
import kr.co.ibk.repository.TemplateRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TemplateService extends _BaseService {
    private final TemplateRepository templateRepository;

    public List<TemplateInfo> page(TemplateForm params) {
         /*
        default search condition
         */
        if (ObjectUtils.isEmpty(params.getSearchTarget())) {
            params.setSearchTarget("1");
        }

        int totalCount = templateRepository.getTotalCount(params);

        PaginationInfo paginationInfo = new PaginationInfo(params);
        paginationInfo.setTotalRecordCount(totalCount);
        params.setPaginationInfo(paginationInfo);

        List<TemplateInfo> list = new ArrayList<>();
        if (totalCount > 0) {
            params.setPagingAt("Y");
            list = getList(params);
        }

        return list;
    }

    /**
     * return all list
     *
     * @param form
     * @return
     */
    public List<TemplateInfo> getList(TemplateForm form) {
        return templateRepository.getList(form);
    }
}
