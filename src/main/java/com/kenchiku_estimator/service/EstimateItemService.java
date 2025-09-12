package com.kenchiku_estimator.service;

import java.util.List;
import com.kenchiku_estimator.model.EstimateItem;

public interface EstimateItemService {

  // 見積書アイテムを新規作成
  void createEstimateItem(EstimateItem item);

  // 該当の見積書アイテムを取得する
  List<EstimateItem> findByEstimateId(int id);

  // 見積書アイテムの削除をする
  public boolean deleteEstimateItem(int id);

  // 見積書アイテムの更新をする
  public boolean updateEstimateItem(EstimateItem item);
}
