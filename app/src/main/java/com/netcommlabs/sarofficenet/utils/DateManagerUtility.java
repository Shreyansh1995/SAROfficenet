package com.netcommlabs.sarofficenet.utils;

import android.app.DatePickerDialog;
import android.content.Context;
import android.widget.DatePicker;
import android.widget.TextView;

import java.util.Calendar;

public class DateManagerUtility {
    private Context mContext;
    private int curYear;
    private int curMonth;
    private int curDay;
    private int fromYear;
    private int fromMonth;
    private int fromDay;
    private int toYear;
    private int toMonth;
    private int toDay;
    private MySharedPreference pref;
    private TextView tv_FromDate;
    private TextView tv_ToDate;


    // for every screen this method should call only once :)
    public void setCurrentDate(Context mContext, TextView tvFrom, TextView tvTo) {
        Calendar c = MyCalenderUtil.getCurrentDateTime();
        curYear = c.get(Calendar.YEAR);
        curMonth = c.get(Calendar.MONTH);
        curDay = c.get(Calendar.DAY_OF_MONTH);
        toYear = curYear;
        toMonth = curMonth;
        toDay = curDay;
        pref = MySharedPreference.getInstance(mContext);
        tv_FromDate = tvFrom;
        tv_ToDate = tvTo;
        this.mContext = mContext;
        tv_ToDate.setText("" + curDay + "-" + (curMonth + 1) + "-" + curYear);
        //  tv_FromDate.setText("" + curDay + "-" + (curMonth -1) + "-" + curYear);
        Calendar cal = MyCalenderUtil.createCalenderWith15DaysBehind("" + curDay, "" + curMonth, "" + curYear);
        fromYear = cal.get(Calendar.YEAR);
        fromMonth = cal.get(Calendar.MONTH);
        fromDay = cal.get(Calendar.DAY_OF_MONTH);
        tv_FromDate.setText("" + cal.get(Calendar.DAY_OF_MONTH) + "-" + (cal.get(Calendar.MONTH) + 1) + "-" + cal.get(Calendar.YEAR));


    }



    public void FromDatePicker(Context context, final TextView tv) {

        Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR);
        int mMonth = c.get(Calendar.MONTH);
        int mDay = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog expdatePickerDialog = null;
        expdatePickerDialog = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                tv.setText("" + dayOfMonth + "-" + (month + 1) + "-" + year);
                fromDay = dayOfMonth;
                fromMonth = month;
                fromYear = year;

            }
        }, mYear, mMonth, mDay);
        Calendar cal = MyCalenderUtil.createCalender("" + curDay, "" + (curMonth), "" + curYear);
        expdatePickerDialog.getDatePicker();
        expdatePickerDialog.show();
    }

    public void FromDatePicker1(final TextView tv,final TextView tv2) {
        DatePickerDialog expdatePickerDialog = null;
        expdatePickerDialog = new DatePickerDialog(mContext, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                tv.setText("" + dayOfMonth + "-" + (month + 1) + "-" + year);
               /* if(month>=11){
                   Calendar c=
                    now.add(Calendar.MONTH, 1);

                    tv2.setText(+now.get(Calendar.DATE)
                            + "-"
                            + (now.get(Calendar.MONTH) + 1)
                            + "-"
                            + now.get(Calendar.YEAR));
                 //   tv2.setText(formattedDate);
                }else{
                    tv2.setText("" + dayOfMonth + "-" + (month + 2) + "-" + year);
                }*/

                fromDay = dayOfMonth;
                fromMonth = month;
                fromYear = year;

            }
        }, fromYear, fromMonth, fromDay);
        Calendar cal = MyCalenderUtil.createCalender("" + curDay, "" + (curMonth), "" + curYear);
        expdatePickerDialog.getDatePicker();
        expdatePickerDialog.show();
    }

    public void ToDatePicker(Context context,final TextView tv) {

        Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR);
        int mMonth = c.get(Calendar.MONTH);
        int mDay = c.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog expdatePickerDialog = null;

        expdatePickerDialog = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                tv.setText("" + dayOfMonth + "-" + (month + 1) + "-" + year);
                toDay = dayOfMonth;
                toMonth = month;
                toYear = year;
            }
        }, mYear, mMonth, mDay);
        Calendar cal = MyCalenderUtil.createCalender("" + curDay, "" + (curMonth), "" + curYear);
        expdatePickerDialog.getDatePicker();
        expdatePickerDialog.setTitle("");
        expdatePickerDialog.show();
    }
}
