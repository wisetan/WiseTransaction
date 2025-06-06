package com.wise.transdemo.service;


import com.wise.transdemo.domain.*;
import com.wise.transdemo.exception.TransactionAccountInvalidateException;
import com.wise.transdemo.exception.TransactionBlacklistedException;
import com.wise.transdemo.exception.TransactionNotFoundException;
import com.wise.transdemo.repository.TransactionRepository;
import com.wise.transdemo.service.impl.TransactionLifeCycle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class TransactionService implements TransactionLifeCycle {
    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private BlacklistService blacklistService;

    @Autowired
    private List<DepositRules> depositRules;

    @Autowired
    private List<WithDrawRules> withDrawRules;

    @Autowired
    private List<TransferRules> transferRules;

    // 创建新交易
    public Transaction createTransaction(String accountNumber, BigDecimal amount, TransactionType type) {
        // 黑名单验证
        return createTransaction(null,accountNumber,amount,type,null);
    }

    public Transaction createTransaction(String id, String accountNumber, BigDecimal amount, TransactionType type, LocalDateTime timestamp) {
        // 黑名单验证
        if (blacklistService.isAccountBlacklisted(accountNumber)) {
            throw new TransactionBlacklistedException();
        }

        // 业务逻辑验证
        if (type == TransactionType.WITHDRAW && amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Withdrawal amount must be positive");
        }
        beginTransaction();
        Transaction transaction = Transaction.create(id, accountNumber, amount, type, timestamp);
        // 创建前 需要进行业务规则校验
        checkTransaction(transaction);
        Transaction result =  transactionRepository.save(transaction);
        endTransaction();
        return result;
    }

    private void checkTransaction(Transaction transaction) {
        if (transaction.getType() == TransactionType.WITHDRAW) {
            withDrawRules.forEach(item -> item.checkTransaction(transaction));
        } else if(transaction.getType() == TransactionType.TRANSFER) {
            transferRules.forEach(item -> item.checkTransaction(transaction));
        } else if(transaction.getType() == TransactionType.DEPOSIT) {
            depositRules.forEach(item -> item.checkTransaction(transaction));
        }
    }

    // 根据ID获取交易
    public Transaction getTransactionById(String id) {
        return transactionRepository.findById(id)
                .orElseThrow(() -> new TransactionNotFoundException());
    }

    public boolean checkTransactionAccount(Transaction transaction,String account) {
        if (!transaction.getAccountNumber().equals(account)){
            throw new TransactionAccountInvalidateException();
        }
        return true;
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
        // accountNumber 需要校验，防止跨账户更新
        checkTransactionAccount(existingTransaction,accountNumber);
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
    public void deleteTransaction(String id , String accountNumber) {
        if (!transactionRepository.existsById(id)) {
            throw new TransactionNotFoundException();
        }
        // accountNumber 需要校验，防止跨账户修改
        Transaction existingTransaction = getTransactionById(id);
        checkTransactionAccount(existingTransaction,accountNumber);
        transactionRepository.deleteById(id);
    }

    public long getTotalTransactionsCountByAccount(String accountNumber) {
        return transactionRepository.getCountByAccountNumber(accountNumber);
    }

    public long getTotalTransactionsCount() {
        return transactionRepository.count();
    }

    @Override
    public void beginTransaction() {
        //  开始交易动作前
    }

    @Override
    public void endTransaction() {
        // 结束交易动作后
    }
}


