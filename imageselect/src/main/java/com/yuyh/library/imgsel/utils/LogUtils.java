package com.yuyh.library.imgsel.utils;

import android.content.Context;
import android.util.Log;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Log工具類，可控制Log輸出開關、保存Log到文件、過濾輸出等級
 *
 * @author yuyh.
 * @date 16/4/9.
 */
public class LogUtils {
    private static Boolean LOG_SWITCH = true; // 日誌文件總開關
    private static Boolean LOG_TO_FILE = false; // 日誌寫入文件開關
    private static String LOG_TAG = "TAG"; // 默認的tag
    private static char LOG_TYPE = 'v';// 輸入日誌類型，v代表輸出所有信息,w則只輸出警告...
    private static int LOG_SAVE_DAYS = 7;// sd卡中日誌文件的最多保存天數

    private final static SimpleDateFormat LOG_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// 日誌的輸出格式
    private final static SimpleDateFormat FILE_SUFFIX = new SimpleDateFormat("yyyy-MM-dd");// 日誌文件格式
    private static String LOG_FILE_PATH; // 日誌文件保存路徑
    private static String LOG_FILE_NAME;// 日誌文件保存名稱

    public static void init(Context context) { // 在Application中初始化
        LOG_FILE_PATH = FileUtils.createRootPath(context);
        LOG_FILE_NAME = "Log";
    }

    /**************************** Warn *********************************/
    public static void w(Object msg) {
        w(LOG_TAG, msg);
    }

    public static void w(String tag, Object msg) {
        w(tag, msg, null);
    }

    public static void w(String tag, Object msg, Throwable tr) {
        log(tag, msg.toString(), tr, 'w');
    }

    /*************************** Error ********************************/
    public static void e(Object msg) {
        e(LOG_TAG, msg);
    }

    public static void e(String tag, Object msg) {
        e(tag, msg, null);
    }

    public static void e(String tag, Object msg, Throwable tr) {
        log(tag, msg.toString(), tr, 'e');
    }

    /*************************** Debug ********************************/
    public static void d(Object msg) {
        d(LOG_TAG, msg);
    }

    public static void d(String tag, Object msg) {// 調試信息
        d(tag, msg, null);
    }

    public static void d(String tag, Object msg, Throwable tr) {
        log(tag, msg.toString(), tr, 'd');
    }

    /**************************** Info *********************************/
    public static void i(Object msg) {
        i(LOG_TAG, msg);
    }

    public static void i(String tag, Object msg) {
        i(tag, msg, null);
    }

    public static void i(String tag, Object msg, Throwable tr) {
        log(tag, msg.toString(), tr, 'i');
    }

    /************************** Verbose ********************************/
    public static void v(Object msg) {
        v(LOG_TAG, msg);
    }

    public static void v(String tag, Object msg) {
        v(tag, msg, null);
    }

    public static void v(String tag, Object msg, Throwable tr) {
        log(tag, msg.toString(), tr, 'v');
    }

    /**
     * 根據tag, msg和等級，輸出日誌
     *
     * @param tag
     * @param msg
     * @param level
     */
    private static void log(String tag, String msg, Throwable tr, char level) {
        if (LOG_SWITCH) {
            if ('e' == level && ('e' == LOG_TYPE || 'v' == LOG_TYPE)) { // 輸出錯誤信息
                Log.e(tag, msg, tr);
            } else if ('w' == level && ('w' == LOG_TYPE || 'v' == LOG_TYPE)) {
                Log.w(tag, msg, tr);
            } else if ('d' == level && ('d' == LOG_TYPE || 'v' == LOG_TYPE)) {
                Log.d(tag, msg, tr);
            } else if ('i' == level && ('d' == LOG_TYPE || 'v' == LOG_TYPE)) {
                Log.i(tag, msg, tr);
            } else {
                Log.v(tag, msg, tr);
            }
            if (LOG_TO_FILE)
                log2File(String.valueOf(level), tag, msg + tr == null ? "" : "\n" + Log.getStackTraceString(tr));
        }
    }

    /**
     * 打開日誌文件並寫入日誌
     *
     * @return
     **/
    private synchronized static void log2File(String mylogtype, String tag, String text) {
        Date nowtime = new Date();
        String date = FILE_SUFFIX.format(nowtime);
        String dateLogContent = LOG_FORMAT.format(nowtime) + ":" + mylogtype + ":" + tag + ":" + text; // 日誌輸出格式
        File destDir = new File(LOG_FILE_PATH);
        if (!destDir.exists()) {
            destDir.mkdirs();
        }
        File file = new File(LOG_FILE_PATH, LOG_FILE_NAME + date);
        try {
            FileWriter filerWriter = new FileWriter(file, true);
            BufferedWriter bufWriter = new BufferedWriter(filerWriter);
            bufWriter.write(dateLogContent);
            bufWriter.newLine();
            bufWriter.close();
            filerWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 刪除指定的日誌文件
     */
    public static void delFile() {// 刪除日誌文件
        String needDelFiel = FILE_SUFFIX.format(getDateBefore());
        File file = new File(LOG_FILE_PATH, needDelFiel + LOG_FILE_NAME);
        if (file.exists()) {
            file.delete();
        }
    }

    /**
     * 得到LOG_SAVE_DAYS天前的日期
     *
     * @return
     */
    private static Date getDateBefore() {
        Date nowtime = new Date();
        Calendar now = Calendar.getInstance();
        now.setTime(nowtime);
        now.set(Calendar.DATE, now.get(Calendar.DATE) - LOG_SAVE_DAYS);
        return now.getTime();
    }
}
