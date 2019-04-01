package com.shichen.music.utils;

import android.text.TextUtils;
import android.util.Log;

import com.shichen.music.BuildConfig;

/**
 * @author by shichen, Email 754314442@qq.com, Date on 2019/4/1
 */
public class LogUtils {
    public static void Log(String TAG, String msg) {
        //根据app编译的类型判断是否需要Log
        if (BuildConfig.DEBUG) {
            if (!TextUtils.isEmpty(msg)) {
                Log.e(TAG, msg);
            }
        }
    }

    public static void Log(String TAG, String msg, Throwable e) {
        if (BuildConfig.DEBUG) {
            if (!TextUtils.isEmpty(msg)) {
                if (e != null) {
                    Log.e(TAG, msg, e);
                }
            }
        }
    }
}
