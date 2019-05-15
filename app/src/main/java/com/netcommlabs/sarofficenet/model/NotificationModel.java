package com.netcommlabs.sarofficenet.model;

public class NotificationModel {
    String Title,Body,Id;
    String IfRead;
    String DateTime;
    String ModuleID;

    public String getModuleID() {
        return ModuleID;
    }

    public void setModuleID(String moduleID) {
        ModuleID = moduleID;
    }

    public String getDateTime() {
        return DateTime;
    }

    public void setDateTime(String dateTime) {
        DateTime = dateTime;
    }

    public String getIfRead() {
        return IfRead;
    }

    public void setIfRead(String ifRead) {
        IfRead = ifRead;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getBody() {
        return Body;
    }

    public void setBody(String body) {
        Body = body;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }
}
