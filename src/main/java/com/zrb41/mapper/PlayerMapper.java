package com.zrb41.mapper;

import com.zrb41.entity.Player;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PlayerMapper {

    Player queryById(Integer id);

    List<Player> queryAll();

    int deleteById(Integer id);

    int insertBatch(List<Player> list);

    int deleteBatch(List<Integer> ids);

    int update(Player player);
}
