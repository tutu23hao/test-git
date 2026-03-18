package org.tianle.springbootmodule.disruptor.message.core;

import com.lmax.disruptor.EventHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.tianle.springbootmodule.disruptor.message.model.MediaMessageEvent;

@Component
public class MediaMessageEventHandler implements EventHandler<MediaMessageEvent> {

    private static final Logger LOGGER = LoggerFactory.getLogger(MediaMessageEventHandler.class);

    @Override
    public void onEvent(MediaMessageEvent event, long sequence, boolean endOfBatch) {
        LOGGER.info("Consumed message event, sequence={}, endOfBatch={}, payload={}",
                sequence, endOfBatch, event.describe());
        event.clear();
    }
}
