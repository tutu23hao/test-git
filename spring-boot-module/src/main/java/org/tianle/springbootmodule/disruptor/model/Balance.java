package org.tianle.springbootmodule.disruptor.model;

import org.tianle.springbootmodule.disruptor.core.PooledPayload;

import java.math.BigDecimal;

public class Balance implements PooledPayload {

    private long accountId;
    private BigDecimal amount;
    private String currency;

    public long getAccountId() {
        return accountId;
    }

    public void setAccountId(long accountId) {
        this.accountId = accountId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    @Override
    public void reset() {
        this.accountId = 0;
        this.amount = null;
        this.currency = null;
    }

    @Override
    public String describe() {
        return "Balance{" +
                "accountId=" + accountId +
                ", amount=" + amount +
                ", currency='" + currency + '\'' +
                '}';
    }
}
