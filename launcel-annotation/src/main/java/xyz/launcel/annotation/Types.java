/*
 * Copyright (C), 2017
 * Author: Launcel
 * Date: 17.11.17
 * Version: 1.0
 */

package xyz.launcel.annotation;


import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Created by Launcel in 2017/9/27
 */
@Getter
@AllArgsConstructor
public enum Types
{

    NUMBER("整数"),
    STRING("字符"),
    DECIMAL("小数"),
    EMAIL("邮箱"),
    TEL("手机号"),
    QQ("qq号码"),
    IP("ip地址"),
    LIST("数组");

    private String desc;

}
