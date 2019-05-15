package com.netcommlabs.sarofficenet.model;

public class AttendanceRegulerizeModel {
    private String ContactNo;
    private String EmpName;
    private String FillDate;
    private String Period;
    public String ReqId;

    public String getRMStatus() {
        return RMStatus;
    }

    public void setRMStatus(String RMStatus) {
        this.RMStatus = RMStatus;
    }

    public String RMStatus;

    private String RegularizationText;

    public String getRegularizationText() {
        return RegularizationText;
    }

    public void setRegularizationText(String regularizationText) {
        RegularizationText = regularizationText;
    }

    public String getRegularizationTextId() {
        return RegularizationTextId;
    }

    public void setRegularizationTextId(String regularizationTextId) {
        RegularizationTextId = regularizationTextId;
    }

    private String RegularizationTextId;

    public String getContactNo() {
        return ContactNo;
    }

    public void setContactNo(String contactNo) {
        ContactNo = contactNo;
    }

    public String getEmpName() {
        return EmpName;
    }

    public void setEmpName(String empName) {
        EmpName = empName;
    }

    public String getFillDate() {
        return FillDate;
    }

    public void setFillDate(String fillDate) {
        FillDate = fillDate;
    }

    public String getPeriod() {
        return Period;
    }

    public void setPeriod(String period) {
        Period = period;
    }

    public String getReqId() {
        return ReqId;
    }

    public void setReqId(String reqId) {
        ReqId = reqId;
    }

    public String getReqNo() {
        return ReqNo;
    }

    public void setReqNo(String reqNo) {
        ReqNo = reqNo;
    }

    public String getRequestDate() {
        return RequestDate;
    }

    public void setRequestDate(String requestDate) {
        RequestDate = requestDate;
    }

    public String getResion() {
        return Resion;
    }

    public void setResion(String resion) {
        Resion = resion;
    }

    public String getRegularizationType() {
        return RegularizationType;
    }

    public void setRegularizationType(String regularizationType) {
        RegularizationType = regularizationType;
    }

    private String ReqNo;
    public String RequestDate;
    private String Resion;
    private String RegularizationType;
    private String Type;

    public String getType() {
        return Type;
    }

    public void setType(String type) {
        Type = type;
    }
}
