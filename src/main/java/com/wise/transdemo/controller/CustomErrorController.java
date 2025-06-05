package com.wise.transdemo.controller;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Controller
public class CustomErrorController implements ErrorController {

    @RequestMapping(value = "/error", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, Object>> handleError(HttpServletRequest request) {
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);

        if (status != null) {
            int statusCode = Integer.parseInt(status.toString());

            if (statusCode == HttpStatus.NOT_FOUND.value()) {
                return new ResponseEntity<>(createErrorResponse("Resource not found", statusCode), HttpStatus.NOT_FOUND);
            } else if (statusCode == HttpStatus.INTERNAL_SERVER_ERROR.value()) {
                return new ResponseEntity<>(createErrorResponse("Internal server error", statusCode), HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }

        return new ResponseEntity<>(createErrorResponse("Unknown error", HttpStatus.INTERNAL_SERVER_ERROR.value()),
                HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private Map<String, Object> createErrorResponse(String message, int statusCode) {
        Map<String, Object> error = new HashMap<>();
        error.put("timestamp", LocalDateTime.now());
        error.put("status", statusCode);
        error.put("error", HttpStatus.valueOf(statusCode).getReasonPhrase());
        error.put("message", message);
        return error;
    }
}
