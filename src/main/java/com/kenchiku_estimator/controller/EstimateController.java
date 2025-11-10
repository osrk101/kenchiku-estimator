package com.kenchiku_estimator.controller;

import java.math.BigDecimal;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.kenchiku_estimator.form.EstimateForm;
import com.kenchiku_estimator.form.EstimateItemForm;
import com.kenchiku_estimator.model.Account;
import com.kenchiku_estimator.model.CustomUserDetails;
import com.kenchiku_estimator.model.Estimate;
import com.kenchiku_estimator.model.EstimateItem;
import com.kenchiku_estimator.service.AccountService;
import com.kenchiku_estimator.service.EstimateItemService;
import com.kenchiku_estimator.service.EstimateService;

import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
@RequestMapping("/estimates")
public class EstimateController {

  @Autowired
  private EstimateService estimateService;

  @Autowired
  private AccountService accountService;

  @Autowired
  private EstimateItemService estimateItemService;

  @Autowired
  ModelMapper modelMapper;

  // 担当する見積書一覧画面を表示する
  // 管理者の場合全ての見積書の一覧画面を表示する
  // 検索条件が指定されていれば検索した見積書一覧画面を表示する
  @GetMapping
  public String viewListEstimates(String searchWords,  @AuthenticationPrincipal CustomUserDetails principal,Model model) {
    log.info("アカウント一覧画面を表示");
    List<Estimate> estimates = null;
    	  
    boolean isAdmin = principal.getAuthorities().stream()
		.anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

      estimates = estimateService.getEstimatesByUser(principal.getId(), isAdmin, searchWords);
      
    log.info("取得された見積書の数 = {}", estimates.size());

    model.addAttribute("estimatesList", estimates);
    model.addAttribute("searchWords", searchWords);

    return "estimates";
  }

  // 見積書の詳細画面を表示する
  @GetMapping("/detail/{id}")
  public String viewEstimateDetail(@PathVariable int id, RedirectAttributes redirectAttributes,
      Model model) throws Exception {
    log.info("controller 見積書詳細画面を表示");

    Estimate estimate = findEstimateAndItems(id, model);

    if (estimate == null) {
      log.warn("指定されたIDの見積書が未存在ID = {}", id);
      redirectAttributes.addFlashAttribute("message", "指定されたIDの見積書は存在しません。");
      redirectAttributes.addFlashAttribute("messageType", "error");
      return "redirect:/estimates";
    }

    return "estimates/detail";
  }

  // 見積書の新規作成画面を表示する
  @GetMapping("/create")
  public String viewCreateEstimateForm(EstimateForm estimateForm, Account mAccount, Model model) {
    log.info("controller 見積書新規作成画面を表示");

    List<Account> fullNameList = accountService.getAllAccountsFullname();
    model.addAttribute("fullNameList", fullNameList);

    estimateForm.getItems().add(new EstimateItemForm());
    model.addAttribute("estimateForm", estimateForm);

    return "estimates/create";
  }

  // 新規見積書の作成をする
  @PostMapping("/create")
  public String createNewEstimate(@Validated EstimateForm estimateForm, BindingResult bindingResult,
      Model model, RedirectAttributes redirectAttributes) throws Exception {
    log.info("controller 新規見積書の作成作業を開始");

    removeEmptyItems(estimateForm); // 空欄行を削除

    if (!bindingResult.hasFieldErrors("items")
        && (estimateForm.getItems() == null || estimateForm.getItems().isEmpty())) {
      bindingResult.rejectValue("items", "message.emptyBreakdown", "見積項目を1つ以上追加してください");
    }

    if (bindingResult.hasErrors()) {
      log.warn("見積書の入力内容にエラー: {}", bindingResult.getAllErrors());

      List<Account> fullNameList = accountService.getAllAccountsFullname();
      model.addAttribute("fullNameList", fullNameList);

      return "estimates/create";
    }

    Estimate estimate = modelMapper.map(estimateForm, Estimate.class);
    estimateService.saveEstimateWithItems(estimate, estimateForm.getItems(), true);

    log.info("新規見積書の作成が完了 ID = {}", estimate.getId());

    redirectAttributes.addFlashAttribute("message", "見積書を作成しました。");
    redirectAttributes.addFlashAttribute("messageType", "success");

    return "redirect:/estimates";
  }

  // 既存見積書の編集画面を表示する
  @GetMapping("/edit/{id}")
  public String viewEditEstimateForm(@PathVariable int id, EstimateForm estimateForm,
      RedirectAttributes redirectAttributes, Model model) throws Exception {
    log.info("controller 見積書編集画面を表示");

    Estimate estimate = findEstimateAndItems(id, model);

    if (estimate == null) {
      redirectAttributes.addFlashAttribute("message", "指定されたIDの見積書は存在しません。");
      redirectAttributes.addFlashAttribute("messageType", "error");
      return "redirect:/estimates";
    }

    setupEditFormForGet(estimate, estimateForm, model);

    return "estimates/edit";
  }

  // 既存見積書の更新をする
  @PostMapping("/edit/{id}")
  public String updateEstimate(@PathVariable int id, @Validated EstimateForm estimateForm,
      BindingResult bindingResult, @RequestParam String action, Model model,
      RedirectAttributes redirectAttributes) throws Exception {
    if (bindingResult.hasErrors()) {
      log.error("見積書の入力内容にエラー: {}", bindingResult.getAllErrors());
      populateChoices(model, estimateForm);
      return "estimates/edit";
    }

    if ("update".equals(action)) {
      log.info("controller 既存見積書の更新処理を開始");

      try {
        Estimate estimate = modelMapper.map(estimateForm, Estimate.class);
        estimate.setId(id);

        estimateService.saveEstimateWithItems(estimate, estimateForm.getItems(), false);

        log.info("見積書の更新が完了。見積書一覧へリダイレクト");

        redirectAttributes.addFlashAttribute("message", "見積書を更新しました。");
        redirectAttributes.addFlashAttribute("messageType", "success");

        return "redirect:/estimates";

      } catch (Exception e) {
        log.error("見積書の更新に失敗: {}", e.getMessage());

        List<Account> fullNameList = accountService.getAllAccountsFullname();
        model.addAttribute("fullNameList", fullNameList);

        bindingResult.reject("error.update", "見積書の更新に失敗");
      }

      return "estimates/edit";

      // 別件で保存をする
    } else if ("saveAsNew".equals(action)) {
      log.info("controller 別件で保存の処理を開始");

      try {
        Estimate estimate = modelMapper.map(estimateForm, Estimate.class);
        estimate.setId(0); // 新規作成のためIDをリセット

        estimateService.saveEstimateWithItems(estimate, estimateForm.getItems(), true);

        log.info("新規見積書の作成が完了。見積書一覧へリダイレクト");

        redirectAttributes.addFlashAttribute("message", "別件で見積書を作成しました。");
        redirectAttributes.addFlashAttribute("messageType", "success");

        return "redirect:/estimates";

      } catch (Exception e) {
        log.error("別件での見積書保存に失敗: {}", e.getMessage());

        bindingResult.reject("error.saveAsNew", "別件での見積書保存に失敗。");
      }
    }

    return "estimates/edit";
  }

  // 見積書削除の確認画面を表示
  @GetMapping("/confirmDelete/{id}")
  public String viewDeleteEstimateConfirm(@PathVariable int id,
      RedirectAttributes redirectAttributes, Model model) throws Exception {
    log.info("controller 見積書削除確認画面を表示");

    Estimate estimate = findEstimateAndItems(id, model);

    if (estimate == null) {
      redirectAttributes.addFlashAttribute("message", "指定されたIDの見積書は存在しません。");
      redirectAttributes.addFlashAttribute("messageType", "error");
      return "redirect:/estimates";
    }

    model.addAttribute("estimate", estimate);

    return "estimates/confirmDelete";
  }

  // 見積書の削除処理
  @PostMapping("/delete/{id}")
  public String deleteEstimate(@PathVariable int id, RedirectAttributes redirectAttributes) {
    log.info("controller 見積書削除処理を開始");

    try {
      estimateService.getEstimateOne(id); // 存在確認

      Boolean isDelatedEstimateItem = estimateItemService.deleteEstimateItem(id); // アイテム削除
      Boolean isDelatedEstimate = estimateService.deleteEstimate(id); // 見積書削除

      if (!isDelatedEstimateItem || !isDelatedEstimate) {
        redirectAttributes.addFlashAttribute("message", "見積書の削除に失敗しました。");
        redirectAttributes.addFlashAttribute("messageType", "error");
        return "redirect:/estimates";
      }

      log.info("見積書（ID: {}）を削除", id);

      redirectAttributes.addFlashAttribute("message", "見積書を削除しました。");
      redirectAttributes.addFlashAttribute("messageType", "success");

    } catch (DataAccessException e) {
      log.error("見積書の削除に失敗: {}", e.getMessage());

      redirectAttributes.addFlashAttribute("message", "見積書の削除に失敗しました。");
      redirectAttributes.addFlashAttribute("messageType", "error");
    }

    return "redirect:/estimates";
  }

  // --------------------- 以下、共通メソッド ---------------------

  // 指定されたIDの見積書と見積書アイテムを取得する
  public Estimate findEstimateAndItems(int id, Model model) throws Exception {
    Estimate estimate = estimateService.getEstimateOne(id);

    if (estimate == null) {
      return estimate;
    }

    log.info("取得された見積書 = {}", estimate);

    List<EstimateItem> items = estimateItemService.findByEstimateId(id);
    log.info("取得された見積書アイテム = {}", items);

    // 合計金額を計算
    BigDecimal total = BigDecimal.ZERO;
    for (EstimateItem item : items) {
      BigDecimal amount = item.getUnitPrice().multiply(item.getQuantity());
      total = total.add(amount);
    }

    model.addAttribute("estimate", estimate);
    model.addAttribute("estimateItems", items);
    model.addAttribute("totalAmount", total);

    return estimate;
  }

  // EstimateFormをセットアップする
  public void setupEditForm(Estimate estimate, EstimateForm estimateForm, Model model) {
    modelMapper.map(estimate, estimateForm);
    model.addAttribute("estimateForm", estimateForm);
  }

  // EstimateItemFormをセットアップする
  public void setupEstimateItemForm(EstimateItem item, EstimateItemForm estimateItemForm,
      Model model) {
    modelMapper.map(item, estimateItemForm);
    model.addAttribute("estimateItemForm", estimateItemForm);
  }

  // 担当者リストの選択肢をセットアップする
  private void populateChoices(Model model, EstimateForm estimateForm) {
    model.addAttribute("fullNameList", accountService.getAllAccountsFullname());
    model.addAttribute("selectedAccountId", estimateForm.getCreatedBy());
  }

  // 編集画面GET 初回表示専用（ここだけフォームを上書き＆addAttribute）
  private void setupEditFormForGet(Estimate estimate, EstimateForm estimateForm, Model model) {
    modelMapper.map(estimate, estimateForm);

    List<EstimateItem> estimateItems = estimateItemService.findByEstimateId(estimate.getId());
    List<EstimateItemForm> itemForms = estimateItems.stream().map(item -> {
      EstimateItemForm f = new EstimateItemForm();
      f.setId(item.getId());
      f.setEstimateId(item.getEstimateId());
      f.setItemName(item.getItemName());
      f.setUnitPrice(item.getUnitPrice());
      f.setQuantity(item.getQuantity());
      f.setUnit(item.getUnit());
      return f;
    }).toList();
    estimateForm.setItems(itemForms);

    model.addAttribute("estimateForm", estimateForm);
    populateChoices(model, estimateForm);
  }

  // 内訳の空欄行を削除する
  public void removeEmptyItems(EstimateForm estimateForm) {
    List<EstimateItemForm> items = estimateForm.getItems();
    items.removeIf(item -> item.getItemName() == null
        || item.getItemName().isBlank() && (item.getUnitPrice() == null)
            && (item.getQuantity() == null) && (item.getUnit() == null));
  }
}
