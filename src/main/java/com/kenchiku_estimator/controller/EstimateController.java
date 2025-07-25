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

    // 指定されたIDの見積書を取得してスコープに格納する
    public MEstimate findEstimateAndItems(int id, Model model) throws Exception {
        MEstimate estimate = estimateService.getEstimateOne(id);
        log.info("取得された見積書 = {}", estimate);
        if (estimate == null) {
            throw new Exception("指定された見積書（ID: " + id + "）は存在しません。");
        }
        List<MEstimateItem> estimateItems = estimateItemService.findByEstimateId(id);
        log.info("取得された見積書アイテム = {}", estimateItems);
        model.addAttribute("estimate", estimate);
        model.addAttribute("estimateItems", estimateItems);
        return estimate;
    }

    // EstimateFormをセットアップする
    public void setupEditForm(MEstimate estimate, EstimateForm estimateForm, Model model) {
        modelMapper.map(estimate, estimateForm);
        model.addAttribute("estimateForm", estimateForm);
        List<MAccount> fullnameList = accountService.getAllAccountsFullname();
        model.addAttribute("fullnameList", fullnameList);
    }

    // 見積書を全取得して見積書一覧へ送る 検索ワードが入力されていれば検索して見積書一覧へ送る
    @GetMapping
    public String viewListEstimates(@ModelAttribute("message") String message, Model model) {
        List<MEstimate> estimatesList = estimateService.getAllEstimates();
        log.info("取得された見積書一覧 = {}", estimatesList);
        model.addAttribute("estimatesList", estimatesList);
        log.info("controller 見積書一覧を表示");
        return "estimates";
    }

    // 見積書の詳細ページを表示する
    @GetMapping("/detail/{id}")
    public String viewEstimateDetail(@PathVariable int id, Model model) throws Exception {
        log.info("controller 見積書詳細画面を表示");
        findEstimateAndItems(id, model);
        return "estimates/detail";
    }

    // 既存見積書の編集ページを表示する
    @GetMapping("/edit/{id}")
    public String ViewEditEstimateForm(@PathVariable int id, EstimateForm estimateForm, BindingResult bindingResult,
            Model model) throws Exception {
        log.info("controller 見積書編集画面を表示");
        MEstimate estimate = findEstimateAndItems(id, model);
        setupEditForm(estimate, estimateForm, model);
        List<MEstimateItem> estimateItems = estimateItemService.findByEstimateId(id);
        log.info("取得された見積書アイテム = {}", estimateItems);
        List<EstimateItemForm> itemForms = estimateItems.stream()
                .map(item -> {
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

    // 新規見積書作成画面を表示する
    @GetMapping("/create")
    public String viewCreateEstimateForm(EstimateForm estimateForm, MAccount mAccount, Model model) {
        log.info("controller 見積書作成画面を表示");
        List<MAccount> fullnameList = accountService.getAllAccountsFullname();
        model.addAttribute("fullnameList", fullnameList);
        estimateForm.getItems().add(new EstimateItemForm());
        model.addAttribute("estimateForm", estimateForm);
        return "estimates/create";
    }

    // 新規見積書の登録
    @PostMapping("/create")
    public String createNewEstimate(@Validated EstimateForm estimateForm, BindingResult bindingResult,
            Model model,
            RedirectAttributes redirectAttributes) {
        log.info("controller 新規見積書の登録作業を開始します");
        if (bindingResult.hasErrors()) {
            log.error("見積書の入力内容にエラーがあります: {}", bindingResult.getAllErrors());
            List<MAccount> fullnameList = accountService.getAllAccountsFullname();
            model.addAttribute("fullnameList", fullnameList);
            return "estimates/create";
        }
        try {
            MEstimate estimate = modelMapper.map(estimateForm, MEstimate.class);
            String estimateNumber = estimateService.generateEstimateNumber();
            estimate.setEstimateNumber(estimateNumber);
            estimateService.createNewEstimate(estimate);
            log.info("新規見積書の登録を完了しました: {}", estimate);

            for (EstimateItemForm itemForm : estimateForm.getItems()) {
                MEstimateItem item = modelMapper.map(itemForm, MEstimateItem.class);
                item.setEstimateId(estimate.getId());
                log.info("新規見積書アイテムを登録を開始します: {}", itemForm);
                estimateItemService.createEstimateItem(item);
            }
        } catch (Exception e) {
            log.error("新規見積書の登録に失敗しました: {}", e.getMessage());
            bindingResult.reject("error.create", "新規見積書の登録に失敗しました。");
            return "estimates/create";
        }
        log.info("新規見積書の登録が完了しました。見積書一覧へリダイレクトします");
        redirectAttributes.addFlashAttribute("message", "見積書を登録しました。");
        return "redirect:/estimates";
    }

    // 見積書削除の確認画面を表示
    @GetMapping("/confirmDelete/{id}")
    public String viewDeleteEstimateConfirm(@PathVariable int id, Model model) throws Exception {
        log.info("controller 見積書削除確認画面を表示");
        MEstimate estimate = findEstimateAndItems(id, model);
        model.addAttribute("estimate", estimate);
        return "estimates/confirmDelete";
    }

    // 見積書の削除処理
    @PostMapping("/delete/{id}")
    public String deleteEstimate(@PathVariable int id, RedirectAttributes redirectAttributes) {
        log.info("controller 見積書削除処理を開始");
        try {
            estimateService.getEstimateOne(id); // 存在確認
            estimateItemService.deleteEstimateItem(id); // アイテム削除
            estimateService.deleteEstimate(id); // 見積書削除
            log.info("見積書（ID: {}）を削除しました", id);
            redirectAttributes.addFlashAttribute("message", "見積書を削除しました。");
        } catch (Exception e) {
            log.error("見積書の削除に失敗しました: {}", e.getMessage());
            redirectAttributes.addFlashAttribute("message", "見積書の削除に失敗しました。");
        }
        return "redirect:/estimates";
    }

}