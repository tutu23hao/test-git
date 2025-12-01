package org.tian.example;

/**
 * @author ：tianLe
 */

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.SortedMap;
import java.util.TreeMap;


class ConsistentHashing {
    private final int replicas;
    private final SortedMap<Integer, String> ring = new TreeMap<>();


    public ConsistentHashing(int replicas) {
        this.replicas = replicas;
    }

    public static void main(String[] args) {
        ConsistentHashing ch = new ConsistentHashing(3);
        ch.addNode("node1");
        ch.addNode("node2");
        ch.addNode("node3");
        System.out.println(ch.getNode("data1"));
        System.out.println(ch.getNode("data2"));
        ch.addNode("node4");
        System.out.println(ch.getNode("data1"));
        System.out.println(ch.getNode("data2"));
        ch.removeNode("node2");
        System.out.println(ch.getNode("data1"));
        System.out.println(ch.getNode("data2"));
    }

    public void addNode(String node) {
        try {
            for (int i = 0; i < replicas; i++) {
                String virtualNode = node + "#" + i;
                int hash = hash(virtualNode);
                ring.put(hash, node);
            }
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    public void removeNode(String node) {
        try {
            for (int i = 0; i < replicas; i++) {
                String virtualNode = node + "#" + i;
                int hash = hash(virtualNode);
                ring.remove(hash);
            }
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    public String getNode(String key) {
        if (ring.isEmpty()) {
            return null;
        }
        try {
            int hash = hash(key);
            if (!ring.containsKey(hash)) {
                SortedMap<Integer, String> tailMap = ring.tailMap(hash);
                hash = tailMap.isEmpty() ? ring.firstKey() : tailMap.firstKey();
            }
            return ring.get(hash);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }

    private int hash(String key) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("MD5");
        byte[] digest = md.digest(key.getBytes());
        int hash = 0;
        for (byte b : digest) {
            hash = ((hash << 8) | (b & 0xFF));
        }
        return hash;
    }
}
