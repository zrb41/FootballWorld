package com.zrb41.controller;

import com.zrb41.dto.LoginDTO;
import com.zrb41.entity.Article;
import com.zrb41.entity.User;
import com.zrb41.service.UserService;
import com.zrb41.utils.Result;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Slf4j
@RestController
@RequestMapping("user")
public class UserController {

    @Autowired
    private UserService userService;

    // 基于Session - 给用户发送验证码
    @GetMapping("codeBySession")
    public Result<String> codeBySession(String phone, HttpSession httpSession){
        return userService.codeBySession(phone, httpSession);
    }

    // 基于Redis - 给用户发送验证码
    // 将验证码保存在redis中，key为phone，value为验证码
    @GetMapping("codeByRedis")
    public Result<String> codeByRedis(String phone){
        return userService.codeByRedis(phone);
    }

    // 基于session - 实现用户的创建/登录 - 根据验证码
    @PostMapping("loginBySession")
    public Result<Object> loginBySession(@RequestBody LoginDTO loginDTO,HttpSession httpSession){
        return userService.loginBySession(loginDTO,httpSession);
    }

    // 基于Redis - 实现用户的创建/登录 - 根据验证码
    @PostMapping("loginByRedis")
    public Result<Object> loginBySession(@RequestBody LoginDTO loginDTO){
        return userService.loginByRedis(loginDTO);
    }

    // todo
    // 基于Session - 用户退出登录

    // 基于Redis - 用户退出登录
    @PostMapping("logoutByRedis")
    public Result<Object> logoutByRedis(HttpServletRequest request){
        return userService.logoutByRedis(request);
    }

    // todo
    // commit message规范+插件

    // 根据用户id查询用户
    @GetMapping("{id}")
    public Result<User> queryById(@PathVariable Integer id){
        return userService.queryById(id);
    }

    // 根据用户id查询该用户发布了哪些文章
    @GetMapping("of/user")
    public Result<List<Article>> queryArticleById(Integer userId){
        return userService.queryArticleById(userId);
    }
}
