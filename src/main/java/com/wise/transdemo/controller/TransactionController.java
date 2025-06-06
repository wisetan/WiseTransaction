package com.wise.transdemo.controller;


import com.wise.transdemo.annotation.VerifySignature;
import com.wise.transdemo.base.CommonResponse;
import com.wise.transdemo.base.PageResponse;
import com.wise.transdemo.base.ResponseFactory;
import com.wise.transdemo.domain.Transaction;
import com.wise.transdemo.domain.TransactionType;
import com.wise.transdemo.exception.TransactionDuplicateIdException;
import com.wise.transdemo.exception.TransactionNotFoundException;
import com.wise.transdemo.service.TransactionService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/transactions")
@Validated
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    // 创建交易
    @PostMapping
    @VerifySignature
    public ResponseEntity<CommonResponse<Transaction>> createTransaction(
            @NotNull(message = "交易记录号不能为空") @RequestParam String id,
            @NotNull(message = "账户号码不能为空") @RequestParam String accountNumber,
            @DecimalMax(value = "1000.00", message = "交易金额不能超过10000.00") @NotNull(message = "交易金额不能为空") @RequestParam BigDecimal amount,
            @NotNull(message = "交易类型不能为空") @RequestParam TransactionType type,
            @NotNull(message = "交易时间不能为空") @RequestParam LocalDateTime timestamp) {
        try {
            Transaction transaction = transactionService.getTransactionById(id);

            if (!ObjectUtils.isEmpty(transaction)) {
                throw new TransactionDuplicateIdException();
            }
        } catch (TransactionNotFoundException e) {
            e.printStackTrace();
        }
        Transaction transaction = transactionService.createTransaction(id, accountNumber, amount, type, timestamp);
        if (transaction == null) {
            throw new RuntimeException("UNKNOWN");
        }
        CommonResponse<Transaction> response = ResponseFactory.createCommonResponse(201, "交易创建成功", transaction);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    // 获取单个交易
    @GetMapping("/{id}")
    public ResponseEntity<CommonResponse<Transaction>> getTransaction(@PathVariable String id) {
        Transaction transaction = transactionService.getTransactionById(id);
        CommonResponse<Transaction> response = ResponseFactory.createCommonResponse(200, "获取交易成功", transaction);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // 获取所有交易（分页）
    @GetMapping
    public ResponseEntity<PageResponse<Transaction>> getAllTransactions(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        List<Transaction> transactions = transactionService.getAllTransactions(page, size);
        long totalElements = transactionService.getTotalTransactionsCount();
        int totalPages = (int) Math.ceil((double) totalElements / size);
        PageResponse<Transaction> response = ResponseFactory.createPageResponse(200, "获取所有交易成功", transactions, page, size, totalPages, totalElements);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // 根据账户获取交易（分页）
    @GetMapping("/account/{accountNumber}")
    public ResponseEntity<PageResponse<Transaction>> getTransactionsByAccount(
            @PathVariable String accountNumber,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        List<Transaction> transactions = transactionService.getTransactionsByAccount(accountNumber, page, size);
        long totalElements = transactionService.getTotalTransactionsCountByAccount(accountNumber);
        int totalPages = (int) Math.ceil((double) totalElements / size);
        PageResponse<Transaction> response = ResponseFactory.createPageResponse(200, "根据账户获取交易成功", transactions, page, size, totalPages, totalElements);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // 更新交易
    @PutMapping("/{id}")
    public ResponseEntity<CommonResponse<Transaction>>  updateTransaction(
            @PathVariable String id,
            @RequestParam String accountNumber,
            @RequestParam(required = false) BigDecimal amount) {
        Transaction updatedTransaction = transactionService.updateTransaction(id, accountNumber, amount, null);
        CommonResponse<Transaction> response = ResponseFactory.createCommonResponse(200, "交易更新成功", updatedTransaction);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // 删除交易
    @DeleteMapping("/{id}")
    public ResponseEntity<CommonResponse<Void>> deleteTransaction(@PathVariable String id,@RequestParam String accountNumber) {
        transactionService.deleteTransaction(id,accountNumber);
        CommonResponse<Void> response = ResponseFactory.createCommonResponse(204, "交易删除成功", null);
        return new ResponseEntity<>(response, HttpStatus.NO_CONTENT);
    }
}
