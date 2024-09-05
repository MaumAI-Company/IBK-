package com.mindslab.web.auth;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.mindslab.web.vo.MemberVO;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class UserPrincipal implements UserDetails{
    private MemberVO member;

    public UserPrincipal(MemberVO member) {
        this.member = member;
    }

	public MemberVO getMember(){
		return member;
	}

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
		List<GrantedAuthority> authorities = new ArrayList<>();

        // getRoleList로 사용자의 권한 목록을 가져온 후 권한 ID를 셋팅한다.
		this.member.getRoleList().forEach(roleVO -> {
			GrantedAuthority authority = new SimpleGrantedAuthority(
					"ROLE_" + roleVO.getRoleId());
			authorities.add(authority);
		});
		return authorities;
    }

    @Override
    public String getPassword() {
        return this.member.getMemPwd();
    }

    @Override
    public String getUsername() {
        return this.member.getMemId();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
