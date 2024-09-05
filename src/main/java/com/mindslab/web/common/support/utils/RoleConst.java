package com.mindslab.web.common.support.utils;

import java.security.Principal;
import java.util.Iterator;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

public enum RoleConst {

    ADMIN("ROLE_ADMIN"),
    USER("ROLE_USER"),
    GUEST("ROLE_GUEST");

    private String role;

    RoleConst(String role) {
        this.role = role;
    }

    public String getRole() {
        return this.role;
    }

    public static RoleConst findByRole(String role) {
        for (RoleConst roleConst : RoleConst.values()) {
            if (roleConst.equals(role)) {
                return roleConst;
            }
        }
        throw new RuntimeException();
    }

    public static boolean compareToRole(Principal principal, String role) {
        Iterator<? extends GrantedAuthority> authorities = ((UsernamePasswordAuthenticationToken) principal)
                .getAuthorities().iterator();
        while (authorities.hasNext()) {
            GrantedAuthority authority = authorities.next();

            if (role.equals(authority.getAuthority())) {
                return true;
            }
        }
        return false;
    }

}
