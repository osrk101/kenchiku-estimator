package com.kenchiku_estimator.repository;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import com.kenchiku_estimator.model.MEstimate;

@Mapper
public interface EstimateMapper {

  // 全件取得(アカウント氏名を含む)
  List<MEstimate> findAll();

  // 検索ワードに該当する見積書を取得
  List<MEstimate> findBySearchWords(String searchWords);


  // IDで取得(アカウント氏名を含む)
  MEstimate findById(int id);

  // 見積番号の年月日で件数を取得
  int countByEstimateNumberPrefix(String prefix);


  // 新規見積書の登録
  void insert(MEstimate estimate);

  // 見積書の更新
  int update(MEstimate estimate);

  // 見積書の削除
  int delete(int id);

  // 担当している見積書の数を取得
  int countByCreatedBy(int accountId);


}
