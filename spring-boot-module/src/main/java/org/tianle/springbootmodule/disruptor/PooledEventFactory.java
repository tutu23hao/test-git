package org.tianle.springbootmodule.disruptor;

import com.lmax.disruptor.EventFactory;

public class PooledEventFactory implements EventFactory<PooledEvent> {
    @Override
    public PooledEvent newInstance() {
        return new PooledEvent();
    }
}
