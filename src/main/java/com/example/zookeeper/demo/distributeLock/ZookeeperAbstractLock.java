package com.example.zookeeper.demo.distributeLock;

import org.I0Itec.zkclient.ZkClient;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;

import java.util.concurrent.CountDownLatch;

/**
 * 抽象类，用于实现lock接口
 *
 * @author lumingming
 * @version 1.0
 * @create 2018-11-05 14:04
 **/
public abstract class ZookeeperAbstractLock implements Lock{
    // zk连接地址
    private static final String CONNECTSTRING = "127.0.0.1:2181";
    // 创建zk连接
    protected ZkClient zkClient = new ZkClient(CONNECTSTRING);
    protected static final String PATH = "/lock";
    protected CountDownLatch countDownLatch = null;

    @Override
    public void getLock() {
        if (tryLock()) {
            System.out.println("###获取锁成功#####");
        } else {
            // 等待
            waitLock();
            // 重新获取锁
            getLock();
        }
    }

    // 是否获取锁成功,成功返回true 失败返回fasle
    abstract Boolean tryLock();

    // 等待
    abstract void waitLock();

    @Override
    public void unLock() {
        if (zkClient != null) {
            zkClient.close();
            System.out.println("释放锁资源");
        }
    }

}
