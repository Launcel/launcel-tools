package xyz.launcel.lang;

import xyz.launcel.log.RootLogger;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class TimeFormatUtil
{
    public static String format(Date date, String format)
    {
        return new SimpleDateFormat(format).format(date);
    }

    public static Date parseDate(String date, String format)
    {
        try
        {
            return new SimpleDateFormat(format).parse(date);
        }
        catch (ParseException e)
        {
            RootLogger.error("时间转换失败..., exception={}", e.getCause());
        }
        return null;
    }

    public static Integer getYear(Date date)
    {
        var cl = Calendar.getInstance();
        cl.setTime(date);
        return cl.get(Calendar.YEAR);
    }

    /**
     * 获取某日期的月份
     *
     * @param date
     *
     * @return
     */
    public static Integer getMonth(Date date)
    {
        var cal = Calendar.getInstance();
        cal.setTime(date);
        return cal.get(Calendar.MONTH) + 1;

    }

    /**
     * 获取某日期的日数
     *
     * @param date
     *
     * @return
     */
    public static Integer getDay(Date date)
    {
        var cal = Calendar.getInstance();
        cal.setTime(date);
        return cal.get(Calendar.DATE);
    }

    /**
     * 获取指定日期所在周的第一天
     *
     * @param date
     *
     * @return
     */
    public static Date getFirstDayOfWeek(Date date)
    {
        var c = Calendar.getInstance();
        c.setFirstDayOfWeek(Calendar.MONDAY);
        c.setTime(date);
        c.set(Calendar.DAY_OF_WEEK, c.getFirstDayOfWeek()); // Monday
        return c.getTime();
    }

    /**
     * 获取指定日期所在周的最后一天
     *
     * @param date
     *
     * @return
     */
    public static Date getLastDayOfWeek(Date date)
    {
        var c = Calendar.getInstance();
        c.setFirstDayOfWeek(Calendar.MONDAY);
        c.setTime(date);
        c.set(Calendar.DAY_OF_WEEK, c.getFirstDayOfWeek() + 6); // Sunday
        return c.getTime();
    }

    /**
     * 获取某年某周的第一天
     *
     * @param year 目标年份
     * @param week 目标周数
     *
     * @return
     */
    public static Date getFirstDayOfWeek(int year, int week)
    {
        var c = Calendar.getInstance();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, Calendar.JANUARY);
        c.set(Calendar.DATE, 1);

        Calendar cal = (Calendar) c.clone();
        cal.add(Calendar.DATE, week * 7);

        return getFirstDayOfWeek(cal.getTime());
    }

    /**
     * 获取某年某周的最后一天
     *
     * @param year 目标年份
     * @param week 目标周数
     *
     * @return
     */
    public static Date getLastDayOfWeek(int year, int week)
    {
        var c = Calendar.getInstance();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, Calendar.JANUARY);
        c.set(Calendar.DATE, 1);

        Calendar cal = (Calendar) c.clone();
        cal.add(Calendar.DATE, week * 7);

        return getLastDayOfWeek(cal.getTime());
    }

    /**
     * 获取某年某月的第一天
     *
     * @param year  目标年份
     * @param month 目标月份
     *
     * @return
     */
    public static Date getFirstDayOfMonth(int year, int month)
    {
        month = month - 1;
        var c = Calendar.getInstance();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, month);

        var day = c.getActualMinimum(Calendar.DAY_OF_MONTH);

        c.set(Calendar.DAY_OF_MONTH, day);
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        return c.getTime();
    }

    /**
     * 获取某年某月的最后一天
     *
     * @param year  目标年份
     * @param month 目标月份
     *
     * @return
     */
    public static Date getLastDayOfMonth(int year, int month)
    {
        month = month - 1;
        var c = Calendar.getInstance();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, month);
        var day = c.getActualMaximum(Calendar.DAY_OF_MONTH);
        c.set(Calendar.DAY_OF_MONTH, day);
        c.set(Calendar.HOUR_OF_DAY, 23);
        c.set(Calendar.MINUTE, 59);
        c.set(Calendar.SECOND, 59);
        c.set(Calendar.MILLISECOND, 999);
        return c.getTime();
    }

    /**
     * 获取某个日期为星期几
     *
     * @param date
     *
     * @return String "星期*"
     */
    public static String getDayWeekOfDate1(Date date)
    {
        String[] weekDays = {"星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"};
        var      cal      = Calendar.getInstance();
        cal.setTime(date);

        var w = cal.get(Calendar.DAY_OF_WEEK) - 1;
        if (w < 0)
            w = 0;
        return weekDays[w];
    }

    /**
     * 获得指定日期的星期几数
     *
     * @param date
     *
     * @return int
     */
    public static Integer getDayWeekOfDate2(Date date)
    {
        var c = Calendar.getInstance();
        c.setTime(date);
        return c.get(Calendar.DAY_OF_WEEK);
    }

    /**
     * 获得指定时间加减参数后的日期(不计算则输入0)
     *
     * @param date        指定日期
     * @param year        年数，可正可负
     * @param month       月数，可正可负
     * @param day         天数，可正可负
     * @param hour        小时数，可正可负
     * @param minute      分钟数，可正可负
     * @param second      秒数，可正可负
     * @param millisecond 毫秒数，可正可负
     *
     * @return 计算后的日期
     */
    public static Date addDate(Date date, int year, int month, int day, int hour, int minute, int second, int millisecond)
    {
        var c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.YEAR, year);//加减年数
        c.add(Calendar.MONTH, month);//加减月数
        c.add(Calendar.DATE, day);//加减天数
        c.add(Calendar.HOUR, hour);//加减小时数
        c.add(Calendar.MINUTE, minute);//加减分钟数
        c.add(Calendar.SECOND, second);//加减秒
        c.add(Calendar.MILLISECOND, millisecond);//加减毫秒数

        return c.getTime();
    }

    /**
     * 获得两个日期的时间戳之差
     *
     * @param startDate
     * @param endDate
     *
     * @return
     */
    public static Long getDistanceTimestamp(Date startDate, Date endDate)
    {
        return (endDate.getTime() - startDate.getTime() + 1000000) / (3600 * 24 * 1000);
    }

    /**
     * 获得两个时间相差距离多少天多少小时多少分多少秒
     *
     * @param str1 时间参数 1 格式：1990-01-01 12:00:00
     * @param str2 时间参数 2 格式：2009-01-01 12:00:00
     *
     * @return long[] 返回值为：{天, 时, 分, 秒}
     */
    public static long[] getDistanceTime(Date one, Date two)
    {
        var day  = 0L;
        var hour = 0L;
        var min  = 0L;
        var sec  = 0L;

        var time1 = one.getTime();
        var time2 = two.getTime();
        var diff  = time2 - time1;
        if (time2 >= time1)
        {
            diff = time1 - time2;
        }
        day = diff / (24 * 60 * 60 * 1000);
        hour = (diff / (60 * 60 * 1000) - day * 24);
        min = ((diff / (60 * 1000)) - day * 24 * 60 - hour * 60);
        sec = (diff / 1000 - day * 24 * 60 * 60 - hour * 60 * 60 - min * 60);

        return new long[]{day, hour, min, sec};
    }

    /**
     * 解析两个日期之间的所有月份
     *
     * @param beginDateStr 开始日期，至少精确到yyyy-MM
     * @param endDateStr   结束日期，至少精确到yyyy-MM
     *
     * @return yyyy-MM日期集合
     */
    public static List<String> getMonthListOfDate(String beginDateStr, String endDateStr)
    {
        // 指定要解析的时间格式
        var f = new SimpleDateFormat("yyyy-MM");
        // 返回的月份列表
        String sRet;

        // 定义一些变量
        Date beginDate;
        Date endDate;

        Calendar beginGC;
        Calendar endGC;
        var      list = new ArrayList<String>();

        try
        {
            // 将字符串parse成日期
            beginDate = f.parse(beginDateStr);
            endDate = f.parse(endDateStr);

            // 设置日历
            beginGC = Calendar.getInstance();
            beginGC.setTime(beginDate);

            endGC = Calendar.getInstance();
            endGC.setTime(endDate);

            // 直到两个时间相同
            while (beginGC.getTime().compareTo(endGC.getTime()) <= 0)
            {
                sRet = beginGC.get(Calendar.YEAR) + "-" + (beginGC.get(Calendar.MONTH) + 1);
                list.add(sRet);
                // 以月为单位，增加时间
                beginGC.add(Calendar.MONTH, 1);
            }
            return list;
        }
        catch (Exception e)
        {
            RootLogger.error("error={}", e.getCause());
        }
        return null;
    }

    /**
     * 解析两个日期段之间的所有日期
     *
     * @param beginDateStr 开始日期  ，至少精确到yyyy-MM-dd
     * @param endDateStr   结束日期  ，至少精确到yyyy-MM-dd
     *
     * @return yyyy-MM-dd日期集合
     */
    public static List<String> getDayListOfDate(String beginDateStr, String endDateStr)
    {
        // 指定要解析的时间格式
        var f = new SimpleDateFormat("yyyy-MM-dd");

        // 定义一些变量
        Date beginDate;
        Date endDate;

        Calendar beginGC;
        Calendar endGC;
        var      list = new ArrayList<String>();

        try
        {
            // 将字符串parse成日期
            beginDate = f.parse(beginDateStr);
            endDate = f.parse(endDateStr);

            // 设置日历
            beginGC = Calendar.getInstance();
            beginGC.setTime(beginDate);

            endGC = Calendar.getInstance();
            endGC.setTime(endDate);
            var sdf = new SimpleDateFormat("yyyy-MM-dd");

            // 直到两个时间相同
            while (beginGC.getTime().compareTo(endGC.getTime()) <= 0)
            {

                list.add(sdf.format(beginGC.getTime()));
                // 以日为单位，增加时间
                beginGC.add(Calendar.DAY_OF_MONTH, 1);
            }
            return list;
        }
        catch (Exception e)
        {
            RootLogger.error("error={}", e.getCause());
        }
        return null;
    }

}
