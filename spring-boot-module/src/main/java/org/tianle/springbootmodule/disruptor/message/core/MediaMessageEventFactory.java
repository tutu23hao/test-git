package org.tianle.springbootmodule.disruptor.message.core;

import com.lmax.disruptor.EventFactory;
import org.tianle.springbootmodule.disruptor.message.model.MediaMessageEvent;

public class MediaMessageEventFactory implements EventFactory<MediaMessageEvent> {

    @Override
    public MediaMessageEvent newInstance() {
        return new MediaMessageEvent();
    }
}
