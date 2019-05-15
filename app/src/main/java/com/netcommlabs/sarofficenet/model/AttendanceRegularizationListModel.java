package com.netcommlabs.sarofficenet.model;

public class AttendanceRegularizationListModel {
    private String EmpName;
    private String Period;
    private String RMStatus;
    private String RegularisationType;
    private String ReqDate;
    private String ReqID;
    private String ReqNo;
    private String Type;

    public AttendanceRegularizationListModel(String empName, String period, String RMStatus, String regularisationType, String reqDate, String reqID, String reqNo, String type) {
        EmpName = empName;
        Period = period;
        this.RMStatus = RMStatus;
        RegularisationType = regularisationType;
        ReqDate = reqDate;
        ReqID = reqID;
        ReqNo = reqNo;
        Type = type;
    }

    public String getEmpName() {
        return EmpName;
    }

    public void setEmpName(String empName) {
        EmpName = empName;
    }

    public String getPeriod() {
        return Period;
    }

    public void setPeriod(String period) {
        Period = period;
    }

    public String getRMStatus() {
        return RMStatus;
    }

    public void setRMStatus(String RMStatus) {
        this.RMStatus = RMStatus;
    }

    public String getRegularisationType() {
        return RegularisationType;
    }

    public void setRegularisationType(String regularisationType) {
        RegularisationType = regularisationType;
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
