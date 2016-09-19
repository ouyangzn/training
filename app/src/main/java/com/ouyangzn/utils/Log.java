package com.ouyangzn.utils;

import com.ouyangzn.BuildConfig;

/**
 * <p>日志类,用于代替android提供的Log,以达到更好的控制日志打印的目的
 * 用{@link #LOG_LEVER}来控制最低打印的日志等级,{@link #LOG_LEVER}可在application中自由设置
 * <p>开发中调试所需打印的日志最高等级不应超过<pre>Log.d();</pre>
 * 
 * @author ouyangzn
 */
public class Log {

    private Log() {
        throw new UnsupportedOperationException("cannot be instantiated");
    }
    
    public final static short VERBOSE = android.util.Log.VERBOSE;
    public final static short DEBUG = android.util.Log.DEBUG;
    public final static short INFO = android.util.Log.INFO;
    public final static short WARN = android.util.Log.WARN;
    public final static short ERROR = android.util.Log.ERROR;
    
    /** 打印log日志的最低等级 */
    public static short LOG_LEVER = VERBOSE;
    /** verbose和debug等级的日志是否要打印 */
//    public static boolean LOG_DEBUG = BuildConfig.LOG_DEBUG;
    

    public static void v(String tag, String msg) {
        if (BuildConfig.LOG_DEBUG && LOG_LEVER <= VERBOSE)
            android.util.Log.v(tag, msg);
    }
    
    public static void v(String tag, String msg, Throwable tr) {
        if (BuildConfig.LOG_DEBUG && LOG_LEVER <= VERBOSE)
            android.util.Log.v(tag, msg, tr);
    }

    public static void d(String tag, String msg) {
        if (BuildConfig.LOG_DEBUG && LOG_LEVER <= DEBUG)
            android.util.Log.d(tag, msg);
    }
    
    public static void d(String tag, String msg, Throwable tr) {
        if (BuildConfig.LOG_DEBUG && LOG_LEVER <= DEBUG)
            android.util.Log.d(tag, msg, tr);
    }
    
    public static void i(String tag, String msg) {
        if (LOG_LEVER <= INFO || BuildConfig.LOG_DEBUG)
            android.util.Log.i(tag, msg);
    }
    
    public static void i(String tag, String msg, Throwable tr) {
        if (LOG_LEVER <= INFO || BuildConfig.LOG_DEBUG)
            android.util.Log.i(tag, msg, tr);
    }

    public static void w(String tag, String msg) {
        if (LOG_LEVER <= WARN || BuildConfig.LOG_DEBUG)
            android.util.Log.w(tag, msg);
    }
    
    public static void w(String tag, String msg, Throwable tr) {
        if (LOG_LEVER <= WARN || BuildConfig.LOG_DEBUG)
            android.util.Log.w(tag, msg, tr);
    }

    public static void e(String tag, String msg) {
        if (LOG_LEVER <= ERROR || BuildConfig.LOG_DEBUG)
            android.util.Log.e(tag, msg);
    }
    
    public static void e(String tag, String msg, Throwable tr) {
        if (LOG_LEVER <= ERROR || BuildConfig.LOG_DEBUG)
            android.util.Log.e(tag, msg, tr);
    }

}
