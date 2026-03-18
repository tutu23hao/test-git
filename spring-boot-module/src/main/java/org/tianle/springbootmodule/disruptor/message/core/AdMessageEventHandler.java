package org.tianle.springbootmodule.disruptor.message.core;

import com.lmax.disruptor.EventHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.tianle.springbootmodule.disruptor.message.model.MediaMessageEvent;
import org.tianle.springbootmodule.disruptor.message.model.MessageType;

@Component
@Slf4j
public class AdMessageEventHandler implements EventHandler<MediaMessageEvent> {


    @Override
    public void onEvent(MediaMessageEvent event, long sequence, boolean endOfBatch) {
        if (event.getType() != MessageType.AD) {
            log.info(" ===>>> 不属于类型消息 event：{}", event.getType());
            return;
        }

        // Ad-specific handling: only advertisement events are processed here.
        log.info("广告 handler processed event, sequence={}, adId={}, advertiser={}, content={}, publishTime={}",
                sequence,
                event.getMessageId(),
                event.getSource(),
                event.getContent(),
                event.getPublishTime());
    }
}
