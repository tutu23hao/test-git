package org.tianle.springbootmodule.disruptor.core;

import org.apache.commons.pool2.impl.GenericObjectPool;

public class LoggingEventHandler<T extends PooledPayload> extends AbstractEventHandler<T> {

    public LoggingEventHandler(GenericObjectPool<T> pool, Class<T> payloadType) {
        super(pool, payloadType);
    }

    @Override
    protected void handleEvent(T payload) {
        // No additional processing; logging happens in the abstract base class.
    }
}
