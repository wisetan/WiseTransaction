package com.wise.transdemo.exception;

import com.wise.transdemo.enums.TransactionErrorCode;

public class TransactionBlacklistedException extends BaseTransactionException {

    public TransactionBlacklistedException() {
        super(TransactionErrorCode.TRANSACTION_ACCOUNT_BLOCKED.getCode(), TransactionErrorCode.TRANSACTION_ACCOUNT_BLOCKED.getMessage());
    }

    public TransactionBlacklistedException(String message) {
        super(TransactionErrorCode.TRANSACTION_ACCOUNT_BLOCKED.getCode(), message);
    }
}
