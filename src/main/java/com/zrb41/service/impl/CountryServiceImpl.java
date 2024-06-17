package com.zrb41.service.impl;

import com.zrb41.mapper.CountryMapper;
import com.zrb41.pojo.Player;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CountryServiceImpl implements com.zrb41.service.CountryService {

    @Autowired
    private CountryMapper countryMapper;

    @Override
    public List<Player> queryById(Integer id) {
        return countryMapper.queryById(id);
    }
}
