package org.tianle.springbootmodule.disruptor;

import com.lmax.disruptor.EventFactory;

/**
 * Factory used by Disruptor to preallocate event entries.
 */
public class UserEventFactory implements EventFactory<UserEvent> {
    @Override
    public UserEvent newInstance() {
        return new UserEvent();
    }
}
