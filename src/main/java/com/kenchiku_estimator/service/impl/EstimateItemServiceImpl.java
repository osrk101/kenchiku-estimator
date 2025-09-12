package com.kenchiku_estimator.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.kenchiku_estimator.model.EstimateItem;
import com.kenchiku_estimator.repository.EstimateItemMapper;
import com.kenchiku_estimator.service.EstimateItemService;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@Transactional
public class EstimateItemServiceImpl implements EstimateItemService {

  @Autowired
  private EstimateItemMapper estimateItemMapper;


  // 該当する見積書IDのアイテムを取得
  @Override
  public List<EstimateItem> findByEstimateId(int id) {
    log.info("Service 該当するIDの見積書アイテムを取得 ID = {}", id);

    if (id <= 0) {
      throw new IllegalArgumentException("不正なIDが指定されました");
    }

    log.info("Service 見積書アイテムの取得を開始: ID = {}", id);
    List<EstimateItem> itemList = estimateItemMapper.findByEstimateId(id);
    if (itemList == null) {
      return null;
    } else {
      log.info("見積書アイテムの取得に成功: ID = {}", id);
    }

    return itemList;
  }


  // 見積書アイテムの新規作成
  @Override
  public void createEstimateItem(EstimateItem item) {
    log.info("Service 見積書アイテムを新規作成処理を実行");

    estimateItemMapper.insertEstimateItem(item);
    log.info("見積書アイテムの新規作成処理に成功: {}", item);
  }


  // 見積書アイテムの更新
  @Override
  public boolean updateEstimateItem(EstimateItem item) {
    log.info("Service 見積書アイテムの更新処理を実行: {}", item);

    boolean isUpdate = estimateItemMapper.updateEstimateItem(item);
    if (isUpdate) {
      return true;
    }
    log.info("見積書アイテムの更新処理に成功: {}", item);
    return true;
  }


  // 見積書アイテムの削除
  @Override
  public boolean deleteEstimateItem(int id) {
    log.info("Service 見積書アイテムを削除処理を実行: ID = {}", id);

    boolean isDelete = estimateItemMapper.deleteEstimateItem(id);
    if (isDelete) {
      log.info("Service 見積書アイテムを削除処理に成功: ID = {}", id);
      return true;
    }
    log.warn("Service 見積書アイテムの削除に失敗: ID = {}", id);
    return false;
  }
}
