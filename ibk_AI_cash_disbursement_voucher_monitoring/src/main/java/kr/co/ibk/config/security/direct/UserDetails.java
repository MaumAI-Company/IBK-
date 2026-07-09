package kr.co.ibk.config.security.direct;


import kr.co.ibk.domain.web.MemberInfo;
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
    private MemberInfo memberInfo;

    public UserDetails(MemberInfo memberInfo, List<String> authorities)
    {
        super
                (
                        memberInfo.getMemId(),
                        memberInfo.getMemPwd(),
                        getGranted(authorities)
                );
        this.memberInfo = memberInfo;
    }

    private static Collection<? extends GrantedAuthority> getGranted(List<String> authorities) {

        Collection<GrantedAuthority> grantedAuthorities = new ArrayList<>();
        for (String auth: authorities) {
            grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_" + auth));
        }
        return grantedAuthorities;
    }
}