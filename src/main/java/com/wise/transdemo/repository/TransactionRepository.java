package com.wise.transdemo.repository;


import com.wise.transdemo.domain.CachePersister;
import com.wise.transdemo.domain.Transaction;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Repository
public class TransactionRepository {

    private final CachePersister persister;
    private Map<String, Transaction> transactions = new ConcurrentHashMap<String,Transaction>();
    public TransactionRepository() {
        this.persister = new CachePersister("cache_data.json");

        // 启动时加载历史数据
        loadCacheFromDisk();
    }

    @Scheduled(fixedRate = 5 * 1 * 1000)
    public void saveCachePeriodically() {
        saveCacheToDisk();
    }

    // 保存缓存到磁盘
    public void saveCacheToDisk() {
        persister.save(transactions);
    }

    // 从磁盘加载缓存
    public void loadCacheFromDisk() {
        Map<String, Transaction> loadedData =  persister.load();
        loadedData.forEach((key,value) -> {
            if (transactions == null) {
                transactions = new ConcurrentHashMap<>();
            }
            transactions.put(key, value);
        });
    }

    public Transaction save(Transaction transaction) {
        transactions.put(transaction.getId(), transaction);
        return transaction;
    }

    public Optional<Transaction> findById(String id) {
        return Optional.ofNullable(transactions.get(id));
    }

    public List<Transaction> findAll(int page, int size) {
        return transactions.values().stream()
                .sorted(Comparator.comparing(Transaction::getAccountNumber).reversed())
                .skip((long) page * size)
                .limit(size)
                .collect(Collectors.toList());
    }

    public List<Transaction> findByAccountNumber(String accountNumber, int page, int size) {
        return transactions.values().stream()
                .filter(t -> t.getAccountNumber().equals(accountNumber))
                .sorted(Comparator.comparing(Transaction::getTimestamp).reversed())
                .skip((long) page * size)
                .limit(size)
                .collect(Collectors.toList());
    }

    public long getCountByAccountNumber(String accountNumber) {
        return transactions.values().stream().filter(t -> t.getAccountNumber().equals(accountNumber)).count();
    }

    public void deleteById(String id) {
        transactions.remove(id);
    }

    // 检查交易是否存在
    public boolean existsById(String id) {
        return transactions.containsKey(id);
    }

    // 获取总交易数
    public long count() {
        return transactions.size();
    }
}
