package org.tianle.springbootmodule.disruptor.message.core;

import com.lmax.disruptor.EventHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.tianle.springbootmodule.disruptor.message.model.MediaMessageEvent;
import org.tianle.springbootmodule.disruptor.message.model.MessageType;

@Component
public class NewsMessageEventHandler implements EventHandler<MediaMessageEvent> {

    private static final Logger LOGGER = LoggerFactory.getLogger(NewsMessageEventHandler.class);

    @Override
    public void onEvent(MediaMessageEvent event, long sequence, boolean endOfBatch) {
        if (event.getType() != MessageType.NEWS) {
            return;
        }

        // News-specific handling: only news events are processed here.
        LOGGER.info("==>>新闻 handler processed event, sequence={}, newsId={}, category={}, title={}, publishTime={}",
                sequence,
                event.getMessageId(),
                event.getSource(),
                event.getContent(),
                event.getPublishTime());
    }
}
