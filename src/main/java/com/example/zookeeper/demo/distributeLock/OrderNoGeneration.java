package com.example.zookeeper.demo.distributeLock;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 单项目生成订单编号
 *
 * @author lumingming
 * @version 1.0
 * @create 2018-11-05 11:05
 **/
public class OrderNoGeneration {

    private static int count = 0;

    // 生成订单号
    public String getNumber() {
        try {
            Thread.sleep(200);
        } catch (Exception e) {
            // TODO: handle exception
        }
        SimpleDateFormat simpt = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
        return simpt.format(new Date()) + "-" + ++count;

    }
}
