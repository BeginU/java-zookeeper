package com.example.zookeeper.demo.node;

import org.apache.zookeeper.*;

import java.util.concurrent.CountDownLatch;

/**
 * 创建node
 *
 * @author lumingming
 * @version 1.0
 * @create 2018-11-01 14:25
 **/
public class CreateNode {

    private static final String ADDRESS = "192.168.144.133:2181";
    private static final int TIME_OUT  = 2000;
    private static final CountDownLatch countDownLatch = new CountDownLatch(1);

    public static void main(String arg[])throws Exception{
        ZooKeeper zooKeeper = new ZooKeeper(ADDRESS, TIME_OUT, new Watcher() {
            @Override
            public void process(WatchedEvent watchedEvent) {
                // 获取监听状态
                Event.KeeperState keeperState = watchedEvent.getState();
                // 获取监听事件
                Event.EventType eventType = watchedEvent.getType();
                if(keeperState == Event.KeeperState.SyncConnected && eventType == Event.EventType.None){
                    countDownLatch.countDown();
                    System.out.println("zk开始启动...");
                }
            }
        });
        // 进行阻塞
        countDownLatch.await();
        //创建持久节点，允许任何服务器可以访问
        String path = zooKeeper.create("/test23","testdata".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
        //String path1 = zooKeeper.create("/test11","testdata".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
       //zooKeeper.delete("/test22",0);
        System.out.println(path);
        Thread.sleep(5000);
        zooKeeper.close();
    }

}
