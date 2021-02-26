package com.kevin.distributelock.service;

import com.kevin.distributelock.util.RedisLock;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

/**
 * 定时任务接口
 * @author kevin
 * @version 1.0
 * @date 2021-02-26 13:44
 */
@Slf4j
@Service
public class SchedulerService {

    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 定时任务发送短信
     */
//    @Scheduled(cron = "0/30 * * * * ?")
//    public void sendSms(){
//        try(RedisLock redisLock = new RedisLock(redisTemplate,"autoSms",30)){
//            if (redisLock.getLock()){
//                log.info("正在向138XXXXXX发送短信");
//            }
//        }catch (Exception e){
//            throw new IllegalArgumentException("出现分布式锁异常");
//        }
//    }
}
