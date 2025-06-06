package com.wise.transdemo.service.impl;

public interface TransactionLifeCycle {
    /**
     * 开始交易前
     */
    void beginTransaction();

    /**
     * 结束交易后
     */
    void endTransaction();
}
