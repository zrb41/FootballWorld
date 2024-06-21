package com.zrb41.mapper;

import com.zrb41.entity.Follow;
import com.zrb41.service.FollowService;
import org.springframework.stereotype.Repository;

@Repository
public interface FollowMapper {
    // 新增关注数据
    int insert(Follow follow);

    // 根据当前登录用户id和被关注用户id删除
    int deleteByUIdAndFId(Integer userId,Integer followUserId);

    Follow queryByUIdAndFId(Integer userId,Integer followUserId);
}
