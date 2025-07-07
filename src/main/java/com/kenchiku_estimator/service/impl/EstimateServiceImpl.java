package com.kenchiku_estimator.service.impl;

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

}
