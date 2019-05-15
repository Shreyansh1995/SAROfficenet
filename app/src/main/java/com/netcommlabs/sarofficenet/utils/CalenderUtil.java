package com.netcommlabs.sarofficenet.utils;

import android.content.Context;
import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class CalenderUtil {
    public static boolean validDate(Context mContext, String from, String to) {
        Date fromDate = getDateFromString(from);
        Date toDate = getDateFromString(to);
        long diff = 0;
        try {
            diff = toDate.getTime() - fromDate.getTime();
            diff = TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
            Log.e("Days: ", "" + diff);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (diff < 0) {
            return false;
        }
        return true;
    }

    private static Date getDateFromString(String dateString) {
        Date date = null;
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        try {
            date = format.parse(dateString);
            System.out.println(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }
}
