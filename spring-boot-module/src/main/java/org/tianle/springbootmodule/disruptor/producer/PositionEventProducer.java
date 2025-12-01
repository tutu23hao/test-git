package org.tianle.springbootmodule.disruptor.producer;

import com.lmax.disruptor.RingBuffer;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.tianle.springbootmodule.disruptor.core.AbstractEventProducer;
import org.tianle.springbootmodule.disruptor.core.PooledEvent;
import org.tianle.springbootmodule.disruptor.model.Position;

import java.util.concurrent.ThreadLocalRandom;

@Component
public class PositionEventProducer extends AbstractEventProducer<Position> {

    private static final String[] SYMBOLS = {"AAPL", "TSLA", "BABA", "MSFT"};

    public PositionEventProducer(RingBuffer<PooledEvent> ringBuffer,
                                 @Qualifier("positionPool") GenericObjectPool<Position> pool) {
        super(ringBuffer, pool);
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
