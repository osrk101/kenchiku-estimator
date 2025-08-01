package com.kenchiku_estimator.exception;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(IllegalArgumentException.class)
    public String handleIllegalArgument(IllegalArgumentException ex, Model model) {
        model.addAttribute("errorMessage", "不正なリクエスト：" + ex.getMessage());
        return "error/invalid_request";
    }

    @ExceptionHandler(EstimateNotFoundException.class)
    public String handleEstimateNotFound(EstimateNotFoundException ex, Model model) {
        model.addAttribute("errorMessage", "見積書が見つかりません：" + ex.getMessage());
        return "error/estimate_not_found";
    }

}
