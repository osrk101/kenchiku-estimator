package com.kenchiku_estimator.form;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import lombok.Data;



@Data
public class AccountForm {
	
	public interface Create{}
	public interface Update{}

	private int id;

	@NotBlank( message = "アカウント名を入力してください", groups = {Create.class, Update.class})
	@Size(max = 50, message = "アカウント名は50文字以内で入力してください", groups = {Create.class, Update.class})
	private String username;

	@NotBlank(groups = Create.class, message = "パスワードを入力してください")
	@Pattern(regexp = "^$|.{8,20}$", message = "パスワードは未入力または8〜20文字で入力してください", groups = {Create.class, Update.class})
	private String password;

	@NotBlank(message = "氏名を入力してください", groups = {Create.class, Update.class})
	@Size(max = 50, message = "氏名は50文字以内で入力してください", groups = {Create.class, Update.class})
	private String fullName;

	@NotBlank(message = "権限を選択してください", groups = {Create.class, Update.class})
	private String role;
}
