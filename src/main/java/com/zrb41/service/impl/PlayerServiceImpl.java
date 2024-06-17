package com.zrb41.service.impl;

import com.zrb41.mapper.PlayerMapper;
import com.zrb41.pojo.Player;
import com.zrb41.service.PlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PlayerServiceImpl implements PlayerService {

    @Autowired
    private PlayerMapper playerMapper;

    @Override
    public Player queryById(Integer id) {
        return playerMapper.queryById(id);
    }

    @Override
    public List<Player> queryAll() {
        return playerMapper.queryAll();
    }

    @Override
    public int deleteById(Integer id) {
        return playerMapper.deleteById(id);
    }

    @Override
    public int insertBatch(List<Player> list) {
        return playerMapper.insertBatch(list);
    }

    @Override
    public int deleteBatch(List<Integer> ids) {
        return playerMapper.deleteBatch(ids);
    }
}
