package org.tianle.springbootmodule.disruptor;

import com.lmax.disruptor.dsl.Disruptor;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.PreDestroy;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Sets up a small Disruptor pipeline fed by a pooled User producer.
 */
@Service
public class DisruptorDemoService {

    private static final Logger LOGGER = LoggerFactory.getLogger(DisruptorDemoService.class);
    private static final int BUFFER_SIZE = 8192;

    private final GenericObjectPool<User> userPool;
    private final GenericObjectPool<Balance> balancePool;
    private final GenericObjectPool<Position> positionPool;
    private final Disruptor<PooledEvent> disruptor;
    private final UserEventProducer userEventProducer;
    private final BalanceEventProducer balanceEventProducer;
    private final PositionEventProducer positionEventProducer;
    private final ExecutorService publisherExecutor;

    public DisruptorDemoService() {
        GenericObjectPoolConfig<User> config = new GenericObjectPoolConfig<>();
        config.setMaxTotal(BUFFER_SIZE);
        config.setMinIdle(8);
        config.setMaxIdle(BUFFER_SIZE / 2);
        this.userPool = new GenericObjectPool<>(new UserPooledObjectFactory(), config);

        GenericObjectPoolConfig<Balance> balanceConfig = new GenericObjectPoolConfig<>();
        balanceConfig.setMaxTotal(BUFFER_SIZE);
        balanceConfig.setMinIdle(8);
        balanceConfig.setMaxIdle(BUFFER_SIZE / 2);
        this.balancePool = new GenericObjectPool<>(new BalancePooledObjectFactory(), balanceConfig);

        GenericObjectPoolConfig<Position> positionConfig = new GenericObjectPoolConfig<>();
        positionConfig.setMaxTotal(BUFFER_SIZE);
        positionConfig.setMinIdle(8);
        positionConfig.setMaxIdle(BUFFER_SIZE / 2);
        this.positionPool = new GenericObjectPool<>(new PositionPooledObjectFactory(), positionConfig);

        AtomicInteger threadIndex = new AtomicInteger();
        ThreadFactory threadFactory = r -> {
            Thread t = new Thread(r, "disruptor-demo-" + threadIndex.incrementAndGet());
            t.setDaemon(true);
            return t;
        };

        this.disruptor = new Disruptor<>(new PooledEventFactory(), BUFFER_SIZE, threadFactory);
        this.disruptor.handleEventsWith(
                new LoggingEventHandler<>(userPool, User.class),
                new LoggingEventHandler<>(balancePool, Balance.class),
                new LoggingEventHandler<>(positionPool, Position.class)
        );
        this.disruptor.start();
        this.userEventProducer = new UserEventProducer(this.disruptor.getRingBuffer(), this.userPool);
        this.balanceEventProducer = new BalanceEventProducer(this.disruptor.getRingBuffer(), this.balancePool);
        this.positionEventProducer = new PositionEventProducer(this.disruptor.getRingBuffer(), this.positionPool);
        this.publisherExecutor = Executors.newSingleThreadExecutor(r -> {
            Thread t = new Thread(r, "disruptor-publisher");
            t.setDaemon(true);
            return t;
        });
    }

    @PreDestroy
    public void shutdown() {
        this.disruptor.shutdown();
        this.userPool.close();
        this.balancePool.close();
        this.positionPool.close();
        this.publisherExecutor.shutdownNow();
    }

    public int runDemo(int events) {
        if (events <= 0) {
            return 0;
        }

        int totalEvents = events * 3;
        publisherExecutor.submit(() -> publishEvents(events));
        return totalEvents;
    }

    private void publishEvents(int events) {
        for (int i = 0; i < events; i++) {
            userEventProducer.publishEvent();
            balanceEventProducer.publishEvent();
            positionEventProducer.publishEvent();
        }
        LOGGER.info("Disruptor demo completed, published {} events", events * 3);
    }
}
