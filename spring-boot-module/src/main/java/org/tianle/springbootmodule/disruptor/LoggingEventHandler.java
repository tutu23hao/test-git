package org.tianle.springbootmodule.disruptor;

import org.apache.commons.pool2.impl.GenericObjectPool;

public class LoggingEventHandler<T extends PooledPayload> extends AbstractEventHandler<T> {

    public LoggingEventHandler(GenericObjectPool<T> pool, Class<T> payloadType) {
        super(pool, payloadType);
    }

    @Override
    protected void handleEvent(T payload) {
        try {
            Thread.sleep(1);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        // No additional processing; logging happens in the abstract base class.
    }
}
