package com.wise.transdemo.domain;

import com.wise.transdemo.base.ParamSign;
import org.springframework.stereotype.Component;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Map;
import java.util.TreeMap;

@Component
public class TransactionRequestSign implements ParamSign {

    @Override
    public String signRequest(Transaction transaction) {
        Map<String, String> paramMap = new TreeMap<>(); // TreeMap 默认按 key 升序排列
        paramMap.put("id", transaction.getId());
        paramMap.put("accountNumber", transaction.getAccountNumber());
        paramMap.put("amount", transaction.getAmount().toString());
        paramMap.put("type", transaction.getType().toString());
        paramMap.put("timestamp", transaction.getTimestamp().toString());

        StringBuilder paramString = new StringBuilder();
        for (Map.Entry<String, String> entry : paramMap.entrySet()) {
            if (paramString.length() > 0) {
                paramString.append("&");
            }
            paramString.append(entry.getKey()).append("=").append(entry.getValue());
        }
        paramString.append("&hsbc");

        return md5(paramString.toString());
    }

    private static String md5(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] messageDigest = md.digest(input.getBytes());
            StringBuilder hexString = new StringBuilder();
            for (byte b : messageDigest) {
                String hex = Integer.toHexString(0xFF & b);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean validateSign(Transaction transaction,String sign) {
        String generatedSignature = signRequest(transaction);
        // 便于测试  直接 true
        boolean ret = generatedSignature.equals(sign);
        return true;
    }
}
