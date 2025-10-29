package com.kenchiku_estimator.repository;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.kenchiku_estimator.model.Estimate;

@Mapper
public interface EstimateMapper {

  // 全件取得(アカウント氏名を含む)
  List<Estimate> getAllEstimates();

  // 担当する見積書の一覧を取得
  List<Estimate> findByCreateBy(int createBy);

  // 検索ワードに該当する見積書を取得
  List<Estimate> findBySearchWords(String searchWords);

  // IDで取得(アカウント氏名を含む)
  Estimate findById(int id);

  // 見積番号の年月日で件数を取得
  int countByEstimateNumberPrefix(String prefix);

  // 見積書の新規作成
  void insertEstimate(Estimate estimate);

  // 見積書の更新
  boolean updateEstimate(Estimate estimate);

  // 見積書の削除
  boolean deleteEstimate(int id);

  // 担当している見積書の数を取得
  int countByCreatedBy(int accountId);



}
