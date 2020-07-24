package com.wang.mybatisplus.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.util.Date;

// 使用 lombok 的 @Data 注解简化代码
@Data
public class User {

    @TableId(type = IdType.AUTO)
    private Long id;

    private String name;
    private Integer age;
    private String email;

    // 插入时自动填充字段值
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    // 插入和更新时自动填充字段
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;

    // 版本号
    @Version
    @TableField(fill = FieldFill.INSERT)
    private Integer version;

}