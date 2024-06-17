package com.zrb41.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Player {
    private Integer id;
    private String name;
    private Integer age;
    // 国籍
    private String countryName;
    // 俱乐部
    private String clubName;
    // 位置
    private String position;
    // 能力
    private Integer ability;
    // 身价
    private Integer price;
}
