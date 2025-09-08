package com.kenchiku_estimator.controller;

import java.util.List;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import com.kenchiku_estimator.form.AccountForm;
import com.kenchiku_estimator.model.MAccount;
import com.kenchiku_estimator.service.AccountService;
import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
@RequestMapping("accounts")

public class AccountController {

  @Autowired
  private AccountService accountService;

  @Autowired
  ModelMapper modelMapper;

  // アカウント一覧画面を表示する
  @GetMapping
  public String getAccounts(MAccount account, Model model) {
    log.info("アカウント一覧画面を表示します。");
    List<MAccount> accountsList = accountService.getAllAccounts();
    log.info("アカウント一覧の件数: {}", accountsList.size());
    model.addAttribute("accountsList", accountsList);
    return "accounts";
  }

  // アカウント新規登録画面を表示する
  @GetMapping("/create")
  public String getAccountCreate(@ModelAttribute AccountForm accountForm, Model model) {
    log.info("アカウント新規登録画面を表示します。");
    model.addAttribute("accountForm", accountForm);
    return "accounts/create";
  }

  // アカウント新規登録を実行する
  @PostMapping("/create")
  public String postAccountCreate(@Validated AccountForm accountForm, BindingResult bindingResult,
      RedirectAttributes redirectAttributes, Model model) {
    log.info("controller アカウント新規登録を実行します。");
    if (bindingResult.hasErrors()) {
      log.warn("アカウント新規登録の入力内容にエラーがあります。{}", bindingResult.getAllErrors());
      model.addAttribute("accountForm", accountForm);
      return "accounts/create";
    }
    MAccount account = modelMapper.map(accountForm, MAccount.class);
    accountService.createNewAccount(account);
    log.info("アカウントの新規登録が完了しました。ID = {}", account.getId());
    redirectAttributes.addFlashAttribute("successMessage", "アカウントの新規登録が完了しました。");
    redirectAttributes.addFlashAttribute("messageType", "success");
    return "redirect:/accounts";
  }

  // アカウント編集画面を表示する
  @GetMapping("/edit/{id}")
  public String getAccountEdit(@PathVariable int id, AccountForm accountForm,
      RedirectAttributes redirectAttributes, Model model) {
    log.info("controller アカウント編集画面を表示します。");
    MAccount account = accountService.getAccountOne(id);
    if (account == null) {
      log.warn("指定されたIDのアカウントが存在しません。ID = {}", id);
      redirectAttributes.addAttribute("errorMessage", "指定されたIDのアカウントは存在しません。");
      redirectAttributes.addAttribute("messageType", "error");
      return "redirect:/accounts";
    }
    account.setPassword(null);
    setupAccountForm(account, accountForm, model);
    model.addAttribute("accountForm", accountForm);
    return "accounts/edit";
  }

  // アカウントの更新を実行する
  @PostMapping("/edit/{id}")
  public String postAccountEdit(@PathVariable int id, @Validated AccountForm accountForm,
      BindingResult bindingResult, RedirectAttributes redirectAttributes, Model model) {
    log.info("controller アカウントの更新を実行します。");
    if (bindingResult.hasErrors()) {
      log.warn("アカウント更新の入力内容にエラーがあります。{}", bindingResult.getAllErrors());
      model.addAttribute("accountForm", accountForm);
      return "accounts/edit";
    }
    MAccount account = modelMapper.map(accountForm, MAccount.class);
    account.setId(id);
    accountService.updateAccount(account);
    log.info("アカウントの更新が完了しました。ID = {}", account.getId());
    redirectAttributes.addFlashAttribute("successMessage", "アカウントの更新が完了しました。");
    redirectAttributes.addFlashAttribute("messageType", "success");
    return "redirect:/accounts";
  }


  @GetMapping("/confirmDelete/{id}")
  public String getAccountDelete(@PathVariable int id, RedirectAttributes redirectAttributes,
      Model model) {
    log.info("アカウント削除確認画面を表示します。");
    MAccount account = accountService.getAccountOne(id);
    if (account == null) {
      log.warn("指定されたIDのアカウントが存在しません。ID = {}", id);
      redirectAttributes.addFlashAttribute("errorMessage", "指定されたIDのアカウントは存在しません。");
      redirectAttributes.addFlashAttribute("messageType", "error");
      return "redirect:/accounts";
    }
    model.addAttribute("account", account);
    return "accounts/confirmDelete";
  }

  // アカウントの削除を実行する
  @PostMapping("/delete/{id}")
  public String postAccountDelete(@PathVariable int id, RedirectAttributes redirectAttributes) {
    log.info("controller アカウントの削除を実行します。");
    accountService.deleteAccount(id);
    log.info("アカウントの削除が完了しました。ID = {}", id);
    redirectAttributes.addFlashAttribute("successMessage", "アカウントの削除が完了しました。");
    redirectAttributes.addFlashAttribute("messageType", "success");
    return "redirect:/accounts";
  }



  // --------------------- 以下、共通メソッド ---------------------
  // AccountFormをセットアップする
  public void setupAccountForm(MAccount account, AccountForm accountForm, Model model) {
    modelMapper.map(account, accountForm);
    model.addAttribute("accountForm", accountForm);
  }
}
