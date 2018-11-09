package com.example.zookeeper.demo.distributeLock;

import com.sun.org.apache.bcel.internal.generic.LCONST;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 订单服务
 *
 * @author lumingming
 * @version 1.0
 * @create 2018-11-05 11:12
 **/
public class OrderService implements  Runnable{
    private OrderNoGeneration orderNoGeneration = new OrderNoGeneration();

    //Lock lock = new ReentrantLock();

    com.example.zookeeper.demo.distributeLock.Lock lock = new DistributeLock();


    // 不使用锁，会出现重复订单
   /*public void getNum(){
        String num = orderNoGeneration.getNumber();
        System.out.println(Thread.currentThread().getName() + ",生成订单ID:" + num);
   }*/

   // 使用synchronized同步锁
    /*public void getNum(){
        synchronized (this){
            String num = orderNoGeneration.getNumber();
            System.out.println(Thread.currentThread().getName() + ",生成订单ID:" + num);
        }
    }*/

    // 使用lock手动锁
   /* public void getNum(){
        try {
            lock.lock();
            String num = orderNoGeneration.getNumber();
            System.out.println(Thread.currentThread().getName() + ",生成订单ID:" + num);
        }catch (Exception e){
            throw e;
        }finally {
            // 必须关闭
            lock.unlock();
        }
    }*/


   @Override
    public void run() {
        try {
            // 上锁
            lock.getLock();
            // synchronized (this) {
            // 模拟用户生成订单号
            getNumber();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 釋放鎖資源
            lock.unLock();
        }
    }

    public void getNumber() {
        String number = orderNoGeneration.getNumber();
        System.out.println(Thread.currentThread().getName() + ",##number:" + number);
    }

    public static void main(String[] args) {
        System.out.println("##模拟生成订单号开始...");

        for (int i = 0; i < 100; i++) {
            new Thread(new OrderService()).start();
        }
    }



}
