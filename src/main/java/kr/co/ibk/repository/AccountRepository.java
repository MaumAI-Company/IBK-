package kr.co.ibk.repository;

import kr.co.ibk.domain.web.Account;
import kr.co.ibk.model.AccountForm;
import kr.co.ibk.model.RequestList;
import kr.co.ibk.model.SearchForm;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface AccountRepository {
    List<Account> selectListForPaging(RequestList<?> requestList);

    List<Account> selectList(SearchForm form);
    Integer selectListCount(SearchForm form);
    Account getLoad(AccountForm form);
    void setInsert(AccountForm form);
    void setUpdateByPrimaryKeySelective(AccountForm form);
}
