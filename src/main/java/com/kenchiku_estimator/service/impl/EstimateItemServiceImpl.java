package com.kenchiku_estimator.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    // 該当する見積書IDのアイテムを取得する
    @Override
    public List<MEstimateItem> findByEstimateId(int id) {
        log.info("Service 見積書アイテムを取得します ID = {}", id);
        try {
            return estimateItemMapper.findByEstimateId(id);
        } catch (Exception e) {
            log.error("見積書アイテムの取得に失敗しました: ID = {}, エラー = {}", id, e.getMessage());
            throw e;
        }
    }

    // 見積書アイテムを新規作成
    @Override
    public void createEstimateItem(MEstimateItem item) {
        log.info("Service 見積書アイテムを新規作成処理を実行します");
        try {
            estimateItemMapper.createEstimateItem(item);
            log.info("見積書アイテムの新規作成に成功しました");
        } catch (Exception e) {
            log.error("見積書アイテムの新規作成に失敗しました: {}", e.getMessage());
            throw e;
        }
    }

    // 見積書アイテムの更新をする
    @Override
    public void updateEstimateItem(MEstimateItem item) {
        log.info("Service 見積書アイテムの更新処理を実行します: {}", item);
        try {
            estimateItemMapper.updateEstimateItem(item);
            log.info("見積書アイテムの更新に成功しました: {}", item);
        } catch (Exception e) {
            log.error("見積書アイテムの更新に失敗しました: {}", e.getMessage());
            throw e;
        }
    }

    // 見積書アイテムの削除をする
    @Override
    public void deleteEstimateItem(int id) {
        log.info("Service 見積書アイテムを削除処理を実行します: ID = {}", id);
        try {
            estimateItemMapper.deleteEstimateItem(id);
            log.info("見積書アイテムの削除に成功しました: ID = {}", id);
        } catch (Exception e) {
            log.error("見積書アイテムの削除に失敗しました: ID = {}", id, e.getMessage());
            throw e;
        }
    }
}
