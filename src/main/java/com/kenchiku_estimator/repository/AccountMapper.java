package com.kenchiku_estimator.repository;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import com.kenchiku_estimator.model.Account;

@Mapper
public interface AccountMapper {

  // 全件取得
  List<Account> findAll();

  // 氏名を全件取得
  List<Account> findAllFullname();

  // usernameでアカウントを取得
  Account findByUsername(String username);

  // IDでアカウントを1件取得
  Account findById(int id);

  // アカウントの新規作成
  void insert(Account account);

  // アカウントの更新
  boolean update(Account account);

  // アカウントの削除
  boolean delete(int id);

  // 担当している見積書の数を取得
  int countEstimatesByAccountId(int id);


}
