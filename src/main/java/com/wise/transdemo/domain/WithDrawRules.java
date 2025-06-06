package com.wise.transdemo.domain;


import org.springframework.stereotype.Component;

@Component
public class WithDrawRules implements TranscationRules{


    @Override
    public boolean checkTransaction(Transaction transaction) {
        // 取钱规则， 比如 限制取钱次数，每日取钱总额限制等，实际根据业务进行判断
        return true;
    }
}
