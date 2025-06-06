package com.wise.transdemo.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.validation.annotation.Validated;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.UUID;

@Data
@Validated
public class Transaction {

    String id;              // 交易唯一标识
    String accountNumber;  // 账户号码

    BigDecimal amount;      // 交易金额

    TransactionType type;   // 交易类型

    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime timestamp; // 交易时间

    public Transaction() {}

    public Transaction(String id, String accountNumber, BigDecimal amount, TransactionType type, LocalDateTime timestamp) {
        this.id = id;
        this.accountNumber = accountNumber;
        this.amount = amount;
        this.type = type;
        this.timestamp = timestamp;
    }

    public static Transaction create(String accountNumber, BigDecimal amount, TransactionType type) {
        return create(
                UUID.randomUUID().toString(),
                accountNumber,
                amount,
                type,
                LocalDateTime.now()
        );
    }

    public static Transaction create(String id,String accountNumber, BigDecimal amount, TransactionType type) {
        return new Transaction(
                id,
                accountNumber,
                amount,
                type,
                LocalDateTime.now()
        );
    }

    public static Transaction create(String id,String accountNumber, BigDecimal amount, TransactionType type,LocalDateTime timestamp) {
        return new Transaction(
                id,
                accountNumber,
                amount,
                type,
                timestamp
        );
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public TransactionType getType() {
        return type;
    }

    public void setType(TransactionType type) {
        this.type = type;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}

