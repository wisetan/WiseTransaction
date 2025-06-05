package com.wise.transdemo.domain;


import org.springframework.stereotype.Component;

@Component
public class DepositRules implements TranscationRules{
    @Override
    public boolean checkTransaction(Transaction transaction) {
        // 存钱规则  不满足规则throw exception

        return true;
    }
}
