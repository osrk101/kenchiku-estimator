package com.kenchiku_estimator.form;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class EstimateItemForm {

    private int estimateId;

    private String itemName;

    private BigDecimal unitPrice;

    private BigDecimal quantity;

    private String unit;

}
