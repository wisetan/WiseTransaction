package com.wise.transdemo;

import com.wise.transdemo.repository.TransactionRepository;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;


@Component
public class CacheShutdownHook implements DisposableBean {

    @Autowired
    private TransactionRepository repository;

    public CacheShutdownHook(TransactionRepository repository) {
        this.repository = repository;
    }

    // 容器销毁前执行
    @Override
    public void destroy() {
        repository.saveCacheToDisk();
    }
}
