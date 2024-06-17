package com.zrb41.controller;

import com.zrb41.pojo.Player;
import com.zrb41.service.CountryService;
import com.zrb41.utils.Result;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("country")
public class CountryController {

    @Autowired
    private CountryService countryService;

    // 根据国家id查询这个国家下的所有球员
    @GetMapping
    public Result<List<Player>> queryById(Integer id){
        List<Player> players = countryService.queryById(id);
        return Result.success(players);
    }
}
