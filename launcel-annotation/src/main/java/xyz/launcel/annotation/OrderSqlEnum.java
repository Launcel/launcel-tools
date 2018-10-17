package xyz.launcel.annotation;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum OrderSqlEnum
{
    ASC("升序"),
    DESC("降序");
    @Getter
    private String desc;
}
