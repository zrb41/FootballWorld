package com.zrb41.controller;

import com.zrb41.entity.Player;
import com.zrb41.service.PlayerService;
import com.zrb41.utils.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("player")
public class PlayerController {

    @Autowired
    private PlayerService playerService;

    @GetMapping
    // 根据id查询球员信息
    // 添加Redis缓存
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

    @PutMapping
    // 更新球员信息
    public Result<Object> update(@RequestBody Player player){
        int update = playerService.update(player);
        if(update==1){
            return Result.success();
        }
        else{
            return Result.error("更新失败");
        }
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

        return playerService.insertBatch(list);
    }


}
