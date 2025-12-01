package org.tianle.springbootmodule.disruptor.model;

import org.tianle.springbootmodule.disruptor.core.PooledPayload;

import java.time.Instant;

/**
 * Simple User value object reused through an object pool.
 */
public class User implements PooledPayload {

    private long id;
    private String name;
    private Instant createdAt;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public void reset() {
        this.id = 0;
        this.name = null;
        this.createdAt = null;
    }

    @Override
    public String describe() {
        return toString();
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", createdAt=" + createdAt +
                '}';
    }
}
