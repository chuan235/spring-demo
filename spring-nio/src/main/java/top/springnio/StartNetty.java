package top.springnio;


import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.concurrent.CountDownLatch;

public class StartNetty {

    public static void main(String[] args) throws InterruptedException {
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext("top.springnio");
        applicationContext.start();
        new CountDownLatch(1).await();
    }
}
