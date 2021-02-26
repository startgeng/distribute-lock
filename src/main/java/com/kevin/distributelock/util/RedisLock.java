package com.kevin.distributelock.util;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.redis.connection.RedisStringCommands;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.data.redis.core.types.Expiration;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

/**
 * 分布式锁
 * @author kevin
 * @version 1.0
 * @date 2021-02-26 11:25
 */
@Data
@ToString
@NoArgsConstructor
public class RedisLock {

    //redis的封装
    private RedisTemplate redisTemplate;
    //redis的key
    private String key;
    //redis的value
    private String value;
    //过期时间(秒)
    private int expireTime;

    public RedisLock(RedisTemplate redisTemplate,String key,String value,int expireTime){
        this.redisTemplate = redisTemplate;
        this.key = key;
        this.value = UUID.randomUUID().toString();
        this.expireTime = expireTime;
    }

    public boolean getLock(){
        RedisCallback<Boolean> redisCallback = connection -> {
            //设置NX
            RedisStringCommands.SetOption option = RedisStringCommands.SetOption.ifAbsent();
            //设置过期时间
            Expiration expiration = Expiration.seconds(30);
            //序列化key
            byte[] redisKey = redisTemplate.getKeySerializer().serialize(key);
            //序列化value
            byte[] redisValue = redisTemplate.getValueSerializer().serialize(value);
            return connection.set(redisKey, redisValue, expiration, option);
        };

        return  (Boolean) redisTemplate.execute(redisCallback);
    }

    public boolean unLock(){
        String script ="if redis.call('get', KEYS[1]) == ARGV[1] \n" +
                "    then \n" +
                "\t    return redis.call('del', KEYS[1]) \n" +
                "\telse \n" +
                "\t    return 0 \n" +
                "end";
        List<String> list = Arrays.asList(key);
        RedisScript<Boolean> redisScript = RedisScript.of(script,Boolean.class);
        return (Boolean) redisTemplate.execute(redisScript, list, value);
    }
}