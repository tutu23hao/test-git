package org.tianle.springbootmodule.disruptor;

import org.apache.commons.pool2.BasePooledObjectFactory;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.DefaultPooledObject;

/**
 * Factory for commons-pool that manages User instances.
 */
public class UserPooledObjectFactory extends BasePooledObjectFactory<User> {

    @Override
    public User create() {
        return new User();
    }

    @Override
    public PooledObject<User> wrap(User obj) {
        return new DefaultPooledObject<>(obj);
    }

    @Override
    public void passivateObject(PooledObject<User> pooledObject) {
        pooledObject.getObject().reset();
    }
}
