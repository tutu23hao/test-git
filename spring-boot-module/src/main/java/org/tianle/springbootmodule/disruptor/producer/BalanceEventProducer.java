package org.tianle.springbootmodule.disruptor.producer;

import com.lmax.disruptor.RingBuffer;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.tianle.springbootmodule.disruptor.core.AbstractEventProducer;
import org.tianle.springbootmodule.disruptor.core.PooledEvent;
import org.tianle.springbootmodule.disruptor.model.Balance;

import java.math.BigDecimal;
import java.util.concurrent.ThreadLocalRandom;

@Component
public class BalanceEventProducer extends AbstractEventProducer<Balance> {

    public BalanceEventProducer(RingBuffer<PooledEvent> ringBuffer,
                                @Qualifier("balancePool") GenericObjectPool<Balance> pool) {
        super(ringBuffer, pool);
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
