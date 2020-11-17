package com.uweic.lib_base.utils;

import android.util.Log;

/**
 * Created by haoxuhong on 2019/9/23.
 *
 * @description: log的封装
 */
public class MyLogManager {
    public static int LOG_LEVEL = 6;//开发模式为6，上线模式为0
    private final static int ERROR = 1;
    private final static int WARN = 2;
    private final static int INFO = 3;
    private final static int DEBUG = 4;
    private final static int VERBOS = 5;

    public static void e(String tag, String msg) {
        if (LOG_LEVEL > ERROR)
            Log.e(tag, msg);
    }

    public static void w(String tag, String msg) {
        if (LOG_LEVEL > WARN)
            Log.w(tag, msg);
    }

    public static void i(String tag, String msg) {
        if (LOG_LEVEL > INFO)
            Log.i(tag, msg);
    }

    //解决打印日志不全的问题(Android系统对日志长度有限制的，最大长度为4K（注意是字符串的长度）)
//实际测试时发现，日志的最大长度其实是略小于4*1024的，为了保险起见，我们设置每一段日志长度segmentSize = 3*1024
    public static void d(String tag, String msg) {
        if (LOG_LEVEL > DEBUG) {
            if (tag == null || tag.length() == 0
                    || msg == null || msg.length() == 0)
                return;

            int segmentSize = 3 * 1024;
            long length = msg.length();
            if (length <= segmentSize) {// 长度小于等于限制直接打印
                Log.d(tag, msg);
            } else {
                while (msg.length() > segmentSize) {// 循环分段打印日志
                    String logContent = msg.substring(0, segmentSize);
                    msg = msg.replace(logContent, "");
                    Log.d(tag, logContent);
                }
                Log.d(tag, msg);// 打印剩余日志
            }
        }
    }

    public static void v(String tag, String msg) {
        if (LOG_LEVEL > VERBOS)
            Log.v(tag, msg);
    }
}
