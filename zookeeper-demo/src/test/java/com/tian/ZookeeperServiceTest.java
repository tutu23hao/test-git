package com.tian;

import com.tian.service.NodeWatcher;
import com.tian.service.ZookeeperService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class ZookeeperServiceTest extends BaseApplicationTest {

    private static final String TEST_PATH = "/test";
    private static final String TEST_DATA = "HelloZookeeper";
    @Autowired
    private ZookeeperService zookeeperService;
    @Autowired
    private NodeWatcher nodeWatcher;

    @Test
    public void testCreateNode() throws Exception {


        // 监听节点变化
        zookeeperService.watchNode(TEST_PATH, nodeWatcher);
        System.in.read();


//        // 创建节点
//        zookeeperService.createNode(TEST_PATH, TEST_DATA);
//
//        // 验证节点是否存在
//        boolean exists = zookeeperService.checkNodeExists(TEST_PATH);
//        assertTrue(exists, "Node should exist after creation");
    }

    @Test
    public void testGetNodeData() throws Exception {
        // 获取节点数据
        String data = zookeeperService.getNodeData(TEST_PATH);

        // 验证数据是否正确
        assertEquals(TEST_DATA, data, "Node data should match the created data");
    }

    @Test
    public void testUpdateNodeData() throws Exception {
        String updatedData = "HelloWorld";

        // 更新节点数据
        zookeeperService.updateNodeData(TEST_PATH, updatedData);

        // 获取节点数据并验证
        String data = zookeeperService.getNodeData(TEST_PATH);
        assertEquals(updatedData, data, "Node data should be updated");
    }

    @Test
    public void testDeleteNode() throws Exception {
        // 删除节点
        zookeeperService.deleteNode(TEST_PATH);

        // 验证节点是否被删除
        boolean exists = zookeeperService.checkNodeExists(TEST_PATH);
        assertFalse(exists, "Node should not exist after deletion");
    }

    @Test
    public void testCheckNodeExists() throws Exception {
        // 检查不存在的节点
        boolean exists = zookeeperService.checkNodeExists("/nonExistentNode");
        assertFalse(exists, "Non-existent node should return false");
    }
}