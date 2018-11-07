package com.example.zookeeper.demo.distributeLock;

public interface Lock {
    // 获取锁
    public void getLock();

    // 释放锁
    public void unLock();
}
