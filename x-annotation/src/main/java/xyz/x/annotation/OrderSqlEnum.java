package xyz.x.annotation;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum OrderSqlEnum
{
    ASC("升序"),
    DESC("降序");

    private String desc;
}
