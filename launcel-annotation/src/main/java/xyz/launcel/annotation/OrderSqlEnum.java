package xyz.launcel.annotation;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum OrderSqlEnum
{
    ASC("升序"), DESC("降序");
    private String desc;
}
