package com.kenchiku_estimator.model;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class EstimateItem {

    private int id;

    private int estimateId;

    private String itemName;

    private BigDecimal unitPrice;

    private BigDecimal quantity;

    private String unit;

	public void setRowSubtotal(BigDecimal lineTotal) {
		
	}
}
