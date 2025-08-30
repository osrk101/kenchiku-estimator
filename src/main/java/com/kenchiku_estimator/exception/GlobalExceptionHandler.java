package com.kenchiku_estimator.exception;

import org.springframework.dao.DataAccessException;
import org.springframework.ui.Model;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import lombok.extern.slf4j.Slf4j;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    // データベース例外の処理
    @ExceptionHandler(DataAccessException.class)
    public String handleDataAccessException(DataAccessException e, Model model) {
        log.error("データベースエラー: {}", e.getMessage());
        model.addAttribute("message", "データベース処理中にエラーが発生しました。");
        model.addAttribute("messageType", "error");
        return "error";
    }

    // 見積書が見つからない場合の例外処理
    @ExceptionHandler(EstimateNotFoundException.class)
    public String handleNotFound(EstimateNotFoundException ex, Model model) {
        model.addAttribute("message", ex.getMessage());
        return "error";
    }

    // バリデーションエラーの例外処理
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public String handleValidation(MethodArgumentNotValidException ex, Model model) {
        model.addAttribute("message", "入力値が不正です。");
        return "error";
    }

    // その他の例外の処理
    @ExceptionHandler(Exception.class)
    public String handleException(Exception e, Model model) {
        log.error("システムエラー: {}", e.getMessage());
        model.addAttribute("message", "予期せぬエラーが発生しました。");
        model.addAttribute("messageType", "error");
        return "error";
    }

}
