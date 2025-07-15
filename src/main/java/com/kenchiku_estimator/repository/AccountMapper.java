package com.kenchiku_estimator.repository;

import java.util.List;

import com.kenchiku_estimator.model.MAccount;

public interface AccountMapper {

    // 全件取得
    List<MAccount> findAll();

    // 氏名を全件取得
    List<MAccount> findAllFullName();

    // usernameでアカウントを取得
    MAccount findByUsername(String username);

    // 登録
    void insert(MAccount account);

    // 更新
    void update(MAccount account);

    // 削除
    void delete(int id);

}
