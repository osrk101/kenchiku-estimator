package com.kenchiku_estimator.form;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class AccountForm {

	private int id;

	@NotBlank
	private String userName;

	@NotBlank
	private String password;
}