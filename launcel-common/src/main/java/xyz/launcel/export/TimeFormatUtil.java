package xyz.launcel.export;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeFormatUtil
{
    private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    public static String YYYY_MM_DD(Date date)
    {
        return sdf.format(date);
    }
}
