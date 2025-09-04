package com.kenchiku_estimator.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import com.kenchiku_estimator.exception.DataBaseAccessException;
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
    try {
      return accountMapper.findAll();
    } catch (DataAccessException e) {
      log.error("全アカウントの全情報の取得に失敗しました。エラー = {}", e.getMessage());
      throw new DataBaseAccessException("全アカウントの全情報の取得に失敗しました", e);
    }
  }

  // 全アカウントの氏名を取得
  @Override
  public List<MAccount> getAllAccountsFullname() {
    log.info("Service 全アカウントの氏名を取得します");
    try {
      return accountMapper.findAllFullname();
    } catch (DataAccessException e) {
      log.error("全アカウントの氏名の取得に失敗しました。エラー = {}", e.getMessage());
      throw new DataBaseAccessException("全アカウントの氏名の取得に失敗しました", e);
    }
  }

  // IDに該当するアカウントを1件取得
  @Override
  public MAccount getAccountOne(int id) {
    log.info("Service IDに該当するアカウントを1件取得します");
    if (id <= 0) {
      return null;
    }
    try {
      return accountMapper.findById(id);
    } catch (DataAccessException e) {
      log.error("IDに該当するアカウントの取得に失敗しました。エラー = {}", e.getMessage());
      throw new DataBaseAccessException("IDに該当するアカウントの取得に失敗しました", e);
    }
  }
}
