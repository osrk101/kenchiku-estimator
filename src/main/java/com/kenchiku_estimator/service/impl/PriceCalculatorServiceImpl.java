package com.kenchiku_estimator.service.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;

import org.springframework.stereotype.Service;

import com.kenchiku_estimator.dto.EstimateTotalsDto;
import com.kenchiku_estimator.model.Estimate;
import com.kenchiku_estimator.model.EstimateItem;
import com.kenchiku_estimator.service.PriceCalculatorService;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class PriceCalculatorServiceImpl implements PriceCalculatorService {

  private static final BigDecimal TAX_RATE = new BigDecimal("0.10"); // 税率10％
  private static final RoundingMode RM = RoundingMode.DOWN;          // すべて切り捨て

  @Override
  public EstimateTotalsDto calculateForEstimate(Estimate estimate) {
    // 行金額=単価×数量 を合計（円未満切り捨て）
    BigDecimal sum = estimate.getItems().stream()
        .map(this::lineAmount)                        // 行金額（円未満切り捨て済み）
        .reduce(BigDecimal.ZERO, BigDecimal::add);

    BigDecimal subtotal = sum.setScale(0, RM);        // 念のため整数化
    BigDecimal tax      = subtotal.multiply(TAX_RATE).setScale(0, RM);
    BigDecimal total    = subtotal.add(tax);          // 税込合計（円単位）


    EstimateTotalsDto dto = new EstimateTotalsDto();
    dto.setSubtotal(subtotal.intValue());
    dto.setTax(tax.intValue());
    dto.setTotal(total.intValue());
    return dto;
  }

  private BigDecimal lineAmount(EstimateItem item) {
    if (item.getUnitPrice() == null || item.getQuantity() == null) {
      return BigDecimal.ZERO;
    }
    // 単価×数量→円未満切り捨て
    return item.getUnitPrice()
        .multiply((item.getQuantity()))
        .setScale(0, RM);
  }
}
