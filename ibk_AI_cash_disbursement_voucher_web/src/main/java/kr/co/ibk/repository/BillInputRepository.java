package kr.co.ibk.repository;

import kr.co.ibk.domain.web.BillInputInfo;
import kr.co.ibk.model.BillInputForm;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BillInputRepository {
    List<BillInputInfo> getList(BillInputForm params);

    int getTotalCount(BillInputForm params);

    BillInputInfo getDetail(BillInputForm params);

    List<BillInputInfo> getAllList(BillInputForm params);

    BillInputInfo getInputKey(BillInputForm params);
}
