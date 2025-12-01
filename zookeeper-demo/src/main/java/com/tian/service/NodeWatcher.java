package com.tian.service;

import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class NodeWatcher implements Watcher {
    @Autowired
    private ZooKeeper zooKeeper;

    @Override
    public void process(WatchedEvent event) {
        System.out.println("Watch event received: " + event);

        String path = event.getPath();
        switch (event.getType()) {
            case NodeCreated:
                System.out.println("Node created: " + path);
                break;
            case NodeDeleted:
                System.out.println("Node deleted: " + path);
                break;
            case NodeDataChanged:
                System.out.println("Node data changed: " + path);
                break;
            default:
                System.out.println("Other event: " + event.getType());
        }

        try {
            zooKeeper.getData("/test", this, null);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (KeeperException e) {
            e.printStackTrace();
        }

    }
}