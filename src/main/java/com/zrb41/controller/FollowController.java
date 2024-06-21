package com.zrb41.controller;

import com.zrb41.entity.User;
import com.zrb41.mapper.FollowMapper;
import com.zrb41.service.FollowService;
import com.zrb41.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("follow")
public class FollowController {

    @Autowired
    private FollowService followService;

    // 当前登录用户，根据被关注/取关用户的followUserId，关注/取关该用户
    @PutMapping("{followUserId}/{isFollow}")
    public Result<Object> follow(@PathVariable Integer followUserId,@PathVariable Boolean isFollow){
        return followService.follow(followUserId,isFollow);
    }

    // 当前登录用户是否已经关注了用户(followUserId)
    @GetMapping("or/not/{followUserId}")
    public Result<Object> isFollow(@PathVariable Integer followUserId){
        return followService.isFollow(followUserId);
    }

    // 查询当前登录用户与某用户（id）的共同关注好友
    @GetMapping("common/{id}")
    public Result<List<User>> followCommons(@PathVariable Integer id){
        return followService.followCommons(id);
    }
}
