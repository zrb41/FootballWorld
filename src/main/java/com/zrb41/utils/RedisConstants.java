package com.zrb41.utils;

public class RedisConstants {
    // 验证码的key
    public static final String LOGIN_CODE_KEY="login:code:";
    // 验证码有效期
    public static final Long LOGIN_CODE_TTL=10l;

    public static final String LOGIN_TOKEN="login:token:";
    public static final Long LOGIN_TOKEN_TTL=30l;
}
