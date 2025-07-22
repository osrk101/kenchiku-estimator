package com.kenchiku_estimator.model;

import java.time.LocalDate;
import java.util.List;

import lombok.Data;

@Data
public class MEstimate {

    private int id;

    private String estimateNumber;

    private String title;

    private String clientName;

    private int createdBy;

    private LocalDate createdAt;

    private String fullname;

    private List<MEstimateItem> items;
}
