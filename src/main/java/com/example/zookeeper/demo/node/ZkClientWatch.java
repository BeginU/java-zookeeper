package com.example.zookeeper.demo.node;

import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;

import java.util.concurrent.CountDownLatch;

/**
 * zk的自定义监听器
 *
 * @author lumingming
 * @version 1.0
 * @create 2018-11-01 16:26
 **/
public class ZkClientWatch implements Watcher {

    private static final String ADDRESS = "127.0.0.1:2181";
    private static final int TIME_OUT  = 2000;
    private CountDownLatch countDownLatch = new CountDownLatch(1);
    private static final String LOG_MAIN = "[main]";
    private ZooKeeper zk;

    /**
    *  获取连接
    * @author lumingming
    * @date 2018/11/1
    * @param
    * @return
    */
    public void getConnection(String url,int timeOut){
        try {
            zk = new ZooKeeper(url,timeOut,this);
            System.out.println("zk启动成功...");
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
    *   创建节点
    * @author lumingming
    * @date 2018/11/1
    * @param
    * @return
    */
    public boolean createPath(String path, String data) {
        try {
            this.exists(path, true);
            this.zk.create(path, data.getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
            System.out.println( "节点创建成功, Path:" + path + ",data:" + data);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
    *  判断节点是否存在
    * @author lumingming
    * @date 2018/11/1
    * @param
    * @return
    */
    public Stat exists(String path, boolean needWatch) {
        try {
            return this.zk.exists(path, needWatch);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
    *  更新节点数据
    * @author lumingming
    * @date 2018/11/1
    * @param
    * @return
    */
    public boolean updateNode(String path,String data) throws KeeperException, InterruptedException {
        exists(path, true);
        this.zk.setData(path, data.getBytes(), -1);
        return false;
    }

    /**
    *  重写通知回调函数
    * @author lumingming
    * @date 2018/11/1
    * @param
    * @return
    */
    @Override
    public void process(WatchedEvent event) {
        // 获取事件状态
        Event.KeeperState keeperState = event.getState();
        // 获取事件类型
        Event.EventType eventType = event.getType();
        // zk 路径
        String path = event.getPath();
        System.out.println("进入到 process() keeperState:" + keeperState + ", eventType:" + eventType + ", path:" + path);
        // 判断是否建立连接
        if (Event.KeeperState.SyncConnected == keeperState) {
            if (Event.EventType.None == eventType) {
                // 如果建立建立成功,让后程序往下走
                System.out.println(LOG_MAIN + "zk 建立连接成功!");
                countDownLatch.countDown();
            } else if (Event.EventType.NodeCreated == eventType) {
                System.out.println(LOG_MAIN + "事件通知,新增node节点" + path);
            } else if (Event.EventType.NodeDataChanged == eventType) {
                System.out.println(LOG_MAIN + "事件通知,当前node节点" + path + "被修改....");
            }
            else if (Event.EventType.NodeDeleted == eventType) {
                System.out.println(LOG_MAIN + "事件通知,当前node节点" + path + "被删除....");
            }
        }
        System.out.println("--------------------------------------------------------");
    }

    public static void main(String arg[]) throws  Exception{
        ZkClientWatch zkClientWatch = new ZkClientWatch();
        zkClientWatch.getConnection(ADDRESS,TIME_OUT);
        //zkClientWatch.createPath("/111","杨洋");
        /*Stat stat = zkClientWatch.exists("/222",true);
        if(stat != null){
            System.out.println("节点存在");
        }else{
            System.out.println("节点不存在");
        }*/
        zkClientWatch.updateNode("/222","王阳");
    }

}
