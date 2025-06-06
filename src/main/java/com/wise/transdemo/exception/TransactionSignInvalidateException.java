package com.wise.transdemo.exception;

import com.wise.transdemo.enums.TransactionErrorCode;

public class TransactionSignInvalidateException extends BaseTransactionException{

    public TransactionSignInvalidateException() {
        super(TransactionErrorCode.TRANSACTION_INVALIDETE_SIGN.getCode(), TransactionErrorCode.TRANSACTION_INVALIDETE_SIGN.getMessage());
    }

    public TransactionSignInvalidateException(String message) {
        super(TransactionErrorCode.TRANSACTION_INVALIDETE_SIGN.getCode(), message);
    }
}
