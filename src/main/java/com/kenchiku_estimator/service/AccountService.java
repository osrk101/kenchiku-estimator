package com.kenchiku_estimator.service;

import java.util.List;
import com.kenchiku_estimator.model.Account;

public interface AccountService {

  // 全アカウントの全情報を取得
  public List<Account> getAllAccounts();

  // 全アカウントの氏名を取得
  public List<Account> getAllAccountsFullname();

  // IDに該当するアカウントを1件取得
  Account getAccountOne(int id);

  // 新規アカウントの登録
  public void createNewAccount(Account account);

  // アカウントの更新
  public boolean updateAccount(Account account);

  // アカウントの削除
  public boolean deleteAccount(int id);

  String encodePassword(String password);

}
