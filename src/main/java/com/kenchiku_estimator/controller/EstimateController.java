package com.kenchiku_estimator.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.kenchiku_estimator.form.EstimateForm;
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

    // 見積書を全取得して見積書一覧へ送る 検索ワードが入力されていれば検索して見積書一覧へ送る
    @GetMapping
    public String viewListEstimates(Model model) {
        List<MEstimate> estimatesList = estimateService.getAllEstimates();
        log.info("取得された見積書一覧 = {}", estimatesList);
        model.addAttribute("estimatesList", estimatesList);
        log.info("controller 見積書一覧を表示");
        return "estimates";
    }

    // 見積書の詳細ページを表示する
    @GetMapping("/detail/{id}")
    public String viewEstimate(@PathVariable int id, Model model) {
        log.info("controller 見積書詳細画面を表示");
        MEstimate estimate = estimateService.getEstimateOne(id);
        log.info("取得された見積書 = {}", estimate);
        List<MEstimateItem> estimateItems = estimateItemService.findByEstimateId(id);
        model.addAttribute("estimate", estimate);
        model.addAttribute("estimateItems", estimateItems);
        return "estimates/detail";
    }

    @GetMapping("/create")
    public String createEstimate(EstimateForm esimateForm, MAccount mAccount, Model model) {
        log.info("controller 見積書作成画面を表示");
        List<MAccount> fullnameList = accountService.getAllAccountsFullName();
        model.addAttribute("fullnameList", fullnameList);
        model.addAttribute("estimateForm", esimateForm);
        return "estimates/create";
    }

}