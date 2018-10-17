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

    number("整数"),
    string("字符"),
    decimal("小数"),
    email("邮箱"),
    tel("手机号"),
    qq("qq号码"),
    ip("ip地址");
    @Getter
    private String desc;


}
