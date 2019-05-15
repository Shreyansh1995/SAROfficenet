package com.netcommlabs.sarofficenet.utils;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class MyCalenderUtil {
    public static Calendar createCalenderWith15DaysBehind(String day, String month, String year) {
        Calendar calendar = Calendar.getInstance();
        calendar.clear();
        calendar.set(Calendar.MONTH, Integer.parseInt(month));
        calendar.set(Calendar.YEAR, Integer.parseInt(year));
        calendar.set(Calendar.DAY_OF_MONTH, Integer.parseInt(day));
        calendar.add(Calendar.DATE, 0);
        return calendar;
    }

    public static Calendar getCurrentDateTime() {
        Calendar c = Calendar.getInstance();

        c.setTimeInMillis(System.currentTimeMillis());
        return c;
    }

    public static Calendar createCalender(String day, String month, String year) {
        Calendar calendar = Calendar.getInstance();
        calendar.clear();
        calendar.set(Calendar.MONTH, Integer.parseInt(month));
        calendar.set(Calendar.YEAR, Integer.parseInt(year));
        calendar.set(Calendar.DAY_OF_MONTH, Integer.parseInt(day));
        return calendar;
    }


    public static boolean validDate(Context mContext, String from, String to, TextView error_text) {
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
            error_text.setVisibility(View.VISIBLE);
            error_text.setText("From date must be less than to date");
            return false;
        } else if (diff > 90) {
            error_text.setVisibility(View.VISIBLE);
            error_text.setText("From date & to date difference only  3 month");
            return false;
        }
        return true;
    }


    public static boolean validDateNewMethod(Context mContext, String from, String to, TextView error_text) {
        Date fromDate = null;
        Date toDate = null;// = getDateFromString(from);
        //   Date toDate = getDateFromString(to);

        SimpleDateFormat dates = new SimpleDateFormat("dd/MM/yyyy");
        try {
            fromDate = dates.parse(from);
            toDate = dates.parse(to);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        long diff = 0;
        try {
            diff = toDate.getTime() - fromDate.getTime();
            diff = TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
            Log.e("Days: ", "" + diff);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (diff < 0) {
            error_text.setVisibility(View.VISIBLE);
            error_text.setText("From date must be less than to date");
            return false;
        } else if (diff > 30) {
            error_text.setVisibility(View.VISIBLE);
            error_text.setText("From date & to date difference only  1 month");
            return false;
        }
        return true;
    }


    private static Date getDateFromString(String dateString) {
        Date date = null;
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
        try {
            date = format.parse(dateString);
            System.out.println(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    public static String getDateInServerSendFormat(String date) {
        String arr[] = date.split("-");
        return arr[0] + "/" + arr[1] + "/" + arr[2];
    }


    public static boolean validateFromTimeAndToTimeBasedOnAMPM(String fromTime, String toTime) {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm a");
        Calendar c = getCurrentDateTime();
        String fromDateFromat = "" + c.get(Calendar.YEAR) + "-" + (c.get(Calendar.MONTH) + 1) + "-" + c.get(Calendar.DAY_OF_MONTH) + " " + fromTime;
        String toDateFromat = "" + c.get(Calendar.YEAR) + "-" + (c.get(Calendar.MONTH) + 1) + "-" + c.get(Calendar.DAY_OF_MONTH) + " " + toTime;
        try {
            Date fromD = df.parse(fromDateFromat);
            Date toD = df.parse(toDateFromat);
            int hoursDifference = (int) ((toD.getTime() - fromD.getTime()) / 3600000L);
            // if remove the condition like fromTime == toTime
            if (hoursDifference == 0) {
                return true;
            }
            // then remove this if condition
            return toD.after(fromD);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean validateFromTimeAndToTime(String fromTime, String toTime) {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm");
        Calendar c = getCurrentDateTime();
        String fromDateFromat = "" + c.get(Calendar.YEAR) + "-" + (c.get(Calendar.MONTH) + 1) + "-" + c.get(Calendar.DAY_OF_MONTH) + " " + fromTime;
        String toDateFromat = "" + c.get(Calendar.YEAR) + "-" + (c.get(Calendar.MONTH) + 1) + "-" + c.get(Calendar.DAY_OF_MONTH) + " " + toTime;
        try {
            Date fromD = df.parse(fromDateFromat);
            Date toD = df.parse(toDateFromat);
            int hoursDifference = (int) ((toD.getTime() - fromD.getTime()) / 3600000L);
            // if remove the condition like fromTime == toTime
            if (hoursDifference == 0) {
                return true;
            }
            // then remove this if condition
            return toD.after(fromD);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    public static boolean validDate1(Context mContext, String from, String to, TextView error_text) {
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
            error_text.setVisibility(View.VISIBLE);
            error_text.setText("From Date Must be Less Than To Date");
            return false;
        } else if (diff > 360) {
            error_text.setVisibility(View.VISIBLE);
            error_text.setText("From Date & To Date Different Only 1 year");
            return false;
        }
        return true;
    }
}
