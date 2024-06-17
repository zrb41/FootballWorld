package com.zrb41.utils;

public class RedisConstants {
    // 验证码的key
    public static final String LOGIN_CODE_KEY="login:code:";
    // 验证码有效期
    public static final Long LOGIN_CODE_TTL=10l;

    // token的key
    public static final String LOGIN_TOKEN="login:token:";
    // token的有效期
    public static final Long LOGIN_TOKEN_TTL=30l;

    // 球员信息的key
    public static final String CACHE_PLAYER="cache:playerId:";
    // 球员信息的key的有效期
    public static final Long CACHE_PLAYER_TTL=30l;

    // 球员锁
    public static final String LOCK_PLAYER_KEY="lock:player:";
}
