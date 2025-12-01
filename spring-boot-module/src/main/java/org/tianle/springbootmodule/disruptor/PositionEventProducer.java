package org.tianle.springbootmodule.disruptor;

import com.lmax.disruptor.RingBuffer;
import org.apache.commons.pool2.impl.GenericObjectPool;

import java.util.concurrent.ThreadLocalRandom;

public class PositionEventProducer extends AbstractEventProducer<Position> {

    private static final String[] SYMBOLS = {"AAPL", "TSLA", "BABA", "MSFT"};

    public PositionEventProducer(RingBuffer<PooledEvent> ringBuffer, GenericObjectPool<Position> pool) {
        super(ringBuffer, pool);
    }

    @Override
    public void publishEvent() {
        super.publishEvent();
    }

    @Override
    protected void populatePayload(Position payload) {
        ThreadLocalRandom random = ThreadLocalRandom.current();
        payload.setPortfolioId(random.nextInt(1, 10_000));
        payload.setSymbol(SYMBOLS[random.nextInt(SYMBOLS.length)]);
        payload.setQuantity(random.nextInt(1, 10_000));
    }

    @Override
    protected String payloadType() {
        return "position";
    }
}
