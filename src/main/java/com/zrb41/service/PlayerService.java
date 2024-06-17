package com.zrb41.service;

import com.zrb41.pojo.Player;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface PlayerService {
    Player queryById(Integer id);

    List<Player> queryAll();

    int deleteById(Integer id);

    int insertBatch(List<Player> list);

    int deleteBatch(List<Integer> ids);
}
