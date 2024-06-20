package com.zrb41.controller;

import com.zrb41.entity.Article;
import com.zrb41.entity.User;
import com.zrb41.service.ArticleService;
import com.zrb41.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("article")
public class ArticleController {

    @Autowired
    private ArticleService articleService;

    // 添加用户发布的文章
    @PostMapping
    public Result<Object> addArticle(@RequestBody Article article){
        return articleService.addArticle(article);
    }

    // 根据id查询文章
    @GetMapping("{id}")
    public Result<Article> queryById(@PathVariable int id){
        return articleService.queryById(id);
    }

    // 用户对文章点赞/取消点赞
    @GetMapping("like/{id}")
    public Result<Object> likeArticle(@PathVariable Integer id){
        return articleService.likeArticle(id);
    }

    // 根据id查询某文章被哪些用户点赞，根据用户的点赞时间排序
    @GetMapping("likes/{id}")
    public Result<List<User>> queryArticleLikes(@PathVariable Integer id){
        return articleService.queryArticleLikes(id);
    }
}
