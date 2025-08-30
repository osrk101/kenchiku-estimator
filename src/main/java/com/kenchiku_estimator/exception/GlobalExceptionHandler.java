package com.kenchiku_estimator.exception;

import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    // データベース例外の処理
    @ExceptionHandler(DataAccessException.class)
    public String handleDataAccessException(DataAccessException ex, HttpServletRequest req, HttpServletResponse res) {
        req.setAttribute(RequestDispatcher.ERROR_STATUS_CODE, HttpStatus.INTERNAL_SERVER_ERROR.value());
        req.setAttribute(RequestDispatcher.ERROR_MESSAGE, ex.getMessage());
        return "forward:/error";
    }

    // データベースアクセスエラーの例外処理
    @ExceptionHandler(DataBaseAccessException.class)
    public String handleDataBaseAccessException(DataBaseAccessException ex, HttpServletRequest req,
            HttpServletResponse res) {
        req.setAttribute(RequestDispatcher.ERROR_STATUS_CODE, HttpStatus.INTERNAL_SERVER_ERROR.value());
        req.setAttribute(RequestDispatcher.ERROR_MESSAGE, ex.getMessage());
        return "forward:/error";
    }

    // 見積書が見つからない場合の例外処理
    @ExceptionHandler(EstimateNotFoundException.class)
    public String handleNotFound(EstimateNotFoundException ex, HttpServletRequest req, HttpServletResponse res) {
        req.setAttribute(RequestDispatcher.ERROR_STATUS_CODE, HttpStatus.NOT_FOUND.value());
        req.setAttribute(RequestDispatcher.ERROR_MESSAGE, ex.getMessage());
        return "forward:/error";
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
