package com.zrb41.service;

import com.zrb41.dto.LoginDTO;
import com.zrb41.entity.Article;
import com.zrb41.entity.User;
import com.zrb41.utils.Result;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import java.util.List;

public interface UserService {
    Result<String> codeBySession(String phone, HttpSession httpSession);

    Result<String> codeByRedis(String phone);

    Result<Object> loginBySession(LoginDTO loginDTO,HttpSession httpSession);

    Result<Object> loginByRedis(LoginDTO loginDTO);

    Result<Object> logoutByRedis(HttpServletRequest request);

    Result<User> queryById(Integer id);

    Result<List<Article>> queryArticleById(Integer userId);
}
