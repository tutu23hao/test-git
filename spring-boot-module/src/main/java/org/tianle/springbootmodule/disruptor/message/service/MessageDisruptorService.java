package org.tianle.springbootmodule.disruptor.message.service;

import com.lmax.disruptor.dsl.Disruptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.tianle.springbootmodule.disruptor.message.model.MediaMessageEvent;
import org.tianle.springbootmodule.disruptor.message.producer.MediaMessageProducer;

@Service
public class MessageDisruptorService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MessageDisruptorService.class);

    private final MediaMessageProducer mediaMessageProducer;
    @SuppressWarnings("unused")
    private final Disruptor<MediaMessageEvent> messageDisruptor;

    public MessageDisruptorService(MediaMessageProducer mediaMessageProducer,
                                   @Qualifier("messageDisruptor") Disruptor<MediaMessageEvent> messageDisruptor) {
        this.mediaMessageProducer = mediaMessageProducer;
        this.messageDisruptor = messageDisruptor;
    }

    public int runDemo(int events) {
        if (events <= 0) {
            LOGGER.warn("Skip message disruptor publishing because events={} is not positive", events);
            return 0;
        }

        LOGGER.info("Start message disruptor publishing, loops={}, messageTypes=[ad,news]", events);
        Thread publisher = new Thread(() -> publishMessages(events), "message-disruptor-publisher");
        publisher.setDaemon(true);
        publisher.start();
        return events * 2;
    }

    private void publishMessages(int events) {
        for (int i = 0; i < events; i++) {
            mediaMessageProducer.publishAdMessage();
            mediaMessageProducer.publishNewsMessage();
        }
        LOGGER.info("Message disruptor publishing completed, published {} events", events * 2);
    }
}
