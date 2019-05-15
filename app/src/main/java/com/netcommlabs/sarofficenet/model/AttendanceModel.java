package com.netcommlabs.sarofficenet.model;

/**
 * Created by Netcomm on 1/27/2017.
 */

public class AttendanceModel {

    private String Status;
    private String Color;
    private String CheckSundaySaturdayHoliday;
    private String PayCode;
    private String ShiftCode;
    private String DateOffice;

    private String ShiftName;
    private String StartBreak;
    private String Id;

    private String ShiftId;
    private String EndBreak;
    private String MendStart;

    public String getInTime() {
        return InTime;
    }

    public void setInTime(String inTime) {
        InTime = inTime;
    }

    public String getOutTime() {
        return OutTime;
    }

    public void setOutTime(String outTime) {
        OutTime = outTime;
    }

    private String InTime;
    private String OutTime;

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public String getDateOffice2() {
        return DateOffice2;
    }

    public void setDateOffice2(String dateOffice2) {
        DateOffice2 = dateOffice2;
    }

    public String getDateOffice() {
        return DateOffice;
    }

    public void setDateOffice(String dateOffice) {
        DateOffice = dateOffice;
    }

    private String DateOffice2;
    private String MendEnd;

    public String getColor() {
        return Color;
    }

    public void setColor(String color) {
        Color = color;
    }




 /*   public String getMendEnd() {
        return MendEnd;
    }

    public void setMendEnd(String mendEnd) {
        MendEnd = mendEnd;
    }
*/
    public String getCheckSundaySaturdayHoliday() {
        return CheckSundaySaturdayHoliday;
    }

    public void setCheckSundaySaturdayHoliday(String checkSundaySaturdayHoliday) {
        CheckSundaySaturdayHoliday = checkSundaySaturdayHoliday;
    }

    public String getPayCode() {
        return PayCode;
    }

    public void setPayCode(String payCode) {
        PayCode = payCode;
    }

/*    public String getShiftCode() {
        return ShiftCode;
    }

    public void setShiftCode(String shiftCode) {
        ShiftCode = shiftCode;
    }*/



 /*   public String getShiftName() {
        return ShiftName;
    }

    public void setShiftName(String shiftName) {
        ShiftName = shiftName;
    }

    public String getStartBreak() {
        return StartBreak;
    }

    public void setStartBreak(String startBreak) {
        StartBreak = startBreak;
    }*/

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }



 /*   public String getShiftId() {
        return ShiftId;
    }

    public void setShiftId(String shiftId) {
        ShiftId = shiftId;
    }

    public String getEndBreak() {
        return EndBreak;
    }

    public void setEndBreak(String endBreak) {
        EndBreak = endBreak;
    }

    public String getMendStart() {
        return MendStart;
    }

    public void setMendStart(String mendStart) {
        MendStart = mendStart;
    }*/
}
