package com.wise.transdemo;

import com.wise.transdemo.annotation.VerifySignature;
import com.wise.transdemo.base.GlobalExceptionHandler;
import com.wise.transdemo.controller.TransactionController;
import com.wise.transdemo.domain.Transaction;
import com.wise.transdemo.domain.TransactionType;
import com.wise.transdemo.enums.TransactionErrorCode;
import com.wise.transdemo.exception.*;
import com.wise.transdemo.service.BlacklistService;
import com.wise.transdemo.service.TransactionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

//@WebMvcTest(TransactionController.class)
@SpringBootTest
@AutoConfigureMockMvc
public class TransactionControllerTest {

    private MockMvc mockMvc;

    @Mock
    private TransactionService transactionService;

    @Mock
    private BlacklistService blacklistService;

    @InjectMocks
    private TransactionController transactionController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(transactionController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    // ================ 正常流程测试 ================

    @Test
    public void testCreateTransaction_Success() throws Exception {
        String id = "123";
        String accountNumber = "456";
        BigDecimal amount = new BigDecimal("100.00");
        TransactionType type = TransactionType.DEPOSIT;
        LocalDateTime timestamp = LocalDateTime.now();

        Transaction transaction = new Transaction(id, accountNumber, amount, type, timestamp);
        when(transactionService.getTransactionById(id)).thenReturn(null);
        when(transactionService.createTransaction(anyString(), anyString(), any(BigDecimal.class), any(TransactionType.class), any(LocalDateTime.class)))
                .thenReturn(transaction);
        when(blacklistService.isAccountBlacklisted(accountNumber)).thenReturn(false);

        Map<String, String> params = new HashMap<>();
        params.put("id", id);
        params.put("accountNumber", accountNumber);
        params.put("amount", amount.toString());
        params.put("type", type.toString());
        params.put("timestamp", timestamp.toString());
        String sign = "test";

        mockMvc.perform(post("/transactions")
                        .params(buildMockParams(params))
                        .param("sign", sign))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.accountNumber").value(accountNumber))
                .andExpect(jsonPath("$.amount").value(amount.doubleValue()))
                .andExpect(jsonPath("$.type").value(type.toString()));
    }

    @Test
    public void testGetTransaction_Success() throws Exception {
        String id = "123";
        Transaction transaction = new Transaction(id, "456", new BigDecimal("100.00"), TransactionType.DEPOSIT, LocalDateTime.now());
        when(transactionService.getTransactionById(id)).thenReturn(transaction);

        mockMvc.perform(get("/transactions/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id));
    }

    @Test
    public void testGetAllTransactions_Success() throws Exception {
        when(transactionService.getAllTransactions(0, 10)).thenReturn(Arrays.asList(
                new Transaction("1", "456", new BigDecimal("100.00"), TransactionType.DEPOSIT, LocalDateTime.now())
        ));

        mockMvc.perform(get("/transactions")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));
    }

    // ================ 参数校验测试 ================

    @Test
    public void testCreateTransaction_MissingId() throws Exception {

        mockMvc.perform(post("/transactions")
                        .param("accountNumber", "456")
                        .param("amount", "100.00")
                        .param("type", "DEPOSIT")
                        .param("timestamp", LocalDateTime.now().toString()))
                .andExpect(status().is5xxServerError())
                .andExpect(jsonPath("$.code").value(TransactionErrorCode.TRANSACTION_INVALIDATE_PARAM.getCode()));
    }

    @Test
    public void testCreateTransaction_InvalidAmount() throws Exception {
        when(blacklistService.isAccountBlacklisted("abc")).thenReturn(false);

        mockMvc.perform(post("/transactions")
                        .param("id", "123")
                        .param("accountNumber", "456")
                        .param("amount", "1000000.00") // 超过最大金额
                        .param("type", "DEPOSIT")
                        .param("timestamp", LocalDateTime.now().toString()))
                .andExpect(status().is5xxServerError())
                .andExpect(jsonPath("$.code").value(TransactionErrorCode.TRANSACTION_INVALIDATE_PARAM.getCode()));
    }

    // ================ 签名验证测试 ================

    @Test
    public void testCreateTransaction_MissingSign() throws Exception {
        mockMvc.perform(post("/transactions")
                        .param("id", "123")
                        .param("accountNumber", "456")
                        .param("amount", "100.00")
                        .param("type", "DEPOSIT")
                        .param("timestamp", LocalDateTime.now().toString()))
                .andExpect(status().is5xxServerError())
                .andExpect(jsonPath("$.code").value(TransactionErrorCode.TRANSACTION_MISS_SIGN.getCode()));
    }

    @Test
    public void testCreateTransaction_InvalidSign() throws Exception {
        Map<String, String> params = new HashMap<>();
        params.put("id", "123");
        params.put("accountNumber", "456");
        params.put("amount", "100.00");
        params.put("type", "DEPOSIT");
        params.put("timestamp", LocalDateTime.now().toString());

        mockMvc.perform(post("/transactions")
                        .params(buildMockParams(params))
                        .param("sign", "invalidSign"))
                .andExpect(status().is5xxServerError())
                .andExpect(jsonPath("$.code").value(TransactionErrorCode.TRANSACTION_INVALIDETE_SIGN.getCode()));
    }

    // ================ 黑名单测试 ================

    @Test
    public void testCreateTransaction_BlacklistedAccount() throws Exception {
        String accountNumber = "blacklisted";
        when(blacklistService.isAccountBlacklisted(accountNumber)).thenReturn(true);

        Map<String, String> params = new HashMap<>();
        params.put("id", "123");
        params.put("accountNumber", accountNumber);
        params.put("amount", "100.00");
        params.put("type", "DEPOSIT");
        params.put("timestamp", LocalDateTime.now().toString());
        String sign = "test";

        mockMvc.perform(post("/transactions")
                        .params(buildMockParams(params))
                        .param("sign", sign))
                .andExpect(status().is5xxServerError())
                .andExpect(jsonPath("$.code").value(TransactionErrorCode.TRANSACTION_ACCOUNT_BLOCKED.getCode()));
    }

    // ================ 异常处理测试 ================

    @Test
    public void testCreateTransaction_DuplicateId() throws Exception {
        String id = "123";
        when(transactionService.getTransactionById(id)).thenReturn(new Transaction());

        Map<String, String> params = new HashMap<>();
        params.put("id", id);
        params.put("accountNumber", "456");
        params.put("amount", "100.00");
        params.put("type", "DEPOSIT");
        params.put("timestamp", LocalDateTime.now().toString());
        String sign = "dd";//SignUtil.generateSign(params, "hsbc");

        mockMvc.perform(post("/transactions")
                        .params(buildMockParams(params))
                        .param("sign", sign))
                .andExpect(status().is5xxServerError())
                .andExpect(jsonPath("$.code").value(TransactionErrorCode.TRANSACTION_DUPLICATE_RECORD.getCode()));
    }

    @Test
    public void testGetTransaction_NotFound() throws Exception {
        String id = "123";
        when(transactionService.getTransactionById(id)).thenThrow(new TransactionNotFoundException());

        mockMvc.perform(get("/transactions/{id}", id))
                .andExpect(status().is5xxServerError())
                .andExpect(jsonPath("$.code").value(TransactionErrorCode.TRANSACTION_RECORD_NOT_FOUND.getCode()));
    }

    // ================ 辅助方法 ================

    private org.springframework.util.MultiValueMap<String, String> buildMockParams(Map<String, String> params) {
        org.springframework.util.LinkedMultiValueMap<String, String> map = new org.springframework.util.LinkedMultiValueMap<>();
        params.forEach(map::add);
        return map;
    }
}