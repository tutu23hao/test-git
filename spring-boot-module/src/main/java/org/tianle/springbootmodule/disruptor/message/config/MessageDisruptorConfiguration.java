package org.tianle.springbootmodule.disruptor.message.config;

import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.dsl.Disruptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.tianle.springbootmodule.disruptor.message.core.MediaMessageEventFactory;
import org.tianle.springbootmodule.disruptor.message.core.MediaMessageEventHandler;
import org.tianle.springbootmodule.disruptor.message.model.MediaMessageEvent;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

@Configuration
public class MessageDisruptorConfiguration {

    private static final Logger LOGGER = LoggerFactory.getLogger(MessageDisruptorConfiguration.class);
    private static final int BUFFER_SIZE = 4096;

    @Bean(name = "messageDisruptor", destroyMethod = "shutdown")
    public Disruptor<MediaMessageEvent> messageDisruptor(MediaMessageEventHandler mediaMessageEventHandler) {
        LOGGER.info("Creating message disruptor, bufferSize={}, messageTypes=[AD, NEWS]", BUFFER_SIZE);
        Disruptor<MediaMessageEvent> disruptor = new Disruptor<>(
                new MediaMessageEventFactory(),
                BUFFER_SIZE,
                messageDisruptorThreadFactory());

        LOGGER.info("Registering message disruptor event handler");
        disruptor.handleEventsWith(mediaMessageEventHandler);
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
