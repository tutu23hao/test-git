package org.tianle.springbootmodule.disruptor;

public class Position implements PooledPayload {

    private long portfolioId;
    private String symbol;
    private long quantity;

    public long getPortfolioId() {
        return portfolioId;
    }

    public void setPortfolioId(long portfolioId) {
        this.portfolioId = portfolioId;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public long getQuantity() {
        return quantity;
    }

    public void setQuantity(long quantity) {
        this.quantity = quantity;
    }

    @Override
    public void reset() {
        this.portfolioId = 0;
        this.symbol = null;
        this.quantity = 0;
    }

    @Override
    public String describe() {
        return "Position{" +
                "portfolioId=" + portfolioId +
                ", symbol='" + symbol + '\'' +
                ", quantity=" + quantity +
                '}';
    }
}
