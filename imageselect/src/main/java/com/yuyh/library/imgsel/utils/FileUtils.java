package com.yuyh.library.imgsel.utils;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Environment;

import java.io.File;

/**
 * @author yuyh.
 * @date 16/4/9.
 */
public class FileUtils {

    private static final String TAG = FileUtils.class.getSimpleName();

    /**
     * 創建根緩存目錄
     *
     * @return
     */
    public static String createRootPath(Context context) {
        String cacheRootPath = "";
        if (isSdCardAvailable()) {
            // /sdcard/Android/data/<application package>/cache
            cacheRootPath = context.getExternalCacheDir().getPath();
        } else {
            // /data/data/<application package>/cache
            cacheRootPath = context.getCacheDir().getPath();
        }
        return cacheRootPath;
    }

    public static boolean isSdCardAvailable() {
        return Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState());
    }

    /**
     * 遞歸創建文件夾
     *
     * @param dirPath
     * @return 創建失敗返回""
     */
    public static String createDir(String dirPath) {
        try {
            File file = new File(dirPath);
            if (file.getParentFile().exists()) {
                LogUtils.i("----- 創建文件夾" + file.getAbsolutePath());
                file.mkdir();
                return file.getAbsolutePath();
            } else {
                createDir(file.getParentFile().getAbsolutePath());
                LogUtils.i("----- 創建文件夾" + file.getAbsolutePath());
                file.mkdir();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dirPath;
    }

    /**
     * 遞歸創建文件夾
     *
     * @param file
     * @return 創建失敗返回""
     */
    public static String createFile(File file) {
        try {
            if (file.getParentFile().exists()) {
                LogUtils.i("----- 創建文件" + file.getAbsolutePath());
                file.createNewFile();
                return file.getAbsolutePath();
            } else {
                createDir(file.getParentFile().getAbsolutePath());
                file.createNewFile();
                LogUtils.i("----- 創建文件" + file.getAbsolutePath());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String getApplicationId(Context appContext) throws IllegalArgumentException {
        ApplicationInfo applicationInfo = null;
        try {
            applicationInfo = appContext.getPackageManager().getApplicationInfo(appContext.getPackageName(), PackageManager.GET_META_DATA);
            if (applicationInfo == null) {
                throw new IllegalArgumentException(" get application info = null, has no meta data! ");
            }
            LogUtils.d(appContext.getPackageName() + " " + applicationInfo.metaData.getString("APP_ID"));
            return applicationInfo.metaData.getString("APP_ID");
        } catch (PackageManager.NameNotFoundException e) {
            throw new IllegalArgumentException(" get application info error! ", e);
        }
    }
}
