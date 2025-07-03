package com.kenchiku_estimator.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kenchiku_estimator.model.MAccount;
import com.kenchiku_estimator.repository.AccountMapper;

@Service
public class AccountService {

    @Autowired
    private AccountMapper accountMapper;

    public List<MAccount> getAllAccounts() {
        return accountMapper.findAll();
    }
}