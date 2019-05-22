package com.netcommlabs.sarofficenet.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class HelpDeskDetailsList {
    @SerializedName("Category")
    @Expose
    private String category;
    @SerializedName("Department")
    @Expose
    private String department;
    @SerializedName("Issue")
    @Expose
    private String issue;
    @SerializedName("ReqID")
    @Expose
    private String reqID;
    @SerializedName("Status")
    @Expose
    private String status;
    @SerializedName("SubCategory")
    @Expose
    private String subCategory;
    @SerializedName("SubmissionDate")
    @Expose
    private String submissionDate;
    @SerializedName("TicketNo")
    @Expose
    private String ticketNo;

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getIssue() {
        return issue;
    }

    public void setIssue(String issue) {
        this.issue = issue;
    }

    public String getReqID() {
        return reqID;
    }

    public void setReqID(String reqID) {
        this.reqID = reqID;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSubCategory() {
        return subCategory;
    }

    public void setSubCategory(String subCategory) {
        this.subCategory = subCategory;
    }

    public String getSubmissionDate() {
        return submissionDate;
    }

    public void setSubmissionDate(String submissionDate) {
        this.submissionDate = submissionDate;
    }

    public String getTicketNo() {
        return ticketNo;
    }

    public void setTicketNo(String ticketNo) {
        this.ticketNo = ticketNo;
    }
}
