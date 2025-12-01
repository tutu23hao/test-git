package org.tianle.springbootmodule.disruptor.pool;

import org.apache.commons.pool2.BasePooledObjectFactory;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.DefaultPooledObject;
import org.tianle.springbootmodule.disruptor.model.Balance;

public class BalancePooledObjectFactory extends BasePooledObjectFactory<Balance> {
    @Override
    public Balance create() {
        return new Balance();
    }

    @Override
    public PooledObject<Balance> wrap(Balance obj) {
        return new DefaultPooledObject<>(obj);
    }

    @Override
    public void passivateObject(PooledObject<Balance> pooledObject) {
        pooledObject.getObject().reset();
    }
}
