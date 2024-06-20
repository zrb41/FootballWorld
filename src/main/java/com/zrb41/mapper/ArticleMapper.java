package com.zrb41.mapper;

import com.zrb41.entity.Article;
import com.zrb41.utils.Result;
import org.springframework.stereotype.Repository;

@Repository
public interface ArticleMapper {
    int addArticle(Article article);

    Article queryById(Integer id);

    int updateArticle(Article article);
}
