package com.kenchiku_estimator.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import com.kenchiku_estimator.form.AccountForm;
import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
public class AccountController {

  @GetMapping("/index")
  public String getIndex(@ModelAttribute AccountForm accountForm) {
    log.info("ログイン画面を表示します。");
    return "index";
  }

  @GetMapping("/accounts")
  public String getAccounts(@ModelAttribute AccountForm accountForm) {
    log.info("アカウント一覧画面を表示します。");
    return "accounts";
  }

  @GetMapping("/accounts/detail")
  public String getAccountDetail(@ModelAttribute AccountForm accountForm) {
    log.info("アカウント詳細画面を表示します。");
    return "accounts/detail";
  }

  @GetMapping("/accounts/create")
  public String getAccountCreate(@ModelAttribute AccountForm accountForm) {
    log.info("アカウント新規登録画面を表示します。");
    return "accouns/create";
  }

  @GetMapping("/accounts/edit")
  public String getAccountEdit(@ModelAttribute AccountForm accountForm) {
    log.info("アカウント編集画面を表示します。");
    return "accounts/edit";
  }


  @GetMapping("/accounts/delete")
  public String getAccountDelete(@ModelAttribute AccountForm accountForm) {
    log.info("アカウント削除画面を表示します。");
    return "accounts/delete";
  }
}


