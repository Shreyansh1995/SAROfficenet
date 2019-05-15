package com.netcommlabs.sarofficenet.model;

public class LeaveBalanceModel {
    private String AvailedIn;

    public String getAvailedIn() {
        return AvailedIn;
    }

    public void setAvailedIn(String availedIn) {
        AvailedIn = availedIn;
    }

    public String getBalance() {
        return Balance;
    }

    public void setBalance(String balance) {
        Balance = balance;
    }

    public String getCarryForwarded() {
        return CarryForwarded;
    }

    public void setCarryForwarded(String carryForwarded) {
        CarryForwarded = carryForwarded;
    }

    public String getCreditedIn() {
        return CreditedIn;
    }

    public void setCreditedIn(String creditedIn) {
        CreditedIn = creditedIn;
    }

    public String getLeaveType() {
        return LeaveType;
    }

    public void setLeaveType(String leaveType) {
        LeaveType = leaveType;
    }

    public String getPendingForApproval() {
        return PendingForApproval;
    }

    public void setPendingForApproval(String pendingForApproval) {
        PendingForApproval = pendingForApproval;
    }

    private String Balance;
    private String CarryForwarded;
    private String CreditedIn;
    private String LeaveType;
    private String PendingForApproval;
}
