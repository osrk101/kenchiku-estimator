package com.kenchiku_estimator.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import lombok.Data;

@Data
public class Estimate {

  private int id;

  private String estimateNumber;

  private String title;

  private String clientName;

  private int createdBy;

  private LocalDate createdAt;
  
  private LocalDate updateAt;

  private String fullName;

  public List<EstimateItem> items;
  
  public BigDecimal subtotal;
  
  public BigDecimal tax;
  
  public BigDecimal total;
}
