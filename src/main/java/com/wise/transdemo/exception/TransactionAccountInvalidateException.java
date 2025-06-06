package com.wise.transdemo.exception;

import com.wise.transdemo.enums.TransactionErrorCode;

public class TransactionAccountInvalidateException extends BaseTransactionException{

    public TransactionAccountInvalidateException() {
        super(TransactionErrorCode.TRANSACTION_ACCOUNT_INVALIDATE.getCode(), TransactionErrorCode.TRANSACTION_ACCOUNT_INVALIDATE.getMessage());
    }

    public TransactionAccountInvalidateException(String message) {
        super(TransactionErrorCode.TRANSACTION_ACCOUNT_INVALIDATE.getCode(), message);
    }
}
