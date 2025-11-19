package com.kenchiku_estimator.form;

import java.math.BigDecimal;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import lombok.Data;

@Data
public class EstimateItemForm {

    private int id;

    private int estimateId;

    @NotBlank(message = "資材・作業名は必須です")
    @Size(max = 100, message = "資材・作業名は100文字以内で入力してください")
    private String itemName;

    @NotNull(message = "単価は必須です")
    @DecimalMin(value = "0.00", inclusive = true, message = "単価は0以上の値を入力してください")
    @Digits(integer = 4, fraction = 2, message = "単価は整数4桁、小数2桁以内で入力してください")
    public BigDecimal unitPrice;

    @NotNull(message = "数量は必須です")
    @DecimalMin(value = "0.00", inclusive = true, message = "数量は0以上の値を入力してください")
    @Digits(integer = 7, fraction = 2, message = "数量は整数7桁、小数2桁以内で入力してください")
    public BigDecimal quantity;

    @NotBlank(message = "単位は必須です")
    @Size(max = 20, message = "単位は20文字以内で入力してください")
    private String unit;
    
    public BigDecimal rowSubtotal;

}
