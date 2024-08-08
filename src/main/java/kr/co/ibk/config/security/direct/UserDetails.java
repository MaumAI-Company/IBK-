package kr.co.ibk.config.security.direct;


import kr.co.ibk.domain.web.Account;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

//@Data
@EqualsAndHashCode(callSuper=false)
public class UserDetails extends User
{
    private static final long serialVersionUID = 1L;

    @Getter
    private Account account;

    public UserDetails(Account account, List<String> authorities)
    {
        super
                (
                        account.getLoginId(),
                        account.getPwd(),
                        getGranted(authorities)
                );
        this.account = account;
    }

    private static Collection<? extends GrantedAuthority> getGranted(List<String> authorities) {

        Collection<GrantedAuthority> grantedAuthorities = new ArrayList<>();
        for (String auth: authorities) {
            grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_" + auth));
        }
        return grantedAuthorities;
    }
}