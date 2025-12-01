package org.tianle.springbootmodule.disruptor;

import com.lmax.disruptor.EventHandler;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Consumes User events and logs their content before returning them to the pool.
 */
public class UserEventHandler implements EventHandler<UserEvent> {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserEventHandler.class);

    private final GenericObjectPool<User> userPool;

    public UserEventHandler(GenericObjectPool<User> userPool) {
        this.userPool = userPool;
    }

    @Override
    public void onEvent(UserEvent event, long sequence, boolean endOfBatch) throws Exception {
        User user = event.getUser();
        if (user != null) {
            LOGGER.info("Consumed user event: {}", user);
            userPool.returnObject(user);
            event.clear();
        }
    }
}
