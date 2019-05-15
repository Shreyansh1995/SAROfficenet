package com.netcommlabs.sarofficenet.utils;

import android.util.Log;

/**
 * Created by Flash_Netcomm on 2/16/2019.
 */

public class LogUtils {
    private boolean isShown = true;
    private static LogUtils logUtils;

    public LogUtils(String msg, String title) {
        if (isShown) {
            Log.e(title, msg);
        }
    }

    public static LogUtils showLog(String title, String msg) {
        if (logUtils == null) {
            new LogUtils(msg, title);
            logUtils = null;
        }
        return logUtils;
    }
}
