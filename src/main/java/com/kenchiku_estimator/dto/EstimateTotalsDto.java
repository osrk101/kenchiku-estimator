package com.kenchiku_estimator.dto;

import lombok.Data;

@Data
public class EstimateTotalsDto {

	private int subtotal; // 税抜小計（円未満切り捨て後）
	private int tax; // 税額（円未満切り捨て後）
	private int total; // 税込合計（最後に千円未満切り捨て適用済み）
}
