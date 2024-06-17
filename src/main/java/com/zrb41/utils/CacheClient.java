package com.zrb41.utils;

import cn.hutool.core.util.BooleanUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

import static com.zrb41.utils.RedisConstants.LOCK_PLAYER_KEY;

// 工具类 - 用于封装缓存穿透和击穿
@Slf4j
@Component
public class CacheClient {
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    // 线程池
    private static final ExecutorService CACHE_REBUILD_EXECUTOR = Executors.newFixedThreadPool(10);

    // 将任意Java对象序列化为json并存储在string类型的key中，可以设置TTL过期时间
    public void set(String key, Object value, Long time, TimeUnit unit){
        stringRedisTemplate.opsForValue().set(key, JSONUtil.toJsonStr(value),time,unit);
    }

    // 根据指定的key查询缓存，并反序列化为指定类型，利用缓存空值解决缓存穿透
    // 泛型 + 函数式编程
    public <R,ID> R queryWithPassThrough(String keyPrefix, ID id, Class<R> type, Function<ID,R> db,Long time,TimeUnit unit){

        String key=keyPrefix+id;
        // 从Redis中获取key
        String json = stringRedisTemplate.opsForValue().get(key);

        if(json!=null){
            if(json.isBlank()){
                // 获取到了缓存的空值
                return null;
            }
            // 数据存在，直接返回
            return JSONUtil.toBean(json,type);
        }

        // 缓存中不存在key，则查询数据库
        R r = db.apply(id);

        if(r==null){
            // 将空值写入Redis,并设置较短的有效期
            // 【缓存穿透】
            stringRedisTemplate.opsForValue().set(key,"",1L,TimeUnit.MINUTES);
            return null;
        }
        // 数据库中存在，则写入缓存
        this.set(key,r,time,unit);

        return r;
    }

    // 将任意Java对象序列化为json并存储在string类型的key中，并且可以设置逻辑过期时间，用于处理缓存击穿
    public void setWithLogicalExpire(String key,Object value,Long time,TimeUnit unit){
        // 设置逻辑过期时间
        RedisData redisData = new RedisData();
        redisData.setData(value);
        redisData.setExpireTime(LocalDateTime.now().plusSeconds(unit.toSeconds(time)));
        // 写入Redis，不设置过期时间
        stringRedisTemplate.opsForValue().set(key,JSONUtil.toJsonStr(redisData));
    }

    // 根据指定的key查询缓存，并反序列化为指定类型，利用逻辑过期时间解决缓存击穿
    public<R,ID> R queryWithLogicalExpire(String keyPrefix, ID id, Class<R> type, Function<ID,R> db,Long time,TimeUnit unit){
        String key=keyPrefix+id;
        // 从Redis中获取key
        String json = stringRedisTemplate.opsForValue().get(key);
        // 设置了逻辑过期时间的key一定存在，
        // 如果查询不到key，说明该key没有设置逻辑过期时间，直接返回null
        if(json==null){
            return null;
        }

        // 获取对象
        RedisData redisData = JSONUtil.toBean(json, RedisData.class);
        R r = JSONUtil.toBean((JSONObject) redisData.getData(), type);
        LocalDateTime expireTime = redisData.getExpireTime();

        // 判断是否过期
        if(expireTime.isAfter(LocalDateTime.now())){
            // 未过期，直接返回
            return r;
        }

        // 已过期，缓存重建
        String lockKey=LOCK_PLAYER_KEY+id;
        boolean isLock = tryLock(lockKey);
        if(isLock){
            // 开启独立线程，进行缓存重建
            CACHE_REBUILD_EXECUTOR.submit(() -> {
                try {
                    // 查询数据库
                    R newR = db.apply(id);
                    // 重建缓存
                    this.setWithLogicalExpire(key, newR, time, unit);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }finally {
                    // 释放锁
                    unlock(lockKey);
                }
            });
        }

        // 返回过期数据
        return r;
    }

    private boolean tryLock(String key) {
        Boolean flag = stringRedisTemplate.opsForValue().setIfAbsent(key, "1", 10, TimeUnit.SECONDS);
        return BooleanUtil.isTrue(flag);
    }

    private void unlock(String key) {
        stringRedisTemplate.delete(key);
    }



}
