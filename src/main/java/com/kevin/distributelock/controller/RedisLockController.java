package com.kevin.distributelock.controller;

import com.kevin.distributelock.util.RedisLock;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.RedisStringCommands;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.data.redis.core.types.Expiration;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

/**
 * Redis的分布式锁
 * @author kevin
 * @version 1.0
 * @date 2021-02-26 10:10
 */
@RestController
@Slf4j
public class RedisLockController {

    @Autowired
    private RedisTemplate redisTemplate;

    String key = "redisKey";
    //value要保证每一个线程都不相同,要使用UUID
    String value = UUID.randomUUID().toString();

    //redis的分布式锁的应用
    @RequestMapping
    public String redisLock(){
        log.info("我进入了方法");
//        RedisCallback<Boolean> redisCallback = connection -> {
//            //设置NX
//            RedisStringCommands.SetOption option = RedisStringCommands.SetOption.ifAbsent();
//            //设置过期时间
//            Expiration expiration = Expiration.seconds(30);
//            //序列化key
//            byte[] redisKey = redisTemplate.getKeySerializer().serialize(key);
//            //序列化value
//            byte[] redisValue = redisTemplate.getValueSerializer().serialize(value);
//            return connection.set(redisKey, redisValue, expiration, option);
//        };
//
//        Boolean lock = (Boolean) redisTemplate.execute(redisCallback);
        RedisLock redisLock = new RedisLock(redisTemplate,key,value,30);
        if (redisLock.getLock()){
            log.info("我进入到了锁");
            try {
                Thread.sleep(15000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }finally {
                boolean result = redisLock.unLock();
                log.info("最后的结果是:"+result);
            }
        }
        log.info("方法执行完成");
        return "方法执行完成";
    }
}
