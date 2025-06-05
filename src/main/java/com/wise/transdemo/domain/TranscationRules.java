package com.wise.transdemo.domain;



public interface TranscationRules {

    /**
     * 校验transaction 的 交易规则
     *
     * @param transaction
     * @return
     */
    boolean checkTransaction(Transaction transaction);
}
