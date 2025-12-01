package org.tianle.springbootmodule.disruptor.producer;

import com.lmax.disruptor.RingBuffer;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.tianle.springbootmodule.disruptor.core.AbstractEventProducer;
import org.tianle.springbootmodule.disruptor.core.PooledEvent;
import org.tianle.springbootmodule.disruptor.model.User;

import java.time.Instant;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Publishes pooled User instances into the Disruptor ring buffer.
 */
@Component
public class UserEventProducer extends AbstractEventProducer<User> {

    private final AtomicLong sequence = new AtomicLong();

    public UserEventProducer(RingBuffer<PooledEvent> ringBuffer,
                             @Qualifier("userPool") GenericObjectPool<User> userPool) {
        super(ringBuffer, userPool);
    }

    @Override
    protected void populatePayload(User payload) {
        long id = sequence.incrementAndGet();
        payload.setId(id);
        payload.setName("pooled-user-" + id);
        payload.setCreatedAt(Instant.now());
    }

    @Override
    protected String payloadType() {
        return "user";
    }
}
