package com.kenchiku_estimator.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import com.kenchiku_estimator.form.EstimateForm;
import com.kenchiku_estimator.model.MAccount;
import com.kenchiku_estimator.model.MEstimate;
import com.kenchiku_estimator.service.AccountService;
import com.kenchiku_estimator.service.EstimateService;

import lombok.extern.slf4j.Slf4j;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@Slf4j
@RequestMapping("/estimates")
public class EstimateController {

    @Autowired
    private EstimateService estimateService;

    @Autowired
    private AccountService accountService;

    // 見積書を全取得して見積書一覧へ送る 検索ワードが入力されていれば検索して見積書一覧へ送る
    @GetMapping
    public String listEstimates(Model model) {
        List<MEstimate> estimatesList = estimateService.getAllEstimates();
        model.addAttribute("estimatesList", estimatesList);
        log.info("controller 見積書一覧を表示");
        return "estimates";
    }

    // 見積書の詳細ページを表示する
    @GetMapping("/{id}")
    public String viewEstimate(@RequestParam("id") int id, Model model) {
        log.info("controller 見積書詳細画面を表示");
        MEstimate estimate = estimateService.getEstimateOne(id);
        model.addAttribute("estimate", estimate);
        model.addAttribute("id", id);

        return "estimates/detail";
    }

    // 見積書新規作成ページを担当者リストを取得して表示する
    @GetMapping("/create")
    public String showCreateForm(Model model) {
        log.info("controller 見積書新規作成画面を表示");
        List<MAccount> accountsList = accountService.getAllAccounts();
        model.addAttribute("AccountsList", accountsList);
        model.addAttribute("estimateForm", new EstimateForm());
        return "/estimates/create";
    }

    // 既存見積書の編集ページを担当者リストを取得して表示する
    @GetMapping("/edit")
    public String showEditForm(@RequestParam("id") int id, Model model) {
        log.info("Controller 見積書編集画面を表示");
        MEstimate estimate = estimateService.getEstimateOne(id);
        List<MAccount> accountsList = accountService.getAllAccounts();
        model.addAttribute("AccountsList", accountsList);
        model.addAttribute("estimate", estimate);
        model.addAttribute("estimateForm", new EstimateForm());
        return "/estimates/edit";
    }

    // 新規見積書・編集した見積書をデータベースに登録
    @PostMapping("/estimates")
    public String submitEstimate(EstimateForm estimateForm, Model model) {

        // 新規見積書・編集した見積書を上書き保存・編集した見積書を別件で保存それぞれの処理を書く/
        return "estimates";
    }

    // 見積書の削除確認
    @PostMapping("/estimates/delete/{id}")
    public String deleteEstimate(Model model) {

        return "estimates";
    }
}