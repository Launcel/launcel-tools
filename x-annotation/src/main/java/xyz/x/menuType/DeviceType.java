package xyz.x.menuType;

import java.util.Arrays;

/**
 * Created by launcel on 2018/7/26.
 */
public enum DeviceType
{
    Android,
    iOS,
    PC,
    Wechat,
    H5,
    ;

    DeviceType() {}

    public static DeviceType getDevice(final String deviceName)
    {
        return Arrays.stream(values()).filter(device -> device.name().equalsIgnoreCase(deviceName.trim())).findFirst().orElse(null);
    }

}
