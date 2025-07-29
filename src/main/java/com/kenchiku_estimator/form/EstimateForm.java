package com.kenchiku_estimator.form;

import java.util.ArrayList;
import java.util.List;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class EstimateForm {

    private int id;

    private String estimateNumber;

    @NotBlank(message = "件名は必須です")
    @Size(max = 100, message = "件名は100文字以内で入力してください")
    private String title;

    @NotBlank(message = "顧客名は必須です")
    @Size(max = 100, message = "顧客名は100文字以内で入力してください")
    private String clientName;

    @NotNull(message = "担当者は必須です")
    private int createdBy;

    private List<EstimateItemForm> items = new ArrayList<>();

}
