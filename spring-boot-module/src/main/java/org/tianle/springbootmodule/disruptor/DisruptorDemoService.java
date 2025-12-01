package org.tianle.springbootmodule.disruptor;

import com.lmax.disruptor.dsl.Disruptor;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.PreDestroy;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Sets up a small Disruptor pipeline fed by a pooled User producer.
 */
@Service
public class DisruptorDemoService {

    private static final Logger LOGGER = LoggerFactory.getLogger(DisruptorDemoService.class);
    private static final int BUFFER_SIZE = 1024;

    private final GenericObjectPool<User> userPool;
    private final Disruptor<UserEvent> disruptor;
    private final UserEventProducer producer;

    public DisruptorDemoService() {
        GenericObjectPoolConfig<User> config = new GenericObjectPoolConfig<>();
        config.setMaxTotal(BUFFER_SIZE);
        config.setMinIdle(8);
        config.setMaxIdle(BUFFER_SIZE / 2);
        this.userPool = new GenericObjectPool<>(new UserPooledObjectFactory(), config);

        AtomicInteger threadIndex = new AtomicInteger();
        ThreadFactory threadFactory = r -> {
            Thread t = new Thread(r, "disruptor-demo-" + threadIndex.incrementAndGet());
            t.setDaemon(true);
            return t;
        };

        this.disruptor = new Disruptor<>(new UserEventFactory(), BUFFER_SIZE, threadFactory);
        this.disruptor.handleEventsWith(new UserEventHandler(userPool));
        this.disruptor.start();
        this.producer = new UserEventProducer(this.disruptor.getRingBuffer(), this.userPool);
    }

    @PreDestroy
    public void shutdown() {
        this.disruptor.shutdown();
        this.userPool.close();
    }

    public int runDemo(int events) {
        if (events <= 0) {
            return 0;
        }

        for (int i = 0; i < events; i++) {
            producer.publishEvent();
        }

        LOGGER.info("Disruptor demo completed, published {} events", events);
        return events;
    }
}
