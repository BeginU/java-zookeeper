package com.example.zookeeper.demo.distributeLock;

import org.I0Itec.zkclient.IZkChildListener;
import org.I0Itec.zkclient.IZkDataListener;
import org.I0Itec.zkclient.ZkClient;

import java.util.concurrent.CountDownLatch;

/**
 * 分布式锁
 * 实现分布式锁的分案有多个
 * 1、可以使用数据库实现 ；
 * 缺点：性能不太好，易死锁
 * 2、可以使用redis实现分布式锁
 * 缺点：锁释放难以控制，易死锁
 * 3、zookeeper分布式锁
 * 优点：好！
 * @author lumingming
 * @version 1.0
 * @create 2018-11-05 11:36
 **/
public class DistributeLock extends ZookeeperAbstractLock{

    @Override
    Boolean tryLock() {
        try {
            zkClient.createEphemeral(PATH);
            return true;
        } catch (Exception e) {
            return false;
        }

    }

    @Override
    void waitLock() {

        // 使用事件监听，获取到节点被删除，
        IZkDataListener iZkDataListener = new IZkDataListener() {
            // 当节点被删除
            @Override
            public void handleDataDeleted(String dataPath) throws Exception {
                if (countDownLatch != null) {
                    // 唤醒
                    countDownLatch.countDown();
                }

            }

            // 当节点发生改变
            @Override
            public void handleDataChange(String dataPath, Object data) throws Exception {

            }
        };

        // 注册节点信息
        zkClient.subscribeDataChanges(PATH, iZkDataListener);
        if (zkClient.exists(PATH)) {
            // 创建信号量
            countDownLatch = new CountDownLatch(1);
            try {
                // 等待
                countDownLatch.await();
            } catch (Exception e) {

            }

        }
        // 删除事件通知
        zkClient.unsubscribeDataChanges(PATH, iZkDataListener);
    }

}
