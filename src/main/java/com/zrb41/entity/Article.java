package com.zrb41.entity;

import lombok.Data;

// 用户发布的文章
@Data
public class Article {

    private Integer id;
    private Integer userId;
    private String title;
    private String content;
    private Integer liked;
    private Integer comments;

    // 写这篇文章的用户名字 - 非Article表字段
    private String userName;

    // 告诉前端当前登录用户是否对这篇文章点赞，用于图标高亮与否的显示
    // 一个用户对某篇文章最多点赞一次，已点赞的情况下再次点击机会取消点赞
    private boolean isLike;
}
