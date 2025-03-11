package kr.co.ibk.repository;

import kr.co.ibk.domain.web.BillOutputInfo;
import kr.co.ibk.model.BillInputForm;
import org.springframework.stereotype.Repository;

@Repository
public interface BillOutputRepository {
    BillOutputInfo getDetail(BillInputForm form);

    int updateHitYn();
}
