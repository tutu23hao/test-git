package org.tianle.springbootmodule.disruptor;

/**
 * Event that wraps a pooled User instance.
 */
public class UserEvent {

    private User user;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void clear() {
        this.user = null;
    }
}
