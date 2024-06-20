package com.zrb41.service.impl;

import cn.hutool.core.util.BooleanUtil;
import com.zrb41.entity.Article;
import com.zrb41.entity.User;
import com.zrb41.mapper.ArticleMapper;
import com.zrb41.mapper.UserMapper;
import com.zrb41.service.ArticleService;
import com.zrb41.utils.Result;
import com.zrb41.utils.UserHolder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.zrb41.utils.RedisConstants.ARTICLE_LIKED;

@Slf4j
@Service
public class ArticleServiceImpl implements ArticleService {

    @Autowired
    private ArticleMapper articleMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public Result<Object> addArticle(Article article) {
        int i = articleMapper.addArticle(article);
        if (i == 1) {
            return Result.success();
        }
        return Result.error("文章不存在");
    }

    @Override
    public Result<Article> queryById(Integer id) {

        // 查询文章
        Article article = articleMapper.queryById(id);
        if (article == null) {
            return Result.error("文章不存在");
        }

        // 查询用户名字
        User user = userMapper.queryById(article.getUserId());
        if (user == null) {
            return Result.error("文章存在，用户不存在");
        }
        article.setUserName(user.getName());

        return Result.success(article);
    }

    @Override
    public Result<Object> likeArticle(Integer id) {

        // 在Redis中存储文章的点赞信息
        // key为文章id，value为set，里面存储已点赞用户的id


        // 获取当前登录用户的id
        Integer userId = UserHolder.getUser().getId();


        // 判断用户是否对文章点赞
        String key = ARTICLE_LIKED + id;
        // Boolean isMember = stringRedisTemplate.opsForSet().isMember(key, userId.toString());
        Double score = stringRedisTemplate.opsForZSet().score(key, userId.toString());

        // 获取点赞数，更新文章表
        Integer liked = articleMapper.queryById(id).getLiked();
        Article article = new Article();
        article.setId(id);

        String msg;

        if (score == null) {
            // 如果用户未点过赞
            article.setLiked(liked + 1);
            int i = articleMapper.updateArticle(article);

            if (i == 1) {
                // 更新redis,将用户id添加到zset中
                // stringRedisTemplate.opsForSet().add(key,userId.toString());
                stringRedisTemplate.opsForZSet().add(key, userId.toString(), System.currentTimeMillis());
            }
            msg="点赞成功";

        } else {
            // 如果用户已点赞
            article.setLiked(liked - 1);
            int i = articleMapper.updateArticle(article);

            if (i == 1) {
                // 更新redis,将用户id从zset中移除
                stringRedisTemplate.opsForZSet().remove(key, userId.toString());

            }
            msg="取消点赞";

        }

        return Result.success(msg);
    }

    @Override
    public Result<List<User>> queryArticleLikes(Integer id) {
        String key = ARTICLE_LIKED + id;
        Set<String> range = stringRedisTemplate.opsForZSet().range(key, 0, 5);
        if(range==null||range.size()==0){
            return Result.error("获取错误");
        }
        // 将set转为list
        List<Integer> list = range.stream().map(Integer::parseInt).toList();

        ArrayList<User> users = new ArrayList<>();
        for (Integer integer : list) {
            User user = userMapper.queryById(integer);
            users.add(user);
        }

        return Result.success(users);
    }
}
