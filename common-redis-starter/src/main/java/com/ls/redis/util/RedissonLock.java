package com.ls.redis.util;

import com.ls.redis.manager.RedissonManager;
import org.redisson.Redisson;
import org.redisson.api.RLock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

/**
 * 封装redisson锁
 */
public class RedissonLock {

    private static final Logger log = LoggerFactory.getLogger(RedissonLock.class);

    private RedissonManager redissonManager;
    private Redisson redisson;


    public RedissonLock(RedissonManager redissonManager) {
        this.redissonManager = redissonManager;
        this.redisson = redissonManager.getRedisson();
    }

    public RedissonLock() {
    }

    /**
     * 加锁操作 （设置锁的有效时间）
     *
     * @param lockName  锁名称
     * @param leaseTime 锁有效时间
     */
    public void lock(String lockName, long leaseTime) {
        RLock rLock = redisson.getLock(lockName);
        rLock.lock(leaseTime, TimeUnit.SECONDS);
    }

    /**
     * 加锁操作 (锁有效时间采用默认时间30秒）
     *
     * @param lockName 锁名称
     */
    public void lock(String lockName) {
        RLock rLock = redisson.getLock(lockName);
        rLock.lock();
    }

    /**
     * 加锁操作(tryLock锁，没有等待时间）
     *
     * @param lockName  锁名称
     * @param waitTime  等待时间 单位秒
     */
    public boolean tryLock(String lockName, long waitTime) {

        RLock rLock = redisson.getLock(lockName);
        boolean getLock = false;
        try {
            getLock = rLock.tryLock(waitTime, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            log.error("redisson tryLock error," + lockName, e);
            e.printStackTrace();
            return false;
        }
        return getLock;
    }

    /**
     * 加锁操作(tryLock锁，有等待时间）
     *
     * @param lockName  锁名称
     * @param leaseTime 锁有效时间 单位秒
     * @param waitTime  等待时间 单位秒
     */
    public boolean tryLock(String lockName, long leaseTime, long waitTime) {

        RLock rLock = redisson.getLock(lockName);
        boolean getLock = false;
        try {
            getLock = rLock.tryLock(waitTime, leaseTime, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            log.error("redisson tryLock error," + lockName, e);
            e.printStackTrace();
            return false;
        }
        return getLock;
    }

    /**
     * 解锁
     *
     * @param lockName 锁名称
     */
    public void unlock(String lockName) {
        redisson.getLock(lockName).unlock();
    }

    /**
     * 判断该锁是否已经被线程持有
     *
     * @param lockName 锁名称
     */
    public boolean isLock(String lockName) {
        RLock rLock = redisson.getLock(lockName);
        return rLock.isLocked();
    }


    /**
     * 判断该线程是否持有当前锁
     *
     * @param lockName 锁名称
     */
    public boolean isHeldByCurrentThread(String lockName) {
        RLock rLock = redisson.getLock(lockName);
        return rLock.isHeldByCurrentThread();
    }

    public RedissonManager getRedissonManager() {
        return redissonManager;
    }

    public void setRedissonManager(RedissonManager redissonManager) {
        this.redissonManager = redissonManager;
    }


}
