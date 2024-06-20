package com.zrb41.service;

import com.zrb41.entity.Article;
import com.zrb41.entity.User;
import com.zrb41.utils.Result;

import java.util.List;

public interface ArticleService {
    Result<Object> addArticle(Article article);

    Result<Article> queryById(Integer id);

    Result<Object> likeArticle(Integer id);

    Result<List<User>> queryArticleLikes(Integer id);
}
