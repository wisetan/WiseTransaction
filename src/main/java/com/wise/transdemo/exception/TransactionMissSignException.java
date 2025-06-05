package com.wise.transdemo.exception;

import com.wise.transdemo.enums.TransactionErrorCode;

public class TransactionMissSignException extends BaseTransactionException {
    public TransactionMissSignException() {
        super(TransactionErrorCode.TRANSACTION_MISS_SIGN.getCode(), TransactionErrorCode.TRANSACTION_MISS_SIGN.getMessage());
    }

    public TransactionMissSignException(String message) {
        super(TransactionErrorCode.TRANSACTION_MISS_SIGN.getCode(), message);
    }
}
