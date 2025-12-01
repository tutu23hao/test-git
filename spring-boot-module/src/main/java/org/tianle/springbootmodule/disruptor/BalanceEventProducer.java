package org.tianle.springbootmodule.disruptor;

import com.lmax.disruptor.RingBuffer;
import org.apache.commons.pool2.impl.GenericObjectPool;

import java.math.BigDecimal;
import java.util.concurrent.ThreadLocalRandom;

public class BalanceEventProducer extends AbstractEventProducer<Balance> {

    public BalanceEventProducer(RingBuffer<PooledEvent> ringBuffer, GenericObjectPool<Balance> pool) {
        super(ringBuffer, pool);
    }

    @Override
    public void publishEvent() {
        super.publishEvent();
    }

    @Override
    protected void populatePayload(Balance payload) {
        ThreadLocalRandom random = ThreadLocalRandom.current();
        payload.setAccountId(random.nextInt(1, 1_000_000));
        payload.setAmount(BigDecimal.valueOf(random.nextDouble(0, 1_000_000)));
        payload.setCurrency(random.nextBoolean() ? "USD" : "CNY");
    }

    @Override
    protected String payloadType() {
        return "balance";
    }
}
