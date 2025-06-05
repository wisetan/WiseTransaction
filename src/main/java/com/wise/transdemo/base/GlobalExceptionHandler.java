package com.wise.transdemo.base;

import com.wise.transdemo.exception.TransactionNotFoundException;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;


@ControllerAdvice
public class GlobalExceptionHandler {

//    @ExceptionHandler(TransactionNotFoundException.class)
//    public ResponseEntity<Object> handleTransactionNotFoundException(
//            TransactionNotFoundException ex, WebRequest request) {
//
//        Map<String, Object> body = new HashMap<>();
//        body.put("timestamp", LocalDateTime.now());
//        body.put("status", HttpStatus.NOT_FOUND.value());
//        body.put("error", "Not Found");
//        body.put("message", ex.getMessage());
//
//        return new ResponseEntity<>(body, HttpStatus.NOT_FOUND);
//    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleGeneralException(
            Exception ex, WebRequest request) {

        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
        body.put("error", ex.getMessage());
        body.put("message", ex.getMessage());

        return new ResponseEntity<>(body, HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
