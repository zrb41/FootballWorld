package com.zrb41.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.json.JSONUtil;
import com.zrb41.mapper.PlayerMapper;
import com.zrb41.pojo.Player;
import com.zrb41.service.PlayerService;
import com.zrb41.utils.CacheClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

import static com.zrb41.utils.RedisConstants.*;

@Slf4j
@Service
public class PlayerServiceImpl implements PlayerService {

    @Autowired
    private PlayerMapper playerMapper;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private CacheClient cacheClient;

    @Override
    public Player queryById(Integer id) {

        // 查询球员信息，利用缓存空值解决缓存穿透
        return cacheClient.queryWithPassThrough(CACHE_PLAYER, id, Player.class, id1 -> playerMapper.queryById(id1), CACHE_PLAYER_TTL, TimeUnit.MINUTES);

        // 查询球员信息，利用逻辑过期时间解决缓存穿透，前提：进行缓存预热，将热点key提前放入redis中

    }

    @Override
    public List<Player> queryAll() {
        return playerMapper.queryAll();
    }

    @Override
    @Transactional
    // 添加事务，保证缓存和数据库的一致性
    public int update(Player player) {
        // 先修改数据库
        int update = playerMapper.update(player);

        // 再删除缓存
        if(player.getId()==null){
            // 球员id不存在
            return 0;
        }
        String key=CACHE_PLAYER+player.getId();
        stringRedisTemplate.delete(key);

        return update;
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
