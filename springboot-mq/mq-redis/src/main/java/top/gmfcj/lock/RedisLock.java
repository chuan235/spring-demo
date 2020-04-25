package top.gmfcj.lock;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.LockSupport;

public class RedisLock implements Lock {

    @Autowired
    private RedisTemplate redisTemplate;

    private static final String MUTEX_KEY = "redis_mutex_lock";

    @Override
    public void lock() {
        for(;;){
            if(tryLock()){
                return;
            }
        }
    }

    @Override
    public void lockInterruptibly() throws InterruptedException {

    }

    @Override
    public boolean tryLock() {
        // 尝试获取锁 30秒 设置值+设置过期时间
        return redisTemplate.opsForValue().setIfAbsent(MUTEX_KEY, Thread.currentThread().getId(), 30, TimeUnit.SECONDS);
    }

    @Override
    public boolean tryLock(long timeout, TimeUnit unit) throws InterruptedException {
        long nanosTimeout = unit.toNanos(timeout);
        if (Thread.interrupted()) {
            throw new InterruptedException();
        }
        if (nanosTimeout <= 0L) {
            return false;
        }
        final long deadline = System.nanoTime() + nanosTimeout;
        for (; ; ) {
            if (tryLock()) {
                // 成功获取锁
                return true;
            }
            nanosTimeout = deadline - System.nanoTime();
            if (nanosTimeout <= 0L) {
                // 已超时
                return false;
            }
            if (Thread.interrupted()) {
                throw new InterruptedException();
            }
        }
    }

    @Override
    public void unlock() {
        Object value = redisTemplate.opsForValue().get(MUTEX_KEY);
        Long threadId = new Long(Thread.currentThread().getId());
        if (value != null && value.equals(threadId)) {
            redisTemplate.delete(MUTEX_KEY);
        }
    }

    @Override
    public Condition newCondition() {
        return null;
    }
}
