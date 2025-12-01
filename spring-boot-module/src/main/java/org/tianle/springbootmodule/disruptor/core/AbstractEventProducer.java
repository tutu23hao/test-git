package org.tianle.springbootmodule.disruptor.core;

import com.lmax.disruptor.RingBuffer;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Base producer that borrows payload objects from a pool and publishes them to the ring buffer.
 */
public abstract class AbstractEventProducer<T extends PooledPayload> {

    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractEventProducer.class);

    private final RingBuffer<PooledEvent> ringBuffer;
    private final GenericObjectPool<T> pool;

    protected AbstractEventProducer(RingBuffer<PooledEvent> ringBuffer, GenericObjectPool<T> pool) {
        this.ringBuffer = ringBuffer;
        this.pool = pool;
    }

    public void publishEvent() {
        T payload = null;
        try {
            payload = pool.borrowObject();
            populatePayload(payload);

            long seq = ringBuffer.next();
            try {
                PooledEvent event = ringBuffer.get(seq);
                event.setPayload(payload);
            } finally {
                ringBuffer.publish(seq);
            }
        } catch (Exception ex) {
            LOGGER.error("Failed to publish {} event", payloadType(), ex);
            if (payload != null) {
                pool.returnObject(payload);
            }
        }
    }

    protected abstract void populatePayload(T payload);

    protected abstract String payloadType();
}
