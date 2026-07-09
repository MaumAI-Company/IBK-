package kr.co.ibk.repository;

import kr.co.ibk.domain.web.DeployHistoryInfo;
import kr.co.ibk.model.DeployHistoryForm;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DeployHistoryRepository {
    int getTotalCount(DeployHistoryForm params);

    List<DeployHistoryInfo> getList(DeployHistoryForm params);
}
