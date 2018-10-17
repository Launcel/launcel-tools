package xyz.launcel.annotation;

import lombok.Getter;

public enum StatusType
{
    ENABLED(1, "开启"),
    DISENABLED(0, "关闭");
    @Getter
    private int    ststus;
    @Getter
    private String desc;

    StatusType(int ststus, String desc)
    {
        this.ststus = ststus;
        this.desc = desc;
    }
}
