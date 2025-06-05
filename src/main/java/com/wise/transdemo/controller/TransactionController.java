package com.wise.transdemo.controller;


import com.wise.transdemo.annotation.VerifySignature;
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

//    public TransactionController(TransactionService transactionService) {
//        this.transactionService = transactionService;
//    }

    // 创建交易
    @PostMapping
    @VerifySignature
    public ResponseEntity<Transaction> createTransaction(
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
            @RequestParam String accountNumber,
            @RequestParam(required = false) BigDecimal amount) {
        Transaction updatedTransaction = transactionService.updateTransaction(id, accountNumber, amount, null);
        return new ResponseEntity<>(updatedTransaction, HttpStatus.OK);
    }

    // 删除交易
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTransaction(@PathVariable String id,@RequestParam String accountNumber) {
        transactionService.deleteTransaction(id,accountNumber);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
