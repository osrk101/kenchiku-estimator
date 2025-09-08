package com.kenchiku_estimator.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.security.crypto.password.PasswordEncoder;
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

  @Autowired
  private PasswordEncoder passwordEncoder;

  // パスワードの暗号化
  @Override
  public String encodePassword(String password) {
    log.info("Service パスワードの暗号化を行います");
    return passwordEncoder.encode(password);
  }


  // 全アカウントの全情報を取得
  @Override
  public List<MAccount> getAllAccounts() {
    log.info("Service 全アカウントの全情報を取得します");
    try {
      return accountMapper.findAll();
    } catch (DataAccessException e) {
      throw e;
    }
  }

  // 全アカウントの氏名を取得
  @Override
  public List<MAccount> getAllAccountsFullname() {
    log.info("Service 全アカウントの氏名を取得します");
    try {
      return accountMapper.findAllFullname();
    } catch (DataAccessException e) {
      throw e;
    }
  }

  // IDに該当するアカウントを1件取得
  @Override
  public MAccount getAccountOne(int id) {
    log.info("Service 指定されたIDのアカウントを1件取得します");
    if (id <= 0) {
      return null;
    }
    try {
      return accountMapper.findById(id);
    } catch (DataAccessException e) {
      throw e;
    }
  }

  // 新規アカウントの登録
  @Override
  public void createNewAccount(MAccount account) {
    log.info("Service 新規アカウントの登録を行います");
    if (account.getPassword() != null) {
      log.info("パスワード更新");
      account.setPassword(encodePassword(account.getPassword()));
    }
    try {
      accountMapper.insert(account);
    } catch (DataAccessException e) {
      throw e;
    }
  }

  // アカウントの更新
  @Override
  public boolean updateAccount(MAccount account) {
    log.info("Service アカウントの更新を行います");
    if (account.getPassword() != null) {
      log.info("パスワード更新");
      account.setPassword(encodePassword(account.getPassword()));
    }
    try {
      int rowNumber = accountMapper.update(account);
      if (rowNumber > 0) {
        return true;
      }
    } catch (DataAccessException e) {
      throw e;
    }
    return false;
  }

  // アカウントの削除
  @Override
  public boolean deleteAccount(int id) {
    log.info("Service アカウントの削除を行います");
    try {
      int rowNumber = accountMapper.delete(id);
      if (rowNumber > 0) {
        return true;
      }
    } catch (DataAccessException e) {
      throw e;
    }
    return false;
  }
}
