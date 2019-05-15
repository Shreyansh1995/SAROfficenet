package com.netcommlabs.sarofficenet.model;

public class CompOffModel {
    private String AvailedDays;
    private String Balance;
    private String CompOffDate;
    private String CompOffID;
    private String NoOfDays;
    private String ReqDate;

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    private boolean isChecked=false;
    public String getAvailedDays() {
        return AvailedDays;
    }

    public void setAvailedDays(String availedDays) {
        AvailedDays = availedDays;
    }

    public String getBalance() {
        return Balance;
    }

    public void setBalance(String balance) {
        Balance = balance;
    }

    public String getCompOffDate() {
        return CompOffDate;
    }

    public void setCompOffDate(String compOffDate) {
        CompOffDate = compOffDate;
    }

    public String getCompOffID() {
        return CompOffID;
    }

    public void setCompOffID(String compOffID) {
        CompOffID = compOffID;
    }

    public String getNoOfDays() {
        return NoOfDays;
    }

    public void setNoOfDays(String noOfDays) {
        NoOfDays = noOfDays;
    }

    public String getReqDate() {
        return ReqDate;
    }

    public void setReqDate(String reqDate) {
        ReqDate = reqDate;
    }
}
