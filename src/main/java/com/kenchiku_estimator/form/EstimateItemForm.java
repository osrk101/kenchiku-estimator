package com.kenchiku_estimator.form;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class EstimateItemForm {

    private int estimateId;

    private String estimateitemname;

    private BigDecimal unit_price;

    private BigDecimal quantity;

    private String unit;

}
