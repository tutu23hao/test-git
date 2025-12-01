package org.tianle.springbootmodule.disruptor;

import com.lmax.disruptor.RingBuffer;
import org.apache.commons.pool2.impl.GenericObjectPool;

import java.time.Instant;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Publishes pooled User instances into the Disruptor ring buffer.
 */
public class UserEventProducer extends AbstractEventProducer<User> {

    private final AtomicLong sequence = new AtomicLong();

    public UserEventProducer(RingBuffer<PooledEvent> ringBuffer, GenericObjectPool<User> userPool) {
        super(ringBuffer, userPool);
    }

    @Override
    public void publishEvent() {
        super.publishEvent();
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
