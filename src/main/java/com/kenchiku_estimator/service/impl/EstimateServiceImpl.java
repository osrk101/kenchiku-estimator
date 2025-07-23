package com.kenchiku_estimator.service.impl;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
        return estimateMapper.findAll();
    }

    // 該当するIDの見積書を1件取得
    @Override
    public MEstimate getEstimateOne(int EstimateId) {
        log.info("Service 該当するIDの見積書を1件取得します");
        return estimateMapper.findById(EstimateId);
    }

    // 新規見積書の登録
    @Override
    public void createNewEstimate(MEstimate estimate) {
        log.info("Service 新規見積書の登録クエリを実行します: {}", estimate);
        estimateMapper.createNewEstimate(estimate);
        log.info("Service 新規見積書の登録クエリを実行しました: {}", estimate);
    }

    // 見積書番号を生成
    public String generateEstimateNumber() {
        String today = LocalDate.now().format(DateTimeFormatter.ofPattern("yyMMdd"));
        int count = estimateMapper.countByEstimateNumberPrefix(today);
        int sequence = count + 1;
        return today + "-" + String.format("%02d", sequence);
    }

    // 見積書の削除
    public void deleteEstimate(int id) {
        log.info("Service 見積書を削除します: ID = {}", id);
        estimateMapper.deleteEstimate(id);
        log.info("見積書の削除に成功しました: ID = {}", id);
    }

}
