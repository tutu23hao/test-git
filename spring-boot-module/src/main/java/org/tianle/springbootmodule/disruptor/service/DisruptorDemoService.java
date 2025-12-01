package org.tianle.springbootmodule.disruptor.service;

import com.lmax.disruptor.dsl.Disruptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.tianle.springbootmodule.disruptor.core.PooledEvent;
import org.tianle.springbootmodule.disruptor.producer.BalanceEventProducer;
import org.tianle.springbootmodule.disruptor.producer.PositionEventProducer;
import org.tianle.springbootmodule.disruptor.producer.UserEventProducer;

/**
 * Service facade that coordinates producer calls for the demo REST endpoint.
 */
@Service
public class DisruptorDemoService {

    private static final Logger LOGGER = LoggerFactory.getLogger(DisruptorDemoService.class);

    private final UserEventProducer userEventProducer;
    private final BalanceEventProducer balanceEventProducer;
    private final PositionEventProducer positionEventProducer;
    @SuppressWarnings("unused")
    private final Disruptor<PooledEvent> disruptor; // keep reference so Spring manages lifecycle

    public DisruptorDemoService(UserEventProducer userEventProducer,
                                BalanceEventProducer balanceEventProducer,
                                PositionEventProducer positionEventProducer,
                                Disruptor<PooledEvent> disruptor) {
        this.userEventProducer = userEventProducer;
        this.balanceEventProducer = balanceEventProducer;
        this.positionEventProducer = positionEventProducer;
        this.disruptor = disruptor;
    }

    public int runDemo(int events) {
        if (events <= 0) {
            return 0;
        }

        Thread publisher = new Thread(() -> publishEvents(events), "disruptor-publisher");
        publisher.setDaemon(true);
        publisher.start();
        return events * 3;
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
