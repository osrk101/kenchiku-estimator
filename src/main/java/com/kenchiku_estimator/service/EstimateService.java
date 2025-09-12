package com.kenchiku_estimator.service;

import java.util.List;
import com.kenchiku_estimator.form.EstimateItemForm;
import com.kenchiku_estimator.model.Estimate;

public interface EstimateService {

  // 全ての見積書を取得
  public List<Estimate> getAllEstimates();

  // 検索ワードに該当する見積書を取得
  public List<Estimate> getSearchEstimates(String searchWords);

  // 該当するIDの見積書を1件取得
  public Estimate getEstimateOne(int id);

  // 見積書番号を生成
  public String generateEstimateNumber();

  // 新規見積書の登録
  public void createNewEstimate(Estimate estimate);

  // 見積書の更新
  public boolean updateEstimate(Estimate estimate);


  // 見積書と見積書アイテムを一括保存
  public void saveEstimateWithItems(Estimate estimate, List<EstimateItemForm> items, boolean b);

  // 見積書の削除
  public boolean deleteEstimate(int id);



}
