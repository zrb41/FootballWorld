package com.zrb41.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.lang.UUID;
import cn.hutool.core.util.RandomUtil;
import com.zrb41.dto.LoginDTO;
import com.zrb41.mapper.UserMapper;
import com.zrb41.pojo.User;
import com.zrb41.service.UserService;
import com.zrb41.utils.RedisConstants;
import com.zrb41.utils.Result;
import com.zrb41.utils.UserHolder;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

import static com.zrb41.utils.RedisConstants.*;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public Result<String> codeBySession(String phone, HttpSession httpSession) {

        // 校验手机号格式
        if(phone.isBlank()||phone.length()!=11){
            return Result.error("手机号为空或长度错误");
        }
        String regex = "^1([38][0-9]|4[579]|5[0-3,5-9]|6[6]|7[0135678]|9[89])\\d{8}$";
        boolean matches = Pattern.compile(regex).matcher(phone).matches();
        if(!matches){
            return Result.error("手机号格式错误");
        }

        // 生成验证码
        String code = RandomUtil.randomNumbers(6);

        // 将验证码和电话信息保存在session中
        httpSession.setAttribute("code",code);
        httpSession.setAttribute("phone",phone);

        return Result.success(code);
    }

    @Override
    public Result<String> codeByRedis(String phone) {

        // 校验手机号格式
        if(phone.isBlank()||phone.length()!=11){
            return Result.error("手机号为空或长度错误");
        }
        String regex = "^1([38][0-9]|4[579]|5[0-3,5-9]|6[6]|7[0135678]|9[89])\\d{8}$";
        boolean matches = Pattern.compile(regex).matcher(phone).matches();
        if(!matches){
            return Result.error("手机号格式错误");
        }

        // 生成验证码
        String code = RandomUtil.randomNumbers(6);

        // 将验证码保存在redis中.key为手机号，value为验证码，并设置key的有效期
        stringRedisTemplate.opsForValue().set(LOGIN_CODE_KEY +phone,code,LOGIN_CODE_TTL, TimeUnit.MINUTES);

        return Result.success(code);
    }

    @Override
    public Result<Object> loginBySession(LoginDTO loginDTO, HttpSession httpSession) {

        // 基于验证码登录

        if(!loginDTO.getPhone().equals(httpSession.getAttribute("phone"))){
            return Result.error("手机号前后不一致");
        }
        if(!loginDTO.getCode().equals(httpSession.getAttribute("code"))){
            return Result.error("验证码错误");
        }

        User user = userMapper.queryByPhone(loginDTO.getPhone());
        if(user==null){
            // 创建用户
            user=new User();
            user.setPhone(loginDTO.getPhone());
            user.setName("user_"+RandomUtil.randomString(6));
            userMapper.createUser(user);
        }

        // 将用户信息保存在session中
        httpSession.setAttribute("user",user);
        return Result.success();

    }

    @Override
    public Result<Object> loginByRedis(LoginDTO loginDTO) {

        // 从redis中获取验证码进行校验
        String code = stringRedisTemplate.opsForValue().get(LOGIN_CODE_KEY+loginDTO.getPhone());
        if(!loginDTO.getCode().equals(code)){
            return Result.error("验证码错误");
        }

        // 根据电话号码查询用户，如果不存在则创建新用户
        User user = userMapper.queryByPhone(loginDTO.getPhone());
        if(user==null){
            // 创建用户
            user=new User();
            user.setPhone(loginDTO.getPhone());
            user.setName("user_"+RandomUtil.randomString(6));
            userMapper.createUser(user);
        }

        // 将用户信息保存在redis中，key为随机生成的token，value为用户信息
        // 用户后面的请求都会携带这个token
        String token = UUID.randomUUID().toString(true);
        // 将用户信息转化为map - 转化后 ”id“=1 需要手动转化为 "id"="1"
        Map<String, Object> userMap = BeanUtil.beanToMap(user);
        Object id = userMap.get("id");
        userMap.put("id",String.valueOf(id));
        stringRedisTemplate.opsForHash().putAll(LOGIN_TOKEN+token,userMap);
        stringRedisTemplate.expire(LOGIN_TOKEN+token,LOGIN_TOKEN_TTL,TimeUnit.MINUTES);

        return Result.success(token);
    }

    @Override
    public Result<Object> logoutByRedis(HttpSession httpSession) {
        // 让用户的token失效
        // todo
        return Result.success();
    }
}
