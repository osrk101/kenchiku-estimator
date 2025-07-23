package com.kenchiku_estimator.service;

import java.util.List;
import com.kenchiku_estimator.model.MEstimateItem;

public interface EstimateItemService {

    // 見積書アイテムを新規作成
    void createEstimateItem(MEstimateItem estimateItem);

    // 該当の見積書アイテムを取得する
    List<MEstimateItem> findByEstimateId(int id);

    // 見積書アイテムの削除をする
    void deleteEstimateItem(int id);
}
