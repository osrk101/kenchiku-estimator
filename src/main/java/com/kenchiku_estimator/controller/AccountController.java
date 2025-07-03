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

}