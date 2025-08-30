package com.kenchiku_estimator.exception;

import com.kenchiku_estimator.form.EstimateForm;

public class UpdateEstimateFailedException extends RuntimeException {
	private int estimateId;
	private EstimateForm form;
	
	public UpdateEstimateFailedException(int estimateId, EstimateForm form, String message, Throwable cause) {
		super(message, cause);
		this.estimateId = estimateId;
		this.form = form;
	}

	public int getEstimateId() {
		return estimateId;
	}
	public EstimateForm getForm() {
		return form;
	}

}
