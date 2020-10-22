package com.wing.forutona.SpringSecurity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.AbstractUserDetailsAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.server.resource.BearerTokenAuthenticationToken;
import org.springframework.security.oauth2.server.resource.web.BearerTokenAuthenticationFilter;
import org.springframework.stereotype.Component;

@Component
public class FireBaseProvider implements AuthenticationProvider {

    @Qualifier("userDetailService")
    @Autowired
    UserDetailsService userDetailsService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        BearerTokenAuthenticationToken authenticationRequest = (BearerTokenAuthenticationToken)authentication;
        UserDetails userDetails;
        try {
            FirebaseToken firebaseToken = FirebaseAuth.getInstance().verifyIdToken(authenticationRequest.getToken());

            userDetails = userDetailsService.loadUserByUsername(firebaseToken.getUid());

        } catch (FirebaseAuthException e) {
            throw new BadCredentialsException("Token Have Problem");
        }
        return new FireBaseAuthentication(userDetails,userDetails.getAuthorities());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return BearerTokenAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
