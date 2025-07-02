package com.kenchiku_estimator.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kenchiku_estimator.model.MEstimate;
import com.kenchiku_estimator.repository.EstimateMapper;

@Service
public class EstimateService {

    @Autowired
    private EstimateMapper estimateMapper;

    public List<MEstimate> getAllEstimates() {
        return estimateMapper.findAll();
    }
}