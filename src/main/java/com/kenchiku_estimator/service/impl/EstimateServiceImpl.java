package com.kenchiku_estimator.service.impl;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.kenchiku_estimator.form.EstimateItemForm;
import com.kenchiku_estimator.model.MEstimate;
import com.kenchiku_estimator.model.MEstimateItem;
import com.kenchiku_estimator.repository.EstimateMapper;
import com.kenchiku_estimator.service.EstimateService;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@Transactional
public class EstimateServiceImpl implements EstimateService {
  @Autowired
  EstimateMapper estimateMapper;

  @Autowired
  EstimateItemServiceImpl estimateItemService;

  @Autowired
  ModelMapper modelMapper;

  // 全見積書を取得
  @Override
  public List<MEstimate> getAllEstimates() {
    log.info("Service 全見積書を取得します");
    try {
      return estimateMapper.findAll();
    } catch (DataAccessException e) {
      log.error("全見積書の取得に失敗しました。データベースエラー = {}", e.getMessage());
      throw e;
    }
  }

  // 検索ワードに該当する見積書を取得
  @Override
  public List<MEstimate> getSearchEstimates(String searchWords) {
    log.info("Service 検索ワードに該当する見積書を取得します: {}", searchWords);
    try {
      return estimateMapper.findBySearchWords(searchWords);
    } catch (DataAccessException e) {
      log.error("検索ワードによる見積書の取得に失敗しました。データベースエラー = {}", e.getMessage());
      throw e;
    }
  }

  // 該当するIDの見積書を1件取得
  @Override
  public MEstimate getEstimateOne(int estimateId) {
    log.info("Service 該当するIDの見積書を取得します");
    if (estimateId <= 0) {
      return null;
    }
    try {
      log.info("Service 見積書の取得を開始: ID = {}", estimateId);
      MEstimate estimate = estimateMapper.findById(estimateId);
      if (estimate == null) {
        return null;
      }
      return estimate;
    } catch (DataAccessException e) {
      log.error("見積書の取得に失敗しました。ID = {} データベースエラー:  = {}", estimateId, e.getMessage(), e);
      throw e;
    }

  }

  // 見積書番号を生成
  @Override
  public String generateEstimateNumber() {
    log.info("Service 新規見積書番号を生成します");
    try {
      String today = LocalDate.now().format(DateTimeFormatter.ofPattern("yyMMdd"));
      int count = estimateMapper.countByEstimateNumberPrefix(today);
      int sequence = count + 1;
      log.info("Service 見積書番号の生成に成功しました)" + today + "-" + String.format("%02d", sequence));
      return today + "-" + String.format("%02d", sequence);
    } catch (DataAccessException e) {
      log.error("見積書番号の生成に失敗しました: データベースエラー = {}", e.getMessage());
      throw e;
    }
  }

  // 新規見積書の登録
  @Override
  public void createNewEstimate(MEstimate estimate) {
    log.info("Service 新規見積書の登録処理を実行します: {}", estimate);
    try {
      estimateMapper.createNewEstimate(estimate);
    } catch (DataAccessException e) {
      log.error("新規見積書の登録に失敗しました: データベースエラー = {}", e.getMessage());
      throw e;
    }
    log.info("Service 新規見積書の登録に成功しました: {}", estimate);
  }



  // 見積書の更新
  @Override
  public boolean updateEstimate(MEstimate estimate) {
    log.info("Service 見積書の更新処理を実行します: {}", estimate);
    try {
      estimateMapper.updateEstimate(estimate);
    } catch (DataAccessException e) {
      log.error("見積書の更新に失敗しました: ID= {}データベースエラー = {}", estimate.getId(), e.getMessage());
      throw e;
    }
    log.info("Service 見積書の更新に成功しました: {}", estimate);
    return true;
  }

  // 見積書と見積書アイテムを一括保存
  @Transactional
  public void saveEstimateWithItems(MEstimate estimate, List<EstimateItemForm> itemForms,
      boolean isNew) {
    if (isNew) {
      String estimateNumber = generateEstimateNumber();
      estimate.setEstimateNumber(estimateNumber);
      estimate.setId(0);
      createNewEstimate(estimate);
    } else {
      updateEstimate(estimate);
    }
    for (EstimateItemForm itemForm : itemForms) {
      MEstimateItem item = modelMapper.map(itemForm, MEstimateItem.class);
      item.setEstimateId(estimate.getId());
      if (isNew || item.getId() == 0) {
        estimateItemService.createEstimateItem(item);
      } else {
        estimateItemService.updateEstimateItem(item);
      }
    }
  }

  // 見積書の削除
  @Override
  public void deleteEstimate(int id) {
    log.info("Service 見積書を削除処理を実行します: ID = {}", id);
    try {
      estimateMapper.deleteEstimate(id);
      log.info("Service 見積書の削除に成功しました: ID = {}", id);
    } catch (DataAccessException e) {
      log.error("見積書の削除に失敗しました: ID = {}, データベースエラー = {}", id, e.getMessage());
      throw e;
    }
  }

}
