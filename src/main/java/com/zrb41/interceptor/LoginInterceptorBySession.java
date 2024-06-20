package com.zrb41.interceptor;

import com.zrb41.entity.User;
import com.zrb41.utils.UserHolder;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.servlet.HandlerInterceptor;


// ========= 基于session的登录拦截器 =========
public class LoginInterceptorBySession implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        HttpSession session = request.getSession();
        Object user = session.getAttribute("user");
        if(user==null){
            // 用户还未登录，拒绝访问
            response.setStatus(401);
            return false;
        }
        // 将用户信息保存到ThreadLocal中，下次就不用从session中获取了
        UserHolder.setUser((User)user);
        return true;

    }

}
