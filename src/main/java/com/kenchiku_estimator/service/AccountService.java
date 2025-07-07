package com.kenchiku_estimator.service;

import java.util.List;
import com.kenchiku_estimator.model.MAccount;

public interface AccountService {

    // 全アカウントの全情報を取得
    public List<MAccount> getAllAccounts();

    // 全アカウントの氏名を取得
    public List<MAccount> getAllAccountsFullName();
}