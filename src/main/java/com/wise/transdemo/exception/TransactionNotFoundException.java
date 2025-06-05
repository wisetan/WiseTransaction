package com.wise.transdemo.exception;

import com.wise.transdemo.enums.TransactionErrorCode;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class TransactionNotFoundException extends BaseTransactionException{

    public TransactionNotFoundException() {
        super(TransactionErrorCode.TRANSACTION_RECORD_NOT_FOUND.getCode(), TransactionErrorCode.TRANSACTION_RECORD_NOT_FOUND.getMessage());
    }
}
