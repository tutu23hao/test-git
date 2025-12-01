package org.tianle.springbootmodule.disruptor;

import org.apache.commons.pool2.BasePooledObjectFactory;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.DefaultPooledObject;

public class PositionPooledObjectFactory extends BasePooledObjectFactory<Position> {
    @Override
    public Position create() {
        return new Position();
    }

    @Override
    public PooledObject<Position> wrap(Position obj) {
        return new DefaultPooledObject<>(obj);
    }

    @Override
    public void passivateObject(PooledObject<Position> pooledObject) {
        pooledObject.getObject().reset();
    }
}
