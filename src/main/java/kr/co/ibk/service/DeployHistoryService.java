package kr.co.ibk.service;

import kr.co.ibk.domain.web.DeployHistoryInfo;
import kr.co.ibk.model.DeployHistoryForm;
import kr.co.ibk.model.paging.PaginationInfo;
import kr.co.ibk.repository.DeployHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DeployHistoryService extends _BaseService {
    private final DeployHistoryRepository deployHistoryRepository;

    public List<DeployHistoryInfo> getPage(DeployHistoryForm params) {
        int totalCount = deployHistoryRepository.getTotalCount(params);

        PaginationInfo paginationInfo = new PaginationInfo(params);
        paginationInfo.setTotalRecordCount(totalCount);
        params.setPaginationInfo(paginationInfo);

        List<DeployHistoryInfo> list = new ArrayList<>();
        if (totalCount > 0) {
            if (ObjectUtils.isEmpty(params.getSorting())) {
                params.setSorting("desc");
            }
            params.setPagingAt("Y");
            list = getList(params);
        }

        return list;
    }

    public List<DeployHistoryInfo> getList(DeployHistoryForm params) {
        return deployHistoryRepository.getList(params);
    }
}
