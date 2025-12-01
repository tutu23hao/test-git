package com.tian.service;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.apache.curator.framework.recipes.locks.RevocationListener;
import org.apache.curator.retry.ExponentialBackoffRetry;

import java.util.concurrent.TimeUnit;

public class DistributedLockExample {
    private static final String LOCK_PATH = "/locks";

    public static void main(String[] args) {
        CuratorFramework client = CuratorFrameworkFactory.newClient("10.10.10.91:2181,10.10.10.92:2181,10.10.10.93:2181",
                new ExponentialBackoffRetry(1000, 3));
        client.start();

        InterProcessMutex lock = new InterProcessMutex(client, LOCK_PATH);
        lock.makeRevocable(new RevocationListener<InterProcessMutex>() {
            @Override
            public void revocationRequested(InterProcessMutex forLock) {

                System.out.println("Lock revoked, cleaning up resources...");
            }
        });
        try {
            if (lock.acquire(10, TimeUnit.MINUTES)) {
                System.out.println("Lock acquired, executing business logic...");

                Thread.sleep(5000000000000000L); // 模拟业务逻辑
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                lock.release();
                System.out.println("Lock released");
            } catch (Exception e) {
                e.printStackTrace();
            }
            client.close();
        }
    }
}