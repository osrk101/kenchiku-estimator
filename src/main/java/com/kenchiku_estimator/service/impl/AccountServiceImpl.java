package com.kenchiku_estimator.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kenchiku_estimator.model.MAccount;
import com.kenchiku_estimator.repository.AccountMapper;
import com.kenchiku_estimator.service.AccountService;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class AccountServiceImpl implements AccountService {
    @Autowired
    private AccountMapper accountMapper;

    // 全アカウントの全情報を取得
    @Override
    public List<MAccount> getAllAccounts() {
        log.info("Service 全アカウントの全情報を取得します");
        return accountMapper.findAll();
    }

    // 全アカウントの氏名を取得
    @Override
    public List<MAccount> getAllAccountsFullName() {
        log.info("Service 全アカウントの氏名を取得します");
        return accountMapper.findAllFullName();
    }
}
