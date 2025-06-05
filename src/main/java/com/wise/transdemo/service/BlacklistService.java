package com.wise.transdemo.service;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.Arrays;
import java.util.List;

@Service
public class BlacklistService {

    @Value("${transaction.blacklist.accounts:abc}")
    private String blacklistAccounts;

    public boolean isAccountBlacklisted(String accountNumber) {
        List<String> blacklist = Arrays.asList(blacklistAccounts.split(","));
        if (!ObjectUtils.isEmpty(blacklist)) {
            return blacklist.contains(accountNumber);
        }
        return false;
    }
}
