package com.tian.controller;

import com.tian.service.ZookeeperService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/zookeeper")
public class ZookeeperController {

    @Autowired
    private ZookeeperService zookeeperService;

    // 创建节点
    @PostMapping("/create")
    public String createNode(@RequestParam String path, @RequestParam String data) throws Exception {
        zookeeperService.createNode(path, data);
        return "Node created successfully";
    }

    // 获取节点数据
    @GetMapping("/get")
    public String getNodeData(@RequestParam String path) throws Exception {
        return zookeeperService.getNodeData(path);
    }

    // 更新节点数据
    @PutMapping("/update")
    public String updateNodeData(@RequestParam String path, @RequestParam String data) throws Exception {
        zookeeperService.updateNodeData(path, data);
        return "Node updated successfully";
    }

    // 删除节点
    @DeleteMapping("/delete")
    public String deleteNode(@RequestParam String path) throws Exception {
        zookeeperService.deleteNode(path);
        return "Node deleted successfully";
    }

    // 检查节点是否存在
    @GetMapping("/exists")
    public boolean checkNodeExists(@RequestParam String path) throws Exception {
        return zookeeperService.checkNodeExists(path);
    }
}