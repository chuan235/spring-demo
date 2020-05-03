package com.gc.task.service;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 * Springboot的异步任务
 */
@Service
@Async
public class AsyncService {

    /**
     * @Async 表示这一个方式是异步方法
     */
    //@Async
    public void hello(){
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("hello world");
    }

    public void test(){
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("hello test");
    }
}
