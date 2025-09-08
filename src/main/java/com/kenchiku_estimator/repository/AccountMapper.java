package com.kenchiku_estimator.repository;

import java.util.List;
import com.kenchiku_estimator.model.MAccount;

public interface AccountMapper {

  // 全件取得
  List<MAccount> findAll();

  // 氏名を全件取得
  List<MAccount> findAllFullname();

  // usernameでアカウントを取得
  MAccount findByUsername(String username);

  // IDでアカウントを1件取得
  MAccount findById(int id);

  // 新規アカウントの登録
  void insert(MAccount account);

  // アカウントの更新

  int update(MAccount account);

  // アカウントの削除

  int delete(int id);


}
