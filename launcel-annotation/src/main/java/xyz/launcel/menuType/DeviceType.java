package xyz.launcel.menuType;

import lombok.var;

import java.util.Objects;

/**
 * Created by launcel on 2018/7/26.
 */
public enum DeviceType
{Android,

    iOS,

    PC,

    Wechat,

    H5,
    ;

    DeviceType() {}

    public static DeviceType getDevice(final String deviceName)
    {
        if (deviceName != null && !Objects.equals(deviceName, ""))
        {
            for (var device : values())
            {
                if (device.name().equalsIgnoreCase(deviceName.trim()))
                {
                    return device;
                }
            }
        }
        return null;
    }}
