package com.netcommlabs.sarofficenet.model;

public class ShortLeaveAchiveModel {
    private String EmpName;
    private String HODStatus;
    private String LeaveDate;
    private String Period;
    private String RMstatus;

    public ShortLeaveAchiveModel(String empName, String HODStatus, String leaveDate, String period, String RMstatus, String reqDate, String reqID, String reqNo, String type) {
        EmpName = empName;
        this.HODStatus = HODStatus;
        LeaveDate = leaveDate;
        Period = period;
        this.RMstatus = RMstatus;
        ReqDate = reqDate;
        ReqID = reqID;
        ReqNo = reqNo;
        Type = type;
    }

    private String ReqDate;
    private String ReqID;
    private String ReqNo;
    private String Type;

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

    public String getLeaveDate() {
        return LeaveDate;
    }

    public void setLeaveDate(String leaveDate) {
        LeaveDate = leaveDate;
    }

    public String getPeriod() {
        return Period;
    }

    public void setPeriod(String period) {
        Period = period;
    }

    public String getRMstatus() {
        return RMstatus;
    }

    public void setRMstatus(String RMstatus) {
        this.RMstatus = RMstatus;
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

    public String getType() {
        return Type;
    }

    public void setType(String type) {
        Type = type;
    }
}
