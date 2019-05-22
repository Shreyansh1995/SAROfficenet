package com.netcommlabs.sarofficenet.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class HelpDeskAdminListModel {
    @SerializedName("ActionDate")
    @Expose
    private String actionDate;
    @SerializedName("Category")
    @Expose
    private String category;
    @SerializedName("MobileNo")
    @Expose
    private String mobileNo;
    @SerializedName("Query")
    @Expose
    private String query;
    @SerializedName("ReqID")
    @Expose
    private String reqID;
    @SerializedName("Status")
    @Expose
    private String status;
    @SerializedName("SubCategory")
    @Expose
    private String subCategory;
    @SerializedName("SubmitDate")
    @Expose
    private String submitDate;
    @SerializedName("SubmittedBy")
    @Expose
    private String submittedBy;
    @SerializedName("TicketHolder")
    @Expose
    private String ticketHolder;
    @SerializedName("TicketNo")
    @Expose
    private String ticketNo;

    public String getActionDate() {
        return actionDate;
    }

    public void setActionDate(String actionDate) {
        this.actionDate = actionDate;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
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

    public String getSubmitDate() {
        return submitDate;
    }

    public void setSubmitDate(String submitDate) {
        this.submitDate = submitDate;
    }

    public String getSubmittedBy() {
        return submittedBy;
    }

    public void setSubmittedBy(String submittedBy) {
        this.submittedBy = submittedBy;
    }

    public String getTicketHolder() {
        return ticketHolder;
    }

    public void setTicketHolder(String ticketHolder) {
        this.ticketHolder = ticketHolder;
    }

    public String getTicketNo() {
        return ticketNo;
    }

    public void setTicketNo(String ticketNo) {
        this.ticketNo = ticketNo;
    }

}
