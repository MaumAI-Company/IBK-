package kr.co.ibk.repository;

import kr.co.ibk.domain.web.CardInputInfo;
import kr.co.ibk.model.CardInputForm;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CardInputRepository {
    List<CardInputInfo> getList(CardInputForm params);

    int getTotalCount(CardInputForm params);

    CardInputInfo getDetail(CardInputForm params);
}
