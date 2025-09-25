package com.kenchiku_estimator.form;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class AccountForm {

  private int id;

  @NotBlank(message = "アカウント名を入力してください")
  @Size(max = 50, message = "アカウント名は50文字以内で入力してください")
  private String username;

  @NotBlank(message = "パスワードを入力してください")
  @Size(min = 8, message = "パスワードは8文字以上で入力してください")
  private String password;

  @NotBlank(message = "氏名を入力してください")
  @Size(max = 50, message = "氏名は50文字以内で入力してください")
  private String fullName;

  @NotBlank(message = "権限を選択してください")
  private String role;
}

