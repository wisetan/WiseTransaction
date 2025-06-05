package com.wise.transdemo.service;


import com.wise.transdemo.domain.Transaction;
import com.wise.transdemo.domain.TransactionType;
import com.wise.transdemo.exception.TransactionNotFoundException;
import com.wise.transdemo.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Service
//@Transactional
public class TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;

//    public TransactionService(TransactionRepository transactionRepository) {
//        this.transactionRepository = transactionRepository;
//    }

    // 创建新交易
    public Transaction createTransaction(String accountNumber, BigDecimal amount, TransactionType type) {
        // 业务逻辑验证
        if (type == TransactionType.WITHDRAW && amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Withdrawal amount must be positive");
        }

        Transaction transaction = Transaction.create(accountNumber, amount, type);
        return transactionRepository.save(transaction);
    }

    // 根据ID获取交易
    public Transaction getTransactionById(String id) {
        return transactionRepository.findById(id)
                .orElseThrow(() -> new TransactionNotFoundException());
    }

    // 获取所有交易（分页）
    @Cacheable(value = "transactions", key = "{#page, #size}")
    public List<Transaction> getAllTransactions(int page, int size) {
        return transactionRepository.findAll(page, size);
    }

    // 根据账户获取交易（分页）
    @Cacheable(value = "transactions", key = "{#accountNumber, #page, #size}")
    public List<Transaction> getTransactionsByAccount(String accountNumber, int page, int size) {
        return transactionRepository.findByAccountNumber(accountNumber, page, size);
    }

    // 更新交易
    @CacheEvict(value = "transactions", allEntries = true)
    public Transaction updateTransaction(String id, String accountNumber, BigDecimal amount, TransactionType type) {
        Transaction existingTransaction = getTransactionById(id);

        Transaction updatedTransaction = new Transaction(
                id,
                accountNumber != null ? accountNumber : existingTransaction.getAccountNumber(),
                amount != null ? amount : existingTransaction.getAmount(),
                type != null ? type : existingTransaction.getType(),
                existingTransaction.getTimestamp()
        );

        return transactionRepository.save(updatedTransaction);
    }

    // 删除交易
    @CacheEvict(value = "transactions", allEntries = true)
    public void deleteTransaction(UUID id) {
        if (!transactionRepository.existsById(id)) {
            throw new TransactionNotFoundException();
        }
        transactionRepository.deleteById(id);
    }
}


