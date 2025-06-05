package com.wise.transdemo.controller;


import com.wise.transdemo.domain.Transaction;
import com.wise.transdemo.domain.TransactionType;
import com.wise.transdemo.service.TransactionService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/transactions")
@Validated
public class TransactionController {

    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    // 创建交易
    @PostMapping
    public ResponseEntity<Transaction> createTransaction(
            @NotNull(message = "账户号码不能为空") @RequestParam String accountNumber,
            @DecimalMax(value = "1000.00", message = "交易金额不能超过1000000.00") @NotNull(message = "交易金额不能为空") @RequestParam BigDecimal amount,
            @NotNull(message = "交易类型不能为空") @RequestParam TransactionType type) {
        Transaction transaction = transactionService.createTransaction(accountNumber, amount, type);
        return new ResponseEntity<>(transaction, HttpStatus.CREATED);
    }

    // 获取单个交易
    @GetMapping("/{id}")
    public ResponseEntity<Transaction> getTransaction(@PathVariable String id) {
        Transaction transaction = transactionService.getTransactionById(id);
        return new ResponseEntity<>(transaction, HttpStatus.OK);
    }

    // 获取所有交易（分页）
    @GetMapping
    public ResponseEntity<List<Transaction>> getAllTransactions(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        List<Transaction> transactions = transactionService.getAllTransactions(page, size);
        return new ResponseEntity<>(transactions, HttpStatus.OK);
    }

    // 根据账户获取交易（分页）
    @GetMapping("/account/{accountNumber}")
    public ResponseEntity<List<Transaction>> getTransactionsByAccount(
            @PathVariable String accountNumber,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        List<Transaction> transactions = transactionService.getTransactionsByAccount(accountNumber, page, size);
        return new ResponseEntity<>(transactions, HttpStatus.OK);
    }

    // 更新交易
    @PutMapping("/{id}")
    public ResponseEntity<Transaction> updateTransaction(
            @PathVariable String id,
            @RequestParam(required = false) String accountNumber,
            @RequestParam(required = false) BigDecimal amount,
            @RequestParam(required = false) TransactionType type) {
        Transaction updatedTransaction = transactionService.updateTransaction(id, accountNumber, amount, type);
        return new ResponseEntity<>(updatedTransaction, HttpStatus.OK);
    }

    // 删除交易
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTransaction(@PathVariable UUID id) {
        transactionService.deleteTransaction(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
