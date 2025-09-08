package com.kenchiku_estimator.service;

import java.util.List;
import com.kenchiku_estimator.form.EstimateItemForm;
import com.kenchiku_estimator.model.MEstimate;

public interface EstimateService {

  // 全ての見積書を取得
  public List<MEstimate> getAllEstimates();

  // 検索ワードに該当する見積書を取得
  public List<MEstimate> getSearchEstimates(String searchWords);

  // 該当するIDの見積書を1件取得
  public MEstimate getEstimateOne(int id);

  // 見積書番号を生成
  public String generateEstimateNumber();

  // 新規見積書の登録
  public void createNewEstimate(MEstimate estimate);

  // 見積書の更新
  public boolean updateEstimate(MEstimate estimate);


  // 見積書と見積書アイテムを一括保存
  public void saveEstimateWithItems(MEstimate estimate, List<EstimateItemForm> items, boolean b);

  // 見積書の削除
  public boolean deleteEstimate(int id);



}
