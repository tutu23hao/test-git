package org.tianle.springbootmodule.disruptor;

import com.lmax.disruptor.RingBuffer;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Instant;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Publishes pooled User instances into the Disruptor ring buffer.
 */
public class UserEventProducer {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserEventProducer.class);

    private final RingBuffer<UserEvent> ringBuffer;
    private final GenericObjectPool<User> userPool;
    private final AtomicLong sequence = new AtomicLong();

    public UserEventProducer(RingBuffer<UserEvent> ringBuffer, GenericObjectPool<User> userPool) {
        this.ringBuffer = ringBuffer;
        this.userPool = userPool;
    }

    public void publishEvent() {
        User user = null;
        try {
            user = userPool.borrowObject();
            long id = sequence.incrementAndGet();
            user.setId(id);
            user.setName("pooled-user-" + id);
            user.setCreatedAt(Instant.now());

            long seq = ringBuffer.next();
            try {
                UserEvent event = ringBuffer.get(seq);
                event.setUser(user);
            } finally {
                ringBuffer.publish(seq);
            }
        } catch (Exception ex) {
            LOGGER.error("Failed to publish user event", ex);
            if (user != null) {
                userPool.returnObject(user);
            }
        }
    }
}
