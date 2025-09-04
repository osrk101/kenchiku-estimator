package com.kenchiku_estimator.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.kenchiku_estimator.exception.DataBaseAccessException;
import com.kenchiku_estimator.model.MEstimateItem;
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
  public List<MEstimateItem> findByEstimateId(int id) {
    log.info("Service 該当するIDの見積書アイテムを取得します ID = {}", id);
    if (id <= 0) {
      throw new IllegalArgumentException("不正なIDが指定されました");
    }
    try {
      log.info("Service 見積書アイテムの取得を開始: ID = {}", id);
      List<MEstimateItem> itemList = estimateItemMapper.findByEstimateId(id);
      if (itemList == null) {
        return null;
      } else {
        log.info("見積書アイテムの取得に成功しました: ID = {}", id);
      }
      return itemList;
    } catch (DataAccessException e) {
      log.error("データベースエラー: ID = {}, エラー = {}", id, e.getMessage());
      throw new DataBaseAccessException("見積書アイテムの取得に失敗しました", e);
    }

  }

  // 新規見積書アイテムの登録
  @Override
  public void createEstimateItem(MEstimateItem item) {
    log.info("Service 見積書アイテムを新規作成処理を実行します");
    try {
      estimateItemMapper.createEstimateItem(item);
      log.info("見積書アイテムの新規作成に成功しました:{}", item);
    } catch (DataAccessException e) {
      log.error("見積書アイテムの新規作成に失敗しました: {}", e.getMessage());
      throw new DataBaseAccessException("見積書アイテムの新規作成に失敗しました", e);
    }
  }

  // 見積書アイテムの更新
  @Override
  public boolean updateEstimateItem(MEstimateItem item) {
    log.info("Service 見積書アイテムの更新処理を実行します: {}", item);
    try {
      int updatedRows = estimateItemMapper.updateEstimateItem(item);
      if (updatedRows == 0) {
        return false;
      }
      log.info("見積書アイテムの更新に成功しました: {}", item);
    } catch (DataAccessException e) {
      log.error("見積書アイテムの更新に失敗しました データベースエラー = {}", e.getMessage());
      throw new DataBaseAccessException("見積書アイテムの更新に失敗しました", e);
    }
    return true;
  }

  // 見積書アイテムの削除
  @Override
  public boolean deleteEstimateItem(int id) {
    log.info("Service 見積書アイテムを削除処理を実行します: ID = {}", id);
    try {
      int deleted = estimateItemMapper.deleteEstimateItem(id);
      if (deleted == 0) {
        return false;
      }
      log.info("見積書アイテムの削除に成功しました: ID = {}", id);
    } catch (DataAccessException e) {
      log.error("見積書アイテムの削除に失敗しました: ID = {} データベースエラー = {}", id, e.getMessage());
      throw new DataBaseAccessException("見積書アイテムの削除に失敗しました", e);
    }
    return true;
  }
}
