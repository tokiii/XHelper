package com.lost.cuthair.utils;

import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * Log工具类
 * Created by lost on 2016/4/11.
 */
public class LogUtils {

    private LogUtils() {
        throw new UnsupportedOperationException("cannot be instantiated");
    }

    public static boolean isDebug = true;// 是否需要打印log

    private static final String TAG = "info";

    // 下面四个是默认tag的函数
    public static void i(String msg) {
        if (isDebug)
            Log.i(TAG, msg);
    }

    public static void d(String msg) {
        if (isDebug)
            Log.d(TAG, msg);
    }

    public static void e(String msg) {
        if (isDebug)
            Log.e(TAG, msg);
    }

    public static void v(String msg) {
        if (isDebug)
            Log.v(TAG, msg);
    }

    // 下面是传入自定义tag的函数
    public static void i(String tag, String msg) {
        if (isDebug)
            Log.i(tag, msg);
    }

    public static void d(String tag, String msg) {
        if (isDebug)
            Log.i(tag, msg);
    }

    public static void e(String tag, String msg) {
        if (isDebug)
            Log.i(tag, msg);
    }

    public static void v(String tag, String msg) {
        if (isDebug)
            Log.i(tag, msg);
    }


    /**
     * 将毫秒数转换成yyyy-MM-dd-HH-mm-ss的格式
     * @param milliseconds
     * @return
     */
    private static  String paserTime(long milliseconds) {
        System.setProperty("user.timezone", "Asia/Shanghai");
        TimeZone tz = TimeZone.getTimeZone("Asia/Shanghai");
        TimeZone.setDefault(tz);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
        String times = format.format(new Date(milliseconds));

        return times;
    }


    /**
     * 过长的Log信息保存到本地目录
     * @param s
     */
    public static void put( String s) {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            File dir = new File(Environment.getExternalStorageDirectory().toString() + File.separator + "crash" + File.separator);
            if (!dir.exists()) {
                dir.mkdir();
            }
            try {
                String fileName = "local--" +dir.toString() + File.separator + paserTime(System.currentTimeMillis()) + ".log";
                FileOutputStream fos = new FileOutputStream(fileName);
                fos.write(s.toString().getBytes());
                fos.flush();
                fos.close();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

    }


}
