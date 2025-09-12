package com.kenchiku_estimator.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import com.kenchiku_estimator.form.AccountForm;
import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
public class AuthController {

  // ログイン画面を表示する
  @GetMapping("/index")
  public String login(@ModelAttribute AccountForm accountForm) {
    log.info("ログイン画面を表示");
    return "index";
  }
}
