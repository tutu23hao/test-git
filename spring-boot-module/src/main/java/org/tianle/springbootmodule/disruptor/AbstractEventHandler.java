package org.tianle.springbootmodule.disruptor;

import com.lmax.disruptor.EventHandler;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Generic handler that logs payload data and returns it to the matching pool.
 */
public abstract class AbstractEventHandler<T extends PooledPayload> implements EventHandler<PooledEvent> {

    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractEventHandler.class);

    private final GenericObjectPool<T> pool;
    private final Class<T> payloadType;

    protected AbstractEventHandler(GenericObjectPool<T> pool, Class<T> payloadType) {
        this.pool = pool;
        this.payloadType = payloadType;
    }

    @Override
    public void onEvent(PooledEvent event, long sequence, boolean endOfBatch) throws Exception {
        PooledPayload payload = event.getPayload();
        if (payloadType.isInstance(payload)) {
            @SuppressWarnings("unchecked")
            T typedPayload = (T) payload;
//            LOGGER.info("Consumed {} event: {}", payloadType().getSimpleName(), typedPayload.describe());
            handleEvent(typedPayload);
            pool.returnObject(typedPayload);
            event.clear();
        }
    }

    protected abstract void handleEvent(T payload);

    protected Class<T> payloadType() {
        return payloadType;
    }
}
