package com.wise.transdemo.exception;

import com.wise.transdemo.enums.TransactionErrorCode;

public class TransactionDuplicateIdException extends BaseTransactionException {

    public TransactionDuplicateIdException() {
        super(TransactionErrorCode.TRANSACTION_DUPLICATE_RECORD.getCode(), TransactionErrorCode.TRANSACTION_DUPLICATE_RECORD.getMessage());
    }

    public TransactionDuplicateIdException(String message) {
        super(TransactionErrorCode.TRANSACTION_DUPLICATE_RECORD.getCode(), message);
    }
}
