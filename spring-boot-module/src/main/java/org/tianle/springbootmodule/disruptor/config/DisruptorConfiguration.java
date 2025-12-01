package org.tianle.springbootmodule.disruptor.config;

import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.dsl.Disruptor;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.tianle.springbootmodule.disruptor.core.LoggingEventHandler;
import org.tianle.springbootmodule.disruptor.core.PooledEvent;
import org.tianle.springbootmodule.disruptor.core.PooledEventFactory;
import org.tianle.springbootmodule.disruptor.model.Balance;
import org.tianle.springbootmodule.disruptor.model.Position;
import org.tianle.springbootmodule.disruptor.model.User;
import org.tianle.springbootmodule.disruptor.pool.BalancePooledObjectFactory;
import org.tianle.springbootmodule.disruptor.pool.PositionPooledObjectFactory;
import org.tianle.springbootmodule.disruptor.pool.UserPooledObjectFactory;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

@Configuration
public class DisruptorConfiguration {

    public static final int BUFFER_SIZE = 8192;

    @Bean(name = "userPool", destroyMethod = "close")
    public GenericObjectPool<User> userPool() {
        return new GenericObjectPool<>(new UserPooledObjectFactory(), basePoolConfig());
    }

    @Bean(name = "balancePool", destroyMethod = "close")
    public GenericObjectPool<Balance> balancePool() {
        return new GenericObjectPool<>(new BalancePooledObjectFactory(), basePoolConfig());
    }

    @Bean(name = "positionPool", destroyMethod = "close")
    public GenericObjectPool<Position> positionPool() {
        return new GenericObjectPool<>(new PositionPooledObjectFactory(), basePoolConfig());
    }

    @Bean
    public LoggingEventHandler<User> userLoggingEventHandler(@Qualifier("userPool") GenericObjectPool<User> userPool) {
        return new LoggingEventHandler<>(userPool, User.class);
    }

    @Bean
    public LoggingEventHandler<Balance> balanceLoggingEventHandler(@Qualifier("balancePool") GenericObjectPool<Balance> balancePool) {
        return new LoggingEventHandler<>(balancePool, Balance.class);
    }

    @Bean
    public LoggingEventHandler<Position> positionLoggingEventHandler(@Qualifier("positionPool") GenericObjectPool<Position> positionPool) {
        return new LoggingEventHandler<>(positionPool, Position.class);
    }

    @Bean(destroyMethod = "shutdown")
    public Disruptor<PooledEvent> disruptor(
            LoggingEventHandler<User> userLoggingEventHandler,
            LoggingEventHandler<Balance> balanceLoggingEventHandler,
            LoggingEventHandler<Position> positionLoggingEventHandler) {
        Disruptor<PooledEvent> disruptor = new Disruptor<>(new PooledEventFactory(), BUFFER_SIZE, disruptorThreadFactory());
        disruptor.handleEventsWith(userLoggingEventHandler, balanceLoggingEventHandler, positionLoggingEventHandler);
        disruptor.start();
        return disruptor;
    }

    @Bean
    public RingBuffer<PooledEvent> ringBuffer(Disruptor<PooledEvent> disruptor) {
        return disruptor.getRingBuffer();
    }

    private <T> GenericObjectPoolConfig<T> basePoolConfig() {
        GenericObjectPoolConfig<T> config = new GenericObjectPoolConfig<>();
        config.setMaxTotal(BUFFER_SIZE);
        config.setMinIdle(8);
        config.setMaxIdle(BUFFER_SIZE / 2);
        config.setJmxEnabled(false);
        return config;
    }

    private ThreadFactory disruptorThreadFactory() {
        AtomicInteger counter = new AtomicInteger();
        return runnable -> {
            Thread thread = new Thread(runnable, "disruptor-demo-" + counter.incrementAndGet());
            thread.setDaemon(true);
            return thread;
        };
    }
}
