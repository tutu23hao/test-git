package org.tianle.springbootmodule.disruptor.message.config;

import com.lmax.disruptor.BlockingWaitStrategy;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.tianle.springbootmodule.disruptor.message.core.AdMessageEventHandler;
import org.tianle.springbootmodule.disruptor.message.core.NewsMessageEventHandler;
import org.tianle.springbootmodule.disruptor.message.model.MediaMessageEvent;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

@Configuration
public class MessageDisruptorConfiguration {

    private static final Logger LOGGER = LoggerFactory.getLogger(MessageDisruptorConfiguration.class);
    private static final int BUFFER_SIZE = 4096;

    @Bean(name = "messageDisruptor", destroyMethod = "shutdown")
    public Disruptor<MediaMessageEvent> messageDisruptor(AdMessageEventHandler adMessageEventHandler,
                                                         NewsMessageEventHandler newsMessageEventHandler) {
        LOGGER.info("Creating message disruptor, bufferSize={}, messageTypes=[AD, NEWS]", BUFFER_SIZE);
        Disruptor<MediaMessageEvent> disruptor = new Disruptor<>(
                MediaMessageEvent::new,
                BUFFER_SIZE,
                messageDisruptorThreadFactory(),
                ProducerType.SINGLE,
                new BlockingWaitStrategy());

        LOGGER.info("Registering handlers: adMessageEventHandler and newsMessageEventHandler");
        disruptor.handleEventsWith(adMessageEventHandler, newsMessageEventHandler);
        disruptor.start();
        LOGGER.info("Message disruptor started");
        return disruptor;
    }

    @Bean(name = "messageRingBuffer")
    public RingBuffer<MediaMessageEvent> messageRingBuffer(
            @Qualifier("messageDisruptor") Disruptor<MediaMessageEvent> messageDisruptor) {
        return messageDisruptor.getRingBuffer();
    }

    private ThreadFactory messageDisruptorThreadFactory() {
        AtomicInteger counter = new AtomicInteger();
        return runnable -> {
            Thread thread = new Thread(runnable, "message-disruptor-" + counter.incrementAndGet());
            thread.setDaemon(true);
            return thread;
        };
    }
}
