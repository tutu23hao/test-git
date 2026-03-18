package org.tianle.springbootmodule.disruptor.message.service;

import com.lmax.disruptor.dsl.Disruptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.tianle.springbootmodule.disruptor.message.model.MediaMessageEvent;
import org.tianle.springbootmodule.disruptor.message.producer.MediaMessageProducer;

import javax.annotation.PreDestroy;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@Service
public class MessageDisruptorService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MessageDisruptorService.class);

    private final MediaMessageProducer mediaMessageProducer;
    @SuppressWarnings("unused")
    private final Disruptor<MediaMessageEvent> messageDisruptor;
    private final ExecutorService singlePublisherExecutor;

    public MessageDisruptorService(MediaMessageProducer mediaMessageProducer,
                                   @Qualifier("messageDisruptor") Disruptor<MediaMessageEvent> messageDisruptor) {
        this.mediaMessageProducer = mediaMessageProducer;
        this.messageDisruptor = messageDisruptor;
        this.singlePublisherExecutor = Executors.newSingleThreadExecutor(runnable -> {
            Thread thread = new Thread(runnable, "message-disruptor-publisher");
            thread.setDaemon(true);
            return thread;
        });
    }

    public int runDemo(int events) {
        if (events <= 0) {
            LOGGER.warn("Skip message disruptor publishing because events={} is not positive", events);
            return 0;
        }

        LOGGER.info("Submit message publishing task to single producer, loops={}, messageTypes=[ad,news]", events);
        singlePublisherExecutor.submit(() -> publishMessages(events));
        return events * 2;
    }

    private void publishMessages(int events) {
        for (int i = 0; i < events; i++) {
            mediaMessageProducer.publishAdMessage();
            mediaMessageProducer.publishNewsMessage();
        }
        LOGGER.info("Message disruptor publishing completed, published {} events", events * 2);
    }

    @PreDestroy
    public void shutdownPublisherExecutor() {
        singlePublisherExecutor.shutdown();
        try {
            if (!singlePublisherExecutor.awaitTermination(3, TimeUnit.SECONDS)) {
                LOGGER.warn("Single producer executor did not terminate in time, forcing shutdown");
                singlePublisherExecutor.shutdownNow();
            }
        } catch (InterruptedException interruptedException) {
            Thread.currentThread().interrupt();
            LOGGER.warn("Interrupted while waiting single producer executor to stop");
            singlePublisherExecutor.shutdownNow();
        }
    }
}
