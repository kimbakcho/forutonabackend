package com.wing.forutona.SpringSecurity;

import com.wing.forutona.App.ForutonaUser.Domain.FUserInfo;
import com.wing.forutona.App.ForutonaUser.Repository.FUserInfoDataRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class UserDetailService implements UserDetailsService {
    final FUserInfoDataRepository fUserInfoDataRepository;

    @Override
    public UserDetails loadUserByUsername(String userUid) throws UsernameNotFoundException {
        System.out.println("loadUserByUsername");
        System.out.println(userUid);
        FUserInfo fUserInfo = fUserInfoDataRepository.findById(userUid).get();
        List<GrantedAuthority> authorityList = new ArrayList<>();
        authorityList.add(new SimpleGrantedAuthority("ROLE_normal"));
        UserAdapter userAdapter = new UserAdapter(fUserInfo.getUid(),authorityList);
        userAdapter.setfUserInfo(fUserInfo);
        return userAdapter;
    }
}
