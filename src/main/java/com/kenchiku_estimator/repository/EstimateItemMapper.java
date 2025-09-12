package com.kenchiku_estimator.repository;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import com.kenchiku_estimator.model.EstimateItem;

@Mapper
public interface EstimateItemMapper {

  // 見積書アイテムを新規作成
  void insertEstimateItem(EstimateItem item);

  // 該当の見積書アイテムを取得
  public List<EstimateItem> findByEstimateId(int id);

  // 見積書アイテムの削除
  boolean deleteEstimateItem(int id);

  // 見積書アイテムの更新
  boolean updateEstimateItem(EstimateItem item);
}
