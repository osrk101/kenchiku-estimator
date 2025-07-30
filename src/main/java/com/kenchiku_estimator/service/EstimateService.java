package com.kenchiku_estimator.service;

import java.util.List;

import com.kenchiku_estimator.model.MEstimate;

public interface EstimateService {

    // すべての見積書を取得
    public List<MEstimate> getAllEstimates();

    // 該当するIDの見積書を1件取得
    public MEstimate getEstimateOne(int id);

    // 新規見積書の登録
    public void createNewEstimate(MEstimate estimate);

    // 見積書番号を生成
    public String generateEstimateNumber();

    // 見積書の削除
    public void deleteEstimate(int id);

    // 見積書の更新
    public void updateEstimate(MEstimate estimate);

    // 検索ワードに該当する見積書を取得
    public List<MEstimate> getSearchEstimates(String searchWords);
}