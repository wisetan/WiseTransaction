package com.wise.transdemo.enums;


import lombok.Getter;

@Getter
public enum TransactionErrorCode {

    TRANSACTION_UNKNOWN_ERROR(40000, "未知错误"),
    TRANSACTION_RECORD_NOT_FOUND(40001,"交易记录未找到"),
    TRANSACTION_INVALIDATE_AMOUNT_ERROR(40002,"无效交易"),
    TRANSACTION_INVALIDATE_PARAM(40003,"无效请求参数"),
    TRANSACTION_INVALIDETE_REQUEST(40004,"无效请求"),
    TRANSACTION_MISS_SIGN(40014,"签名不存在"),
    TRANSACTION_INVALIDETE_SIGN(40013,"无效签名"),
    TRANSACTION_DUPLICATE_RECORD(40030,"重复提交记录"),
    TRANSACTION_ACCOUNT_INVALIDATE(40031,"请求账号异常,无权限"),
    TRANSACTION_ACCOUNT_BLOCKED(40099,"账号已被列入黑名单"),
    ;

    private final int code;
    private final String message;

    TransactionErrorCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
