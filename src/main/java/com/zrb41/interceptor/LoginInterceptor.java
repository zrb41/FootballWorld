package com.zrb41.interceptor;

import com.zrb41.entity.User;
import com.zrb41.utils.UserHolder;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Data;
import org.springframework.web.servlet.HandlerInterceptor;

// ========= 基于redis的登录拦截器 =========
// 第二层拦截器，被拦截到的请求必须要求用户已登录
@Data
public class LoginInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        User user = UserHolder.getUser();
        if(user==null){
            // 用户未登录
            response.setStatus(401);
            return false;
        }
        // 用户已登录
        return true;
    }
}
