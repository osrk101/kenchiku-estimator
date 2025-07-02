package com.kenchiku_estimator.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.kenchiku_estimator.model.MEstimate;
import com.kenchiku_estimator.service.EstimateService;

@Controller
public class EstimateController {

    @Autowired
    private EstimateService estimateService;

    @GetMapping("/estimates")
    public String listEstimates(Model model) {
        List<MEstimate> estimateList = estimateService.getAllEstimates();
        model.addAttribute("estimateList", estimateList);
        return "estimate/list"; // → templates/estimate/list.html を表示
    }
}