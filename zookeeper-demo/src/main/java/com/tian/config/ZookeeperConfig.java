package com.tian.config;

import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

@Configuration
public class ZookeeperConfig {

    @Value("${zookeeper.connect-string}")
    private String connectString;

    @Bean
    public ZooKeeper zooKeeper() throws IOException {
        return new ZooKeeper(connectString, 3000, watchedEvent -> {
            if (watchedEvent.getState() == Watcher.Event.KeeperState.SyncConnected) {
                System.out.println("Zookeeper connected successfully!");
            }
        });
    }
}