package com.zrb41.service;

import com.zrb41.entity.Player;

import java.util.List;

public interface CountryService {
    List<Player> queryById(Integer id);
}
