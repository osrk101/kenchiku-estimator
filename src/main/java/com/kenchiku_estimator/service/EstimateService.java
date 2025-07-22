package com.kenchiku_estimator.service;

import java.util.List;

import com.kenchiku_estimator.model.MEstimate;

public interface EstimateService {

    // すべての見積書を取得
    public List<MEstimate> getAllEstimates();

    // 該当するIDの見積書を1件取得
    public MEstimate getEstimateOne(int id);

    public void createNewEstimate(MEstimate estimate);

}