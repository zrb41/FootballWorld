package com.zrb41.interceptor;


import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.zrb41.pojo.User;
import com.zrb41.utils.RedisConstants;
import com.zrb41.utils.UserHolder;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.lang.Nullable;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import java.util.Map;
import java.util.concurrent.TimeUnit;


// 第一层拦截器：拦截一切请求，刷新redis中key的TTL
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RefreshTokenInterceptor implements HandlerInterceptor {

    // 通过构造器注入
    private StringRedisTemplate stringRedisTemplate;

    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 获取请求头中的token,名称在前端代码中设置 - authorization
        String token = request.getHeader("authorization");
        if(StrUtil.isBlank(token)){
            // 如果请求中没有携带token，直接返回true。
            // 这次请求可能不需要用户登录
            return true;
        }
        // Redis，根据key获取value
        Map<Object, Object> entries = stringRedisTemplate.opsForHash().entries(RedisConstants.LOGIN_TOKEN + token);
        if(entries.isEmpty()){
            // 原因同上
            return true;
        }

        // 将用户信息保存在ThreadLocal中
        User user = BeanUtil.fillBeanWithMap(entries, new User(), false);
        UserHolder.setUser(user);

        // 刷新token有效期
        stringRedisTemplate.expire(RedisConstants.LOGIN_TOKEN+token,RedisConstants.LOGIN_TOKEN_TTL, TimeUnit.MINUTES);

        return true;
    }

    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, @Nullable ModelAndView modelAndView) throws Exception {
    }

    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, @Nullable Exception ex) throws Exception {
    }
}
