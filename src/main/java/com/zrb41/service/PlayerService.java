package com.zrb41.service;

import com.zrb41.entity.Player;
import com.zrb41.utils.Result;

import java.util.List;

public interface PlayerService {
    Player queryById(Integer id);

    List<Player> queryAll();

    int update(Player player);

    int deleteById(Integer id);

    Result<Object> insertBatch(List<Player> list);

    int deleteBatch(List<Integer> ids);
}
