package kr.co.ibk.config.security.direct;

import kr.co.ibk.common.utils.NullHelper;
import kr.co.ibk.domain.enums.UserRoleType;
import kr.co.ibk.domain.web.Account;
import kr.co.ibk.model.AccountForm;
import kr.co.ibk.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
@RequiredArgsConstructor
public class UserDetailsService implements org.springframework.security.core.userdetails.UserDetailsService {

    private final AccountRepository accountRepository;

    private Logger log = LoggerFactory.getLogger(getClass());


    @SneakyThrows
    @Override
    public UserDetails loadUserByUsername(String loginId) {

        log.info("**스프링시큐리터 회원정보조회 loadUserByUsername loginId:{}", loginId);

        AccountForm accountForm = new AccountForm();
        accountForm.setLoginId(loginId);
        Account load = accountRepository.getLoad(accountForm);

        List<String> authorities = new ArrayList<>();

        if (NullHelper.isNull(load)) {
            throw new InternalAuthenticationServiceException("존재하지 않는 계정입니다. 관리자에게 문의하세요.");
        }

        authorities.add(UserRoleType.ADMIN.name());

        Account account = Account.builder()
                .mngrPid(load.getMngrPid())
                .mngrNm(load.getMngrNm())
                .loginId(load.getLoginId())
                .pwd(load.getPwd())
                .useAt(load.getUseAt())
                .delAt(load.getDelAt())
                .build();

        return new UserDetails(account, authorities);
    }

}
