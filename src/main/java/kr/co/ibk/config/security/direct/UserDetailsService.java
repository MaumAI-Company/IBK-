package kr.co.ibk.config.security.direct;

import kr.co.ibk.common.utils.NullHelper;
import kr.co.ibk.domain.web.MemberInfo;
import kr.co.ibk.service.AdminUserService;
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

    private final AdminUserService adminUserService;

    private Logger log = LoggerFactory.getLogger(getClass());


    @SneakyThrows
    @Override
    public UserDetails loadUserByUsername(String loginId) {

        log.info("**스프링시큐리터 회원정보조회 loadUserByUsername loginId:{}", loginId);

        MemberInfo userInfo = adminUserService.getUserInfo(loginId);

        List<String> authorities = new ArrayList<>();

        if (NullHelper.isNull(userInfo)) {
            throw new InternalAuthenticationServiceException("존재하지 않는 계정입니다. 관리자에게 문의하세요.");
        }
        authorities.add(userInfo.getRoleId());

        MemberInfo memberInfo = MemberInfo.builder()
                .memSeq(userInfo.getMemSeq())
                .memName(userInfo.getMemName())
                .memId(userInfo.getMemId())
                .memPwd(userInfo.getMemPwd())
                .roleId(userInfo.getRoleId())
                .deptName(userInfo.getDeptName())
                .build();

        return new UserDetails(memberInfo, authorities);
    }

}
