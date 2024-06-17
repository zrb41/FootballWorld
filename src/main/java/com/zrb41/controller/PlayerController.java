package com.zrb41.controller;

import com.zrb41.mapper.PlayerMapper;
import com.zrb41.pojo.Player;
import com.zrb41.service.PlayerService;
import com.zrb41.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("player")
public class PlayerController {

    @Autowired
    private PlayerService playerService;

    @GetMapping
    // 根据id查询球员信息
    public Result<Player> queryById(Integer id){
        Player player = playerService.queryById(id);
        if(player==null){
            return Result.error("球员不存在");
        }
        return Result.success(player);
    }

    @GetMapping("queryAll")
    // 查询全部球员信息
    public Result<List<Player>> queryAll(){
        List<Player> players = playerService.queryAll();
        return Result.success(players);
    }

    @DeleteMapping
    // 根据id删除球员信息
    public Result<Object> deleteById(Integer id){
        int i = playerService.deleteById(id);
        if(i==1){
            return Result.success();
        }
        return Result.error("无此球员");
    }

    @DeleteMapping("deleteBatch")
    // 根据id批量删除球员信息
    public Result<Object> deleteBatch(@RequestParam List<Integer> ids){
        int i = playerService.deleteBatch(ids);
        return Result.success();
    }

    @PostMapping("insertBatch")
    // 添加1个或多个球员信息
    public Result<Object> insertBatch(@RequestBody List<Player> list){
        int i = playerService.insertBatch(list);

        // 不用添加事务，因为只执行一句sql。 insert into xx () values (),(),();
        // 添加失败报错的话直接走全局异常处理器
        return Result.success();
    }


}