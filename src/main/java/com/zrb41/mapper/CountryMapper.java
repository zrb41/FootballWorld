package com.zrb41.mapper;

import com.zrb41.pojo.Player;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CountryMapper {
    List<Player>  queryById(Integer id);
}
