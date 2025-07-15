package com.kenchiku_estimator.form;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class EstimateForm {

    private String title;

    private String clientName;

    private List<EstimateItemForm> items = new ArrayList<>();

    private String accountId;

    private List<String> accountFullNameList = new ArrayList<>();

}
