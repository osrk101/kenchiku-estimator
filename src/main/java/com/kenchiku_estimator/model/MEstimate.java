package com.kenchiku_estimator.model;

import java.time.LocalDate;

import lombok.Data;

@Data
public class MEstimate {

    private int id;

    private String estimateNumber;

    private String title;

    private int createdBy;

    private LocalDate createdAt;
}
