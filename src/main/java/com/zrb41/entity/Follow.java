package com.zrb41.entity;

import lombok.Data;

@Data
public class Follow {
    private Integer id;
    private Integer userId;
    private Integer followUserId;
}
