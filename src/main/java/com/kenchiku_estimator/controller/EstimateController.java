package com.kenchiku_estimator.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
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
import com.kenchiku_estimator.model.MAccount;
import com.kenchiku_estimator.model.MEstimate;
import com.kenchiku_estimator.model.MEstimateItem;
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


  // 見積書一覧画面を表示する
  // 検索条件が指定されていれば検索した見積書一覧画面を表示する
  @GetMapping
  public String viewListEstimates(String searchWords, Model model) {
    log.info("アカウント一覧画面を表示します。");
    List<MEstimate> estimates = null;
    if (searchWords == null) {
      log.debug("すべての見積書を取得します。");
      estimates = estimateService.getAllEstimates();
    } else {
      log.debug("検索条件を用いた見積書検索をします。：{}", searchWords);
      estimates = estimateService.getSearchEstimates(searchWords);
    }
    log.info("取得された見積書の数 = {}", estimates.size());
    model.addAttribute("estimatesList", estimates);
    model.addAttribute("searchWords", searchWords);
    return "estimates";
  }

  // 見積書の詳細画面を表示する
  @GetMapping("/detail/{id}")
  public String viewEstimateDetail(@PathVariable int id, RedirectAttributes redirectAttributes,
      Model model) throws Exception {
    log.info("controller 見積書詳細画面を表示します。");
    MEstimate estimate = findEstimateAndItems(id, model);
    if (estimate == null) {
      log.warn("指定されたIDの見積書が存在しません。ID = {}", id);
      redirectAttributes.addFlashAttribute("message", "指定されたIDの見積書は存在しません。");
      redirectAttributes.addFlashAttribute("messageType", "error");
      return "redirect:/estimates";
    }
    return "estimates/detail";
  }

  // 見積書の新規作成画面を表示する
  @GetMapping("/create")
  public String viewCreateEstimateForm(EstimateForm estimateForm, MAccount mAccount, Model model) {
    log.info("controller 見積書新規作成画面を表示");
    List<MAccount> fullnameList = accountService.getAllAccountsFullname();
    model.addAttribute("fullnameList", fullnameList);
    estimateForm.getItems().add(new EstimateItemForm());
    model.addAttribute("estimateForm", estimateForm);
    return "estimates/create";
  }

  // 新規見積書の作成をする
  @PostMapping("/create")
  public String createNewEstimate(@Validated EstimateForm estimateForm, BindingResult bindingResult,
      Model model, RedirectAttributes redirectAttributes) throws Exception {
    log.info("controller 新規見積書の作成作業を開始します");
    if (bindingResult.hasErrors()) {
      log.warn("見積書の入力内容にエラーがあります: {}", bindingResult.getAllErrors());
      List<MAccount> fullnameList = accountService.getAllAccountsFullname();
      model.addAttribute("fullnameList", fullnameList);
      return "estimates/create";
    }
    MEstimate estimate = modelMapper.map(estimateForm, MEstimate.class);
    estimateService.saveEstimateWithItems(estimate, estimateForm.getItems(), true);
    log.info("新規見積書の作成が完了しました。 ID = {}", estimate.getId());
    redirectAttributes.addFlashAttribute("message", "見積書を作成しました。");
    redirectAttributes.addFlashAttribute("messageType", "success");
    return "redirect:/estimates";
  }

  // 既存見積書の編集画面を表示する
  @GetMapping("/edit/{id}")
  public String viewEditEstimateForm(@PathVariable int id, EstimateForm estimateForm,
      RedirectAttributes redirectAttributes, Model model) throws Exception {
    log.info("controller 見積書編集画面を表示します。");
    MEstimate estimate = findEstimateAndItems(id, model);
    if (estimate == null) {
      redirectAttributes.addFlashAttribute("message", "指定されたIDの見積書は存在しません。");
      redirectAttributes.addFlashAttribute("messageType", "error");
      return "redirect:/estimates";
    }
    setupEditForm(estimate, estimateForm, model);
    List<MEstimateItem> estimateItems = estimateItemService.findByEstimateId(id);
    log.info("取得された見積書アイテム = {}", estimateItems);
    List<EstimateItemForm> itemForms = estimateItems.stream().map(item -> {
      EstimateItemForm form = new EstimateItemForm();
      form.setEstimateId(item.getEstimateId());
      form.setItemName(item.getItemName());
      form.setUnitPrice(item.getUnitPrice());
      form.setQuantity(item.getQuantity());
      form.setUnit(item.getUnit());
      return form;
    }).toList();

    estimateForm.setItems(itemForms);
    model.addAttribute("estimateForm", estimateForm);
    return "estimates/edit";
  }

  // 既存見積書の更新をする
  @PostMapping("/edit/{id}")
  public String updateEstimate(@PathVariable int id, @Validated EstimateForm estimateForm,
      BindingResult bindingResult, @RequestParam String action, Model model,
      RedirectAttributes redirectAttributes) throws Exception {
    if (bindingResult.hasErrors()) {
      log.error("見積書の入力内容にエラーがあります: {}", bindingResult.getAllErrors());
      bindingResult.reject("error.update", "見積書の入力内容にエラーがあります。");
      setupEditForm(estimateService.getEstimateOne(id), estimateForm, model);
      return "estimates/edit";
    }

    if ("update".equals(action)) {
      log.info("controller 既存見積書の更新処理を開始");
      try {
        MEstimate estimate = modelMapper.map(estimateForm, MEstimate.class);

        estimate.setId(id);
        estimateService.saveEstimateWithItems(estimate, estimateForm.getItems(), false);
        log.info("見積書の更新が完了しました。見積書一覧へリダイレクトします");
        redirectAttributes.addFlashAttribute("message", "見積書を更新しました。");
        redirectAttributes.addFlashAttribute("messageType", "success");
        return "redirect:/estimates";
      } catch (Exception e) {
        log.error("見積書の更新に失敗しました: {}", e.getMessage());
        setupEditForm(estimateService.getEstimateOne(id), estimateForm, model);
        List<MEstimateItem> estimateItems = estimateItemService.findByEstimateId(id);
        List<EstimateItemForm> itemForms = new ArrayList<>();
        for (MEstimateItem item : estimateItems) {
          EstimateItemForm itemForm = new EstimateItemForm();
          setupEstimateItemForm(item, itemForm, model);
          itemForms.add(itemForm);
        }
        estimateForm.setItems(itemForms);
        model.addAttribute("estimateForm", estimateForm);
        bindingResult.reject("error.update", "見積書の更新に失敗しました。");
      }
      return "estimates/edit";

      // 別件で保存をする
    } else if ("saveAsNew".equals(action)) {
      log.info("controller 別件で保存の処理を開始");
      try {
        MEstimate estimate = modelMapper.map(estimateForm, MEstimate.class);
        estimate.setId(0); // 新規登録のためIDをリセット
        estimateService.saveEstimateWithItems(estimate, estimateForm.getItems(), true);
        log.info("新規見積書の登録が完了しました。見積書一覧へリダイレクトします");
        redirectAttributes.addFlashAttribute("message", "別件で見積書を保存しました。");
        redirectAttributes.addFlashAttribute("messageType", "success");
        return "redirect:/estimates";
      } catch (Exception e) {
        log.error("別件での見積書保存に失敗しました: {}", e.getMessage());
        setupEditForm(estimateService.getEstimateOne(id), estimateForm, model);
        List<MEstimateItem> estimateItems = estimateItemService.findByEstimateId(id);
        List<EstimateItemForm> itemForms = new ArrayList<>();
        for (MEstimateItem item : estimateItems) {
          EstimateItemForm itemForm = new EstimateItemForm();
          setupEstimateItemForm(item, itemForm, model);
          itemForms.add(itemForm);
        }
        estimateForm.setItems(itemForms);
        model.addAttribute("estimateForm", estimateForm);
        bindingResult.reject("error.saveAsNew", "別件での見積書保存に失敗しました。");
      }
    }
    return "estimates/edit";
  }



  // 見積書削除の確認画面を表示
  @GetMapping("/confirmDelete/{id}")
  public String viewDeleteEstimateConfirm(@PathVariable int id,
      RedirectAttributes redirectAttributes, Model model) throws Exception {
    log.info("controller 見積書削除確認画面を表示します。");
    MEstimate estimate = findEstimateAndItems(id, model);
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
      log.info("見積書（ID: {}）を削除しました", id);
      redirectAttributes.addFlashAttribute("message", "見積書を削除しました。");
      redirectAttributes.addFlashAttribute("messageType", "success");
    } catch (DataAccessException e) {
      log.error("見積書の削除に失敗しました: {}", e.getMessage());
      redirectAttributes.addFlashAttribute("message", "見積書の削除に失敗しました。");
      redirectAttributes.addFlashAttribute("messageType", "error");
    }
    return "redirect:/estimates";
  }

  // --------------------- 以下、共通メソッド ---------------------
  // 指定されたIDの見積書と見積書アイテムを取得する
  public MEstimate findEstimateAndItems(int id, Model model) throws Exception {
    MEstimate estimate = estimateService.getEstimateOne(id);
    if (estimate == null) {
      return estimate;
    }
    log.info("取得された見積書 = {}", estimate);
    List<MEstimateItem> items = estimateItemService.findByEstimateId(id);
    log.info("取得された見積書アイテム = {}", items);
    // 合計金額を計算
    BigDecimal total = BigDecimal.ZERO;
    for (MEstimateItem item : items) {
      BigDecimal amount = item.getUnitPrice().multiply(item.getQuantity());
      total = total.add(amount);
    }
    model.addAttribute("estimate", estimate);
    model.addAttribute("estimateItems", items);
    model.addAttribute("totalAmount", total);
    return estimate;
  }

  // EstimateFormをセットアップする
  public void setupEditForm(MEstimate estimate, EstimateForm estimateForm, Model model) {
    modelMapper.map(estimate, estimateForm);
    model.addAttribute("estimateForm", estimateForm);
    List<MAccount> fullnameList = accountService.getAllAccountsFullname();
    model.addAttribute("fullnameList", fullnameList);
  }

  // EstimateItemFormをセットアップする
  public void setupEstimateItemForm(MEstimateItem item, EstimateItemForm estimateItemForm,
      Model model) {
    modelMapper.map(item, estimateItemForm);
    model.addAttribute("estimateItemForm", estimateItemForm);
  }
}
