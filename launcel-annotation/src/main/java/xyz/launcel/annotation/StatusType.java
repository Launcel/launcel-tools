package xyz.launcel.annotation;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@Getter
@AllArgsConstructor
public enum StatusType
{
    ENABLED(1, "开启"),
    DISENABLED(0, "关闭");

    private int ststus;

    private String desc;

    public static StatusType valueOf(int status)
    {
        return Arrays.stream(values()).filter(statusType -> statusType.getStstus() == status).findFirst().orElseThrow(RuntimeException::new);
    }
    //"获取状态类型失败：类型不在范围内..."

}
