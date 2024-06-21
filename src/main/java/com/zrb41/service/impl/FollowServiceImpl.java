package com.zrb41.service.impl;

import com.zrb41.entity.Follow;
import com.zrb41.entity.User;
import com.zrb41.mapper.FollowMapper;
import com.zrb41.mapper.UserMapper;
import com.zrb41.service.FollowService;
import com.zrb41.utils.Result;
import com.zrb41.utils.UserHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.zrb41.utils.RedisConstants.FOLLOWS;

@Service
public class FollowServiceImpl implements FollowService {

    @Autowired
    private FollowMapper followMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public Result<Object> follow(Integer followUserId, Boolean isFollow) {
        // 获取当前登录用户id
        Integer userId = UserHolder.getUser().getId();
        String msg="";
        String key=FOLLOWS+userId;
        if(isFollow){
            // 当前登录用户关注用户（id）
            Follow follow = new Follow();
            follow.setUserId(userId);
            follow.setFollowUserId(followUserId);
            // 新增关注表信息
            int i = followMapper.insert(follow);
            if(i==1){
                msg="关注成功";
                // 将关注信息存入Redis的set中，key为当前登录用户id，set中存入被关注者的id
                stringRedisTemplate.opsForSet().add(key,followUserId.toString());
            }

        }
        else{
            // 当前登录用户取关用户（id）
            // 删除关注信息
            int i = followMapper.deleteByUIdAndFId(userId, followUserId);
            if(i==1){
                msg="取关成功";
                stringRedisTemplate.opsForSet().remove(key,followUserId.toString());
            }
        }

        return Result.success(msg);
    }

    @Override
    public Result<Object> isFollow(Integer followUserId) {
        // 获取当前登录用户id
        Integer userId = UserHolder.getUser().getId();
        Follow follow = followMapper.queryByUIdAndFId(userId, followUserId);
        if(follow!=null){
            return Result.success("用户已关注");
        }
        return Result.success("用户未关注");
    }

    @Override
    public Result<List<User>> followCommons(Integer id) {
        // 获取当前登录用户id
        Integer userId = UserHolder.getUser().getId();
        String key1=FOLLOWS+userId;
        String key2=FOLLOWS+id;
        // 求交集
        Set<String> intersect = stringRedisTemplate.opsForSet().intersect(key1, key2);

        ArrayList<User> res = new ArrayList<>();
        if(intersect!=null){
            List<Integer> list = intersect.stream().map(Integer::parseInt).toList();
            for (Integer integer : list) {
                res.add(userMapper.queryById(integer));
            }
        }

        return Result.success(res);
    }


}
