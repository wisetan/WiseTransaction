package com.wise.transdemo.aop;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.wise.transdemo.domain.Transaction;
import com.wise.transdemo.domain.TransactionRequestSign;
import com.wise.transdemo.enums.TransactionErrorCode;
import com.wise.transdemo.exception.TransactionMissSignException;
import com.wise.transdemo.exception.TransactionSignInvalidateException;
import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

@Aspect
@Component
public class SignatureAspect {

    @Autowired
    TransactionRequestSign requestSign;

    @Autowired
    ObjectMapper objectMapper;

    @Before("@annotation(com.wise.transdemo.annotation.VerifySignature)")
    public void verifySignature(JoinPoint joinPoint) throws Throwable {
        // 获取当前请求
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();

        // 收集请求参数
        Map<String, String> params = new HashMap<>();
        Enumeration<String> paramNames = request.getParameterNames();
        String sign = null;
        while (paramNames.hasMoreElements()) {
            String paramName = paramNames.nextElement();
            if (!paramName.equals("sign")) {
                params.put(paramName, request.getParameter(paramName));
            } else {
                sign = request.getParameter(paramName);
            }
        }

        if (sign == null) {
            throw new TransactionMissSignException();
        }

        // 验证签名
        Transaction transaction = objectMapper.convertValue(params,Transaction.class);
        boolean isValid = requestSign.validateSign(transaction,sign);
        if (!isValid) {
            throw new TransactionSignInvalidateException();
        }
    }
}
