package org.tianle.springbootmodule.disruptor.message.producer;

import com.lmax.disruptor.RingBuffer;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.tianle.springbootmodule.disruptor.message.model.MediaMessageEvent;
import org.tianle.springbootmodule.disruptor.message.model.MessageType;

import java.time.Instant;
import java.util.concurrent.atomic.AtomicLong;

@Component
public class MediaMessageProducer {

    private static final String[] AD_SOURCES = {"尼康", "苹果", "可口可乐", "特斯拉"};
    private static final String[] AD_CONTENTS = {
            "春季售卖开始",
            "新产品发布",
            "打折促销",
            "加入我们的会员"
    };
    private static final String[] NEWS_SOURCES = {"财经", "体育", "科技", "世界"};
    private static final String[] NEWS_CONTENTS = {
            "Market closes with mixed signals",
            "Championship game enters final stage",
            "New AI platform released by startup",
            "Global summit discusses climate policy"
    };

    private final RingBuffer<MediaMessageEvent> ringBuffer;
    private final AtomicLong adSequence = new AtomicLong();
    private final AtomicLong newsSequence = new AtomicLong();

    public MediaMessageProducer(@Qualifier("messageRingBuffer") RingBuffer<MediaMessageEvent> ringBuffer) {
        this.ringBuffer = ringBuffer;
    }

    public void publishAdMessage() {
        long id = adSequence.incrementAndGet();
        int index = (int) ((id - 1) % AD_SOURCES.length);
        publish(MessageType.AD, id, AD_SOURCES[index], AD_CONTENTS[index]);
    }

    public void publishNewsMessage() {
        long id = newsSequence.incrementAndGet();
        int index = (int) ((id - 1) % NEWS_SOURCES.length);
        publish(MessageType.NEWS, id, NEWS_SOURCES[index], NEWS_CONTENTS[index]);
    }

    private void publish(MessageType type, long id, String source, String content) {
        long sequence = ringBuffer.next();
        try {
            MediaMessageEvent event = ringBuffer.get(sequence);
            event.setType(type);
            event.setMessageId(id);
            event.setSource(source);
            event.setContent(content);
            event.setPublishTime(Instant.now());
        } finally {
            ringBuffer.publish(sequence);
        }
    }
}
