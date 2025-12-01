package org.tianle.springbootmodule.disruptor;

/**
 * Generic event that wraps a pooled payload.
 */
public class PooledEvent {

    private PooledPayload payload;

    public PooledPayload getPayload() {
        return payload;
    }

    public void setPayload(PooledPayload payload) {
        this.payload = payload;
    }

    public void clear() {
        this.payload = null;
    }
}
