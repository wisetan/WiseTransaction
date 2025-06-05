package com.wise.transdemo.domain;


import org.springframework.stereotype.Component;

@Component
public class TransferRules implements TranscationRules{


    @Override
    public boolean checkTransaction(Transaction transaction) {
        // 转账规则 , 比如 日转账限额，总转账限额。日转账次数限制等
        return true;
    }
}
