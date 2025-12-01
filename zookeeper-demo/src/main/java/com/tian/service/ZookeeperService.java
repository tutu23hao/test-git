package com.tian.service;

import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Service
public class ZookeeperService {

    @Autowired
    private ZooKeeper zooKeeper;
    @Autowired
    private NodeWatcher nodeWatcher;

    // 创建节点
    public void createNode(String path, String data) throws Exception {
        zooKeeper.create(path, data.getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
    }

    // 获取节点数据
    public String getNodeData(String path) throws Exception {
        byte[] data = zooKeeper.getData(path, false, null);
        return new String(data);
    }

    // 更新节点数据
    public void updateNodeData(String path, String data) throws Exception {
        zooKeeper.setData(path, data.getBytes(), -1);
    }

    // 删除节点
    public void deleteNode(String path) throws Exception {
        zooKeeper.delete(path, -1);
    }

    // 检查节点是否存在
    public boolean checkNodeExists(String path) throws Exception {
        Stat stat = zooKeeper.exists(path, false);
        return stat != null;
    }

    public void watchNode(String path, Watcher watcher) throws Exception {
        zooKeeper.exists(path, watcher); // 监听节点是否存在
        zooKeeper.getData(path, watcher, null); // 监听节点数据变化
    }

    @PostConstruct
    public void initWatchNode() throws InterruptedException, KeeperException {
        zooKeeper.exists("/test", nodeWatcher); // 监听节点是否存在
        zooKeeper.getData("/test", nodeWatcher, null); // 监听节点数据变化
    }

}