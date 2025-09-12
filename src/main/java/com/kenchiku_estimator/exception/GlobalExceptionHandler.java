package com.kenchiku_estimator.exception;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import lombok.extern.slf4j.Slf4j;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

  // データベースアクセスエラーの例外処理
  @ExceptionHandler(DataAccessException.class)
  public String handleDataAccessException(DataAccessException ex, HttpServletRequest req,
      HttpServletResponse res) {
    log.error("DBアクセスエラー", ex);
    req.setAttribute(RequestDispatcher.ERROR_STATUS_CODE, HttpStatus.INTERNAL_SERVER_ERROR.value());
    req.setAttribute("message", "システムエラーが発生しました。（DBアクセス）");
    req.setAttribute("messageType", "error");
    return "forward:/error";
  }

  // その他の例外の処理
  @ExceptionHandler(Exception.class)
  public String handleException(Exception e, HttpServletRequest req, HttpServletResponse res) {
    req.setAttribute(RequestDispatcher.ERROR_STATUS_CODE, HttpStatus.INTERNAL_SERVER_ERROR.value());
    req.setAttribute(RequestDispatcher.ERROR_MESSAGE, "予期せぬエラーが発生しました。");
    log.error("予期せぬエラーが発生: {}", e.getMessage(), e);
    return "forward:/error";
  }


}
