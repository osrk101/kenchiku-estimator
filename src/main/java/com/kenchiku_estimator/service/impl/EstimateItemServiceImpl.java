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

    // 見積書アイテムを新規作成
    @Override
    public void createEstimateItem(MEstimateItem estimateItem) {
        log.info("Service 見積書アイテムを新規作成します");
        try {
            estimateItemMapper.createEstimateItem(estimateItem);
            log.info("見積書アイテムの新規作成に成功しました");
        } catch (Exception e) {
            log.error("見積書アイテムの新規作成に失敗しました: {}", e.getMessage());
            throw e;
        }
    }

    // 該当の見積書アイテムを取得する
    @Override
    public List<MEstimateItem> findByEstimateId(int id) {
        log.info("Service 見積書アイテムを取得します");
        return estimateItemMapper.findByEstimateId(id);
    }
}
