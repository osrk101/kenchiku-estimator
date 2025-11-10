package com.kenchiku_estimator.service;

import com.kenchiku_estimator.dto.EstimateTotalsDto;
import com.kenchiku_estimator.model.Estimate;

public interface PriceCalculatorService {
  EstimateTotalsDto calculateForEstimate(Estimate estimate);
}


