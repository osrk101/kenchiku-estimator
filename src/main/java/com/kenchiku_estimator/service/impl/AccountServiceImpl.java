package com.kenchiku_estimator.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.kenchiku_estimator.model.Account;
import com.kenchiku_estimator.repository.AccountMapper;
import com.kenchiku_estimator.repository.EstimateMapper;
import com.kenchiku_estimator.service.AccountService;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class AccountServiceImpl implements AccountService {

  @Autowired
  private AccountMapper accountMapper;

  @Autowired
  private EstimateMapper estimateMapper;

  @Autowired
  private PasswordEncoder passwordEncoder;


  // パスワードの暗号化
  @Override
  public String encodePassword(String password) {
    log.info("Service パスワードの暗号化を実行");
    return passwordEncoder.encode(password);
  }


  // 全アカウントの全情報を取得
  @Override
  public List<Account> getAllAccounts() {
    log.info("Service 全アカウントの全情報を取得");

    return accountMapper.findAll();
  }


  // 全アカウントの氏名を取得
  @Override
  public List<Account> getAllAccountsFullname() {
    log.info("Service 全アカウントの氏名を取得");

    return accountMapper.findAllFullname();
  }


  // IDに該当するアカウントを1件取得
  @Override
  public Account getAccountOne(int id) {
    log.info("Service 指定されたIDのアカウントを1件取得");

    if (id <= 0) {
      return null;
    }

    return accountMapper.findById(id);
  }



  // アカウントの新規作成
  @Override
  public void createNewAccount(Account account) {
    log.info("Service アカウントの新規作成を実行");

    if (account.getPassword() != null) {
      log.info("パスワード更新");
      account.setPassword(encodePassword(account.getPassword()));
    }
    accountMapper.insertAccount(account);
  }
  


  // アカウントの更新
  @Override
  public boolean updateAccount(Account account) {
    log.info("Service アカウントの更新処理を実行");

    if (account.getPassword() != null) {
      log.info("パスワード更新");
      account.setPassword(encodePassword(account.getPassword()));
    }

    boolean isUpdate = accountMapper.updateAccount(account);

    if (isUpdate) {
      return true;
    }
    return false;
  }


  // アカウントの削除
  @Override
  public boolean deleteAccount(int id) {
    log.info("Service アカウントの削除を実行");

    int estimateCount = estimateMapper.countByCreatedBy(id);
    if (estimateCount > 0) {
      return false;
    }

    boolean isDelete = accountMapper.deleteAccount(id);

    if (isDelete) {
      return true;
    }


    return false;
  }
}
