package ingsw.server.jwt.util;

import ingsw.server.entity.Utente;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;

import java.util.Collection;

public class AuthoritiesUtil {
    public static Collection<? extends GrantedAuthority> getAuthorities(Utente utente) {
        String[] userRoles = {"ROLE_ADMIN"};
        return AuthorityUtils.createAuthorityList(userRoles);
    }
}

