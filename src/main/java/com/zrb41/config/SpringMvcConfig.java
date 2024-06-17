package com.zrb41.config;

import com.zrb41.interceptor.LoginInterceptor;
import com.zrb41.interceptor.RefreshTokenInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class SpringMvcConfig implements WebMvcConfigurer {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 第一层拦截器：拦截一切请求,负责刷新redis中ke的ttl
        registry.addInterceptor(new RefreshTokenInterceptor(stringRedisTemplate)).order(0);
        // 第二层拦截器：负责拦截未登录的请求
        registry.addInterceptor(new LoginInterceptor())
                .excludePathPatterns(
                        "/user/codeByRedis",
                        "/user/loginByRedis").order(1);

    }

}
