package com.zrb41.service;

import com.zrb41.entity.User;
import com.zrb41.utils.Result;

import java.util.List;

public interface FollowService {
    Result<Object> follow(Integer followUserId,Boolean isFollow);

    Result<Object> isFollow(Integer followUserId);

    Result<List<User>> followCommons(Integer id);
}
