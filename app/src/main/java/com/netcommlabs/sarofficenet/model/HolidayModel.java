package com.netcommlabs.sarofficenet.model;

/**
 * Created by Flash_Netcomm on 2/20/2019.
 */

public class HolidayModel {
    String Date;
    String Day;
    String DayStatus;
    String HolidayType;

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getDay() {
        return Day;
    }

    public void setDay(String day) {
        Day = day;
    }

    public String getDayStatus() {
        return DayStatus;
    }

    public void setDayStatus(String dayStatus) {
        DayStatus = dayStatus;
    }

    public String getHolidayType() {
        return HolidayType;
    }

    public void setHolidayType(String holidayType) {
        HolidayType = holidayType;
    }

    public String getIsCurrentMonthHoliday() {
        return IsCurrentMonthHoliday;
    }

    public void setIsCurrentMonthHoliday(String isCurrentMonthHoliday) {
        IsCurrentMonthHoliday = isCurrentMonthHoliday;
    }

    public String getOccation() {
        return Occation;
    }

    public void setOccation(String occation) {
        Occation = occation;
    }

    String IsCurrentMonthHoliday;
    String Occation;
}
