package com.wing.forutona.SpringSecurity;

import com.wing.forutona.App.ForutonaUser.Domain.FUserInfo;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;


public class UserAdapter extends User {

    FUserInfo fUserInfo;

    public UserAdapter(String username,Collection<? extends GrantedAuthority> authorities) {
        super(username, "forutona", authorities);
    }

    public FUserInfo getfUserInfo() {
        return fUserInfo;
    }

    public void setfUserInfo(FUserInfo fUserInfo) {
        this.fUserInfo = fUserInfo;
    }
}
