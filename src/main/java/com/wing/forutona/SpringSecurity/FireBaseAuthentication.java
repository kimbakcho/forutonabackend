package com.wing.forutona.SpringSecurity;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

public class FireBaseAuthentication extends AbstractAuthenticationToken {

    final  UserDetails userDetails;

    public FireBaseAuthentication(UserDetails userDetails, Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        super.setAuthenticated(true);
        this.userDetails = userDetails;
    }

    @Override
    public Object getCredentials() {
        return "forutona";
    }

    @Override
    public Object getPrincipal() {
        return userDetails;
    }
}
