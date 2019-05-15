package com.netcommlabs.sarofficenet.model;

public class LeaveListModel {
   String EmpCode;
    String EmpID;


    public String getCat() {
        return Cat;
    }

    public void setCat(String cat) {
        Cat = cat;
    }

    String Cat;

    public LeaveListModel(String empCode, String empID, String empName, String HODStatus, String leaveFinalStatus, String leave_Name, String mode, String no_of_Days, String period, String reqDate, String reqID, String reqNo, String rm_Status, String user_Status,String cat) {
        EmpCode = empCode;
        EmpID = empID;
        EmpName = empName;
        this.HODStatus = HODStatus;
        LeaveFinalStatus = leaveFinalStatus;
        Leave_Name = leave_Name;
        Mode = mode;
        No_of_Days = no_of_Days;
        Period = period;
        ReqDate = reqDate;
        ReqID = reqID;
        ReqNo = reqNo;
        Rm_Status = rm_Status;
        User_Status = user_Status;
        Cat = cat;
    }

    public String getEmpCode() {
        return EmpCode;
    }

    public void setEmpCode(String empCode) {
        EmpCode = empCode;
    }

    public String getEmpID() {
        return EmpID;
    }

    public void setEmpID(String empID) {
        EmpID = empID;
    }

    public String getEmpName() {
        return EmpName;
    }

    public void setEmpName(String empName) {
        EmpName = empName;
    }

    public String getHODStatus() {
        return HODStatus;
    }

    public void setHODStatus(String HODStatus) {
        this.HODStatus = HODStatus;
    }

    public String getLeaveFinalStatus() {
        return LeaveFinalStatus;
    }

    public void setLeaveFinalStatus(String leaveFinalStatus) {
        LeaveFinalStatus = leaveFinalStatus;
    }

    public String getLeave_Name() {
        return Leave_Name;
    }

    public void setLeave_Name(String leave_Name) {
        Leave_Name = leave_Name;
    }

    public String getMode() {
        return Mode;
    }

    public void setMode(String mode) {
        Mode = mode;
    }

    public String getNo_of_Days() {
        return No_of_Days;
    }

    public void setNo_of_Days(String no_of_Days) {
        No_of_Days = no_of_Days;
    }

    public String getPeriod() {
        return Period;
    }

    public void setPeriod(String period) {
        Period = period;
    }

    public String getReqDate() {
        return ReqDate;
    }

    public void setReqDate(String reqDate) {
        ReqDate = reqDate;
    }

    public String getReqID() {
        return ReqID;
    }

    public void setReqID(String reqID) {
        ReqID = reqID;
    }

    public String getReqNo() {
        return ReqNo;
    }

    public void setReqNo(String reqNo) {
        ReqNo = reqNo;
    }

    public String getRm_Status() {
        return Rm_Status;
    }

    public void setRm_Status(String rm_Status) {
        Rm_Status = rm_Status;
    }

    public String getUser_Status() {
        return User_Status;
    }

    public void setUser_Status(String user_Status) {
        User_Status = user_Status;
    }

    String EmpName;
    String HODStatus;
    String LeaveFinalStatus;
    String Leave_Name;
    String Mode;
    String No_of_Days;
    String Period;
    String ReqDate;
    String ReqID;
    String ReqNo;
    String Rm_Status;
    String User_Status;

}
