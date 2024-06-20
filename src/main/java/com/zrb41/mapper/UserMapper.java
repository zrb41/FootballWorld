package com.zrb41.mapper;

import com.zrb41.entity.User;
import org.springframework.stereotype.Repository;

@Repository
public interface UserMapper {

    // 根据电话号码查询用户
    User queryByPhone(String phone);

    // 添加用户
    int createUser(User user);

    // 根据id查询用户
    User queryById(Integer id);
}
