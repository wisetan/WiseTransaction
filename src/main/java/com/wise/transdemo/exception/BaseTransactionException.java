package com.wise.transdemo.exception;

public class BaseTransactionException extends RuntimeException {

    private int errorCode;
    public BaseTransactionException(int errCode,String message ) {
        super("Transaction Exception");
        this.errorCode = errCode;
    }


}
