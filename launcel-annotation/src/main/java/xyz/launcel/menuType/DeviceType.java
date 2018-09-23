package xyz.launcel.menuType;

import java.util.Objects;

/**
 * Created by launcel on 2018/7/26.
 */
public enum DeviceType
{
    Android,

    iOS,

    PC,

    Wechat,

    ;

    DeviceType() {}

    public static DeviceType getDevice(final String deviceName)
    {
        if (deviceName != null && !Objects.equals(deviceName, ""))
        {
            for (DeviceType device : values())
            {
                if (device.name().equalsIgnoreCase(deviceName))
                {
                    return device;
                }
            }
        }
        return null;
    }
}
