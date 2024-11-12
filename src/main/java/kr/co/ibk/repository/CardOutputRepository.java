package kr.co.ibk.repository;

import kr.co.ibk.domain.web.CardOutputInfo;
import kr.co.ibk.model.CardInputForm;
import org.springframework.stereotype.Repository;

@Repository
public interface CardOutputRepository {
    CardOutputInfo getDetail(CardInputForm form);
}
