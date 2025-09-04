package com.kenchiku_estimator.service;

import java.util.List;
import com.kenchiku_estimator.model.MAccount;

public interface AccountService {

  // 全アカウントの全情報を取得
  public List<MAccount> getAllAccounts();

  // 全アカウントの氏名を取得
  public List<MAccount> getAllAccountsFullname();

  // IDに該当するアカウントを1件取得
  MAccount getAccountOne(int id);

  // 新規アカウントの登録
  public void createNewAccount(MAccount account);

  // アカウントの更新
  public boolean updateAccount(MAccount account);

  // アカウントの削除
  public boolean deleteAccount(int id);

}
