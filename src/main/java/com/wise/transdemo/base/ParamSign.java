package com.wise.transdemo.base;

import com.wise.transdemo.domain.Transaction;

public interface ParamSign {

    public String signRequest(Transaction transaction);


    public boolean validateSign(Transaction transaction,String sign);
}
