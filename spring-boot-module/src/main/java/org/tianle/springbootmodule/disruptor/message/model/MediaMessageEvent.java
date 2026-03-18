package org.tianle.springbootmodule.disruptor.message.model;

import lombok.Data;

import java.time.Instant;

@Data
public class MediaMessageEvent {

    private MessageType type;
    private long messageId;
    private String source;
    private String content;
    private Instant publishTime;

}
