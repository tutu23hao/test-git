package org.tianle.springbootmodule.disruptor.message.model;

import java.time.Instant;

public class MediaMessageEvent {

    private MessageType type;
    private long messageId;
    private String source;
    private String content;
    private Instant publishTime;

    public MessageType getType() {
        return type;
    }

    public void setType(MessageType type) {
        this.type = type;
    }

    public long getMessageId() {
        return messageId;
    }

    public void setMessageId(long messageId) {
        this.messageId = messageId;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Instant getPublishTime() {
        return publishTime;
    }

    public void setPublishTime(Instant publishTime) {
        this.publishTime = publishTime;
    }

    public void clear() {
        type = null;
        messageId = 0;
        source = null;
        content = null;
        publishTime = null;
    }

    public String describe() {
        return "MediaMessageEvent{" +
                "type=" + type +
                ", messageId=" + messageId +
                ", source='" + source + '\'' +
                ", content='" + content + '\'' +
                ", publishTime=" + publishTime +
                '}';
    }
}
