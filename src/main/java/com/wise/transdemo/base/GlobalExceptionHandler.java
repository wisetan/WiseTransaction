package com.wise.transdemo.base;

import com.wise.transdemo.enums.TransactionErrorCode;
import com.wise.transdemo.exception.TransactionBlacklistedException;
import com.wise.transdemo.exception.TransactionMissSignException;
import com.wise.transdemo.exception.TransactionNotFoundException;
import jakarta.validation.ConstraintViolationException;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;


@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleGeneralException(
            Exception ex, WebRequest request) {

        LocalDateTime timestamp = LocalDateTime.now();
        int status = HttpStatus.INTERNAL_SERVER_ERROR.value();
        int code = TransactionErrorCode.TRANSACTION_UNKNOWN_ERROR.getCode();
        String message = ex.getMessage();

        if (ex instanceof MissingServletRequestParameterException || ex instanceof MethodArgumentNotValidException || ex instanceof ConstraintViolationException) {
            code = TransactionErrorCode.TRANSACTION_INVALIDATE_PARAM.getCode();
        } else if (ex instanceof TransactionMissSignException) {
            code = TransactionErrorCode.TRANSACTION_MISS_SIGN.getCode();
        } else if (ex instanceof TransactionBlacklistedException) {
            code = TransactionErrorCode.TRANSACTION_ACCOUNT_BLOCKED.getCode();
        } else if (ex instanceof TransactionNotFoundException) {
            code = TransactionErrorCode.TRANSACTION_RECORD_NOT_FOUND.getCode();
        }

        CommonResponse response = new CommonResponse(code, message,timestamp);
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
