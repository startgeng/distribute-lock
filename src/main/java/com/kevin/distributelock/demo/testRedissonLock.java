package com.kevin.distributelock.demo;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.redisson.Redisson;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.redisson.config.SingleServerConfig;

import java.util.concurrent.TimeUnit;

/**
 * 使用redisssionLock来进行分布式锁
 * @author kevin
 * @version 1.0
 * @date 2021-02-26 15:47
 */
@Slf4j
public class testRedissonLock {

    @Test
    public void redisRessionLock(){
        Config config = new Config();
        config.useSingleServer().setAddress("redis://127.0.0.1:6379");
        RedissonClient redisson = Redisson.create(config);
        //业务名称
        RLock lock = redisson.getLock("order");
        try {
            //超时时间
            lock.lock(30, TimeUnit.SECONDS);
            log.info("我获得了锁!!!");
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }finally {
            log.info("我释放了锁");
            lock.unlock();
        }
    }
}
