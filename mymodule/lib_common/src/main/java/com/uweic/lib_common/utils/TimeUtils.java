package com.uweic.lib_common.utils;


import androidx.annotation.NonNull;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by haoxuhong on 2019/10/15.
 *
 * @description: millis2String           : 将时间戳转为时间字符串
 * string2Millis           : 将时间字符串转为时间戳
 * string2Date             : 将时间字符串转为 Date 类型
 * date2String             : 将 Date 类型转为时间字符串
 * date2Millis             : 将 Date 类型转为时间戳
 * millis2Date             : 将时间戳转为 Date 类型
 * getNowMills             : 获取当前毫秒时间戳
 * getNowString            : 获取当前时间字符串
 * getNowDate              : 获取当前 Date
 */

public class TimeUtils {
    /*   yyyy-MM-dd中的yyyy不能写成大写,写成大写12月份的时候会有显示错误
     *  y：year-of-era；正正经经的年；https://mp.weixin.qq.com/s/pbys8Kj4IMgiqtWXVrgJdg
     * Y：week-based-year；只要本周跨年，那么这周就算入下一年；也就是 12 月*/
    private static final ThreadLocal<SimpleDateFormat> SDF_THREAD_LOCAL = new ThreadLocal<>();

    private static SimpleDateFormat getDefaultFormat() {
        return getDateFormat("yyyy-MM-dd HH:mm:ss");
    }

    public static SimpleDateFormat getDateFormat(String pattern) {
        SimpleDateFormat simpleDateFormat = SDF_THREAD_LOCAL.get();
        if (simpleDateFormat == null) {
            simpleDateFormat = new SimpleDateFormat(pattern, Locale.getDefault());
            SDF_THREAD_LOCAL.set(simpleDateFormat);
        } else {
            simpleDateFormat.applyPattern(pattern);
        }
        return simpleDateFormat;
    }

    /**
     * 将时间戳转为时间字符串
     * 默认yyyy-MM-dd HH:mm:ss
     */
    public static String millis2String(final long millis) {
        return millis2String(millis, getDefaultFormat());
    }

    /**
     * yyyy/MM/dd HH:mm
     *
     * @return the formatted time string
     */
    public static String millis2String(long millis, @NonNull final String pattern) {
        return millis2String(millis, getDateFormat(pattern));
    }

    /**
     * Milliseconds to the formatted time string.
     *
     * @param millis The milliseconds.
     * @param format The format.
     * @return the formatted time string
     */
    public static String millis2String(final long millis, @NonNull final DateFormat format) {
        return format.format(new Date(millis));
    }

    /**
     * 将时间字符串转为时间戳
     */
    /**
     * Formatted time string to the milliseconds.
     * <p>The pattern is {@code yyyy-MM-dd HH:mm:ss}.</p>
     *
     * @param time The formatted time string.
     * @return the milliseconds
     */
    public static long string2Millis(final String time) {
        return string2Millis(time, getDefaultFormat());
    }

    /**
     * Formatted time string to the milliseconds.
     *
     * @param time    The formatted time string.
     * @param pattern The pattern of date format, such as yyyy/MM/dd HH:mm
     * @return the milliseconds
     */
    public static long string2Millis(final String time, @NonNull final String pattern) {
        return string2Millis(time, getDateFormat(pattern));
    }

    /**
     * Formatted time string to the milliseconds.
     *
     * @param time   The formatted time string.
     * @param format The format.
     * @return the milliseconds
     */
    public static long string2Millis(final String time, @NonNull final DateFormat format) {
        try {
            return format.parse(time).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return -1;
    }

    /**
     *  将时间字符串转为 Date 类型
     */
    /**
     * Formatted time string to the date.
     * <p>The pattern is {@code yyyy-MM-dd HH:mm:ss}.</p>
     *
     * @param time The formatted time string.
     * @return the date
     */
    public static Date string2Date(final String time) {
        return string2Date(time, getDefaultFormat());
    }

    /**
     * Formatted time string to the date.
     *
     * @param time    The formatted time string.
     * @param pattern The pattern of date format, such as yyyy/MM/dd HH:mm
     * @return the date
     */
    public static Date string2Date(final String time, @NonNull final String pattern) {
        return string2Date(time, getDateFormat(pattern));
    }

    /**
     * Formatted time string to the date.
     *
     * @param time   The formatted time string.
     * @param format The format.
     * @return the date
     */
    public static Date string2Date(final String time, @NonNull final DateFormat format) {
        try {
            return format.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }
    /**
     * 将 Date 类型转为时间字符串
     */
    /**
     * Date to the formatted time string.
     * <p>The pattern is {@code yyyy-MM-dd HH:mm:ss}.</p>
     *
     * @param date The date.
     * @return the formatted time string
     */
    public static String date2String(final Date date) {
        return date2String(date, getDefaultFormat());
    }

    /**
     * Date to the formatted time string.
     *
     * @param date    The date.
     * @param pattern The pattern of date format, such as yyyy/MM/dd HH:mm
     * @return the formatted time string
     */
    public static String date2String(final Date date, @NonNull final String pattern) {
        return getDateFormat(pattern).format(date);
    }


    public static String date2String(final Date date, @NonNull final DateFormat format) {
        return format.format(date);
    }

    /**
     * 将 Date 类型转为时间戳
     */

    public static long date2Millis(final Date date) {
        return date.getTime();
    }

    /**
     * 将时间戳转为 Date 类型
     */
    public static Date millis2Date(final long millis) {
        return new Date(millis);
    }

    /**
     * 获取当前毫秒时间戳
     *
     * @return
     */
    public static long getNowMills() {
        return System.currentTimeMillis();
    }

    /**
     * 获取当前时间字符串
     */
    public static String getNowString() {
        return millis2String(System.currentTimeMillis(), getDefaultFormat());
    }


    public static String getNowString(@NonNull final DateFormat format) {
        return millis2String(System.currentTimeMillis(), format);
    }

    /**
     * 获取当前 Date
     */

    public static Date getNowDate() {
        return new Date();
    }


    /**
     * 判断两个日期毫秒数相差几天
     *
     * @param startTime   毫秒数
     * @param endTime    毫秒数
     * @return
     */
    public static long dateMillisDiff(long startTime, long endTime) {

        long nd = 1000 * 24 * 60 * 60;// 一天的毫秒数
        long day = 0;
        long diffMillis = startTime - endTime;
        day = diffMillis / nd;// 计算差多少天

        if (day >= 1) {
            return day;
        } else {
            if (day == 0) {
                return 1;
            } else {
                return 0;
            }

        }

    }


}
