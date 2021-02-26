package com.kevin.distributelock.controller;

import lombok.extern.slf4j.Slf4j;
import org.redisson.Redisson;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeUnit;

/**
 * Redisson实现分布式锁
 * @author kevin
 * @version 1.0
 * @date 2021-02-26 15:57
 */
@RestController
@Slf4j
public class RedissionLockController {

    @RequestMapping(value = "redissionLock")
    public String redissionLock(){
        Config config = new Config();
        config.useSingleServer().setAddress("redis://127.0.0.1:6379");
        RedissonClient redisson = Redisson.create(config);
        //业务名称
        RLock lock = redisson.getLock("order");
        log.info("我进入了方法!!!!");
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
        log.info("方法执行完成");
        return "方法执行完成";
    }
}