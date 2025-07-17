package com.kenchiku_estimator.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.kenchiku_estimator.model.CustomUserDetails;
import com.kenchiku_estimator.model.MAccount;
import com.kenchiku_estimator.repository.AccountMapper;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    private AccountMapper accountMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        MAccount account = accountMapper.findByUsername(username);
        if (account == null) {
            throw new UsernameNotFoundException("ユーザーが見つかりません: " + username);
        }
        return new CustomUserDetails(account);
    }
}
