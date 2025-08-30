package com.kenchiku_estimator.service.impl;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import com.kenchiku_estimator.exception.EstimateNotFoundException;
import com.kenchiku_estimator.model.MEstimate;
import com.kenchiku_estimator.repository.EstimateMapper;
import com.kenchiku_estimator.service.EstimateService;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class EstimateServiceImpl implements EstimateService {
    @Autowired
    EstimateMapper estimateMapper;

    // 全見積書を取得
    @Override
    public List<MEstimate> getAllEstimates() {
        log.info("Service 全見積書を取得します");
        try {
            return estimateMapper.findAll();
        } catch (DataAccessException e) {
            log.error("全見積書の取得に失敗しました: データベースエラー = {}", e.getMessage());
            throw new RuntimeException("全見積書の取得に失敗しました", e);
        } catch (Exception e) {
            log.error("予期せぬエラーが発生しました", e.getMessage());
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
            log.error("検索ワードによる見積書の取得に失敗しました: データベースエラー = {}", e.getMessage());
            throw new RuntimeException("検索ワードによる見積書の取得に失敗しました", e);
        } catch (Exception e) {
            log.error("検索ワードによる見積書の取得に失敗しました: {}エラー = {}", e.getMessage());
            throw e;
        }
    }

    // 該当するIDの見積書を1件取得
    @Override
    public MEstimate getEstimateOne(int estimateId) {
        log.info("Service 該当するIDの見積書を取得します");
        if (estimateId <= 0) {
            throw new IllegalArgumentException("不正なIDが指定されました");
        }
        try {
            log.info("Service 見積書の取得を開始: ID = {}", estimateId);
            MEstimate estimate = estimateMapper.findById(estimateId);
            if (estimate == null) {
                throw new EstimateNotFoundException("見積書が見つかりません: ID = " + estimateId);
            }
            return estimate;
        } catch (DataAccessException e) {
            log.error("データベースアクセスエラー: ID = {}, エラー = {}", estimateId, e.getMessage(), e);
            throw new DataBaseAccessException("データベースアクセスエラーが発生しました", e);
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
            throw new RuntimeException("見積書番号の生成に失敗しました", e);
        } catch (Exception e) {
            log.error("見積書番号の生成に失敗しました: {}", e.getMessage());
            throw e;
        }
    }

    // 新規見積書の登録
    @Override
    public void createNewEstimate(MEstimate estimate) {
        log.info("Service 新規見積書の登録処理を実行します: {}", estimate);
        try {
            estimateMapper.createNewEstimate(estimate);
            log.info("Service 新規見積書の登録に成功しました: {}", estimate);
        } catch (DataAccessException e) {
            log.error("新規見積書の登録に失敗しました: データベースエラー = {}", e.getMessage());
            throw new RuntimeException("新規見積書の登録に失敗しました", e);
        } catch (Exception e) {
            log.error("新規見積書の登録に失敗しました: {}", e.getMessage());
            throw e;
        }
    }

    // 見積書の更新
    @Override
    public void updateEstimate(MEstimate estimate) {
        log.info("Service 見積書の更新処理を実行します: {}", estimate);
        try {
            estimateMapper.updateEstimate(estimate);
            log.info("Service 見積書の更新に成功しました: {}", estimate);
        } catch (DataAccessException e) {
            log.error("見積書の更新に失敗しました: データベースエラー = {}", e.getMessage());
            throw new RuntimeException("見積書の更新に失敗しました", e);
        } catch (Exception e) {
            log.error("見積書の更新に失敗しました: {}", e.getMessage());
            throw e;
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
            throw new RuntimeException("見積書の削除に失敗しました", e);
        } catch (Exception e) {
            log.error("見積書の削除に失敗しました: ID = {}, エラー = {}", id, e.getMessage());
            throw e;
        }
    }

}
