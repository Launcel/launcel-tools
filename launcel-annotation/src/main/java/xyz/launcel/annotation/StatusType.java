package xyz.launcel.annotation;

import lombok.Getter;
import lombok.var;

@Getter
public enum StatusType
{
    ENABLED(1, "开启"),
    DISENABLED(0, "关闭");

    private int ststus;

    private String desc;

    StatusType(int ststus, String desc)
    {
        this.ststus = ststus;
        this.desc = desc;
    }

    public static StatusType valueOf(int status)
    {
        for (var statusType : values())
        {
            if (status == statusType.getStstus())
                return statusType;
        }
        throw new RuntimeException("获取状态类型失败：类型不在范围内...");
    }

}
