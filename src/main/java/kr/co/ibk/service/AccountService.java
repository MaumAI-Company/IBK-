package kr.co.ibk.service;

import kr.co.ibk.common.Constants;
import kr.co.ibk.common.exceptions.CustomApiException;
import kr.co.ibk.common.utils.NullHelper;
import kr.co.ibk.domain.web.Account;
import kr.co.ibk.model.AccountForm;
import kr.co.ibk.model.RequestList;
import kr.co.ibk.model.SearchForm;
import kr.co.ibk.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AccountService extends _BaseService {
    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;

    public Page<Account> getListWithPage(SearchForm form,
                                         Pageable pageable) {

        int page = (pageable.getPageNumber() == 0) ? 0 : (pageable.getPageNumber() -1);
        pageable = PageRequest.of(page, form.getPageSize() == null ? Constants.DEFAULT_PAGESIZE : form.getPageSize());

        RequestList<?> requestList = RequestList.builder()
                .data(form)
                .pageable(pageable)
                .build();

        List<Account> list = accountRepository.selectListForPaging(requestList);
        int totCnt = accountRepository.selectListCount(form);
        return new PageImpl<>(list, pageable, totCnt);
    }

    public List<Account> getList(SearchForm form) {
        return accountRepository.selectList(form);
    }

    public Integer getTotalCount(SearchForm form) {
        return accountRepository.selectListCount(form);
    }

    /**
     * 관리자계정 > 상세 조회
     *
     * @param form
     * @return
     */
    public Account getLoad(AccountForm form) {
        try {
            return accountRepository.getLoad(form);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 관리자계정 > 등록
     *
     * @param form
     * @param account
     * @return
     */
    @Transactional
    public Boolean register(AccountForm form, Account account) {
        try {
            form.setRegDtm(LocalDateTime.now());
            form.setRegPsId(account.getLoginId());
            form.setUpdDtm(LocalDateTime.now());
            form.setUpdPsId(account.getLoginId());
            form.setDelAt("N");
            form.setPwd(passwordEncoder.encode(form.getPwd()));

            accountRepository.setInsert(form);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            throw new CustomApiException("저장을 실패했습니다.");
        }
    }

    /**
     * 관리자계정 > 수정
     *
     * @param form
     * @param account
     * @return
     */
    @Transactional
    public Boolean modify(AccountForm form, Account account) {
        try {
            if (!NullHelper.isEmpty(form.getPwd())) {
                form.setPwd(passwordEncoder.encode(form.getPwd()));
            }

            form.setUpdDtm(LocalDateTime.now());
            form.setUpdPsId(account.getLoginId());
            accountRepository.setUpdateByPrimaryKeySelective(form);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            throw new CustomApiException("저장을 실패했습니다.");
        }
    }
}
