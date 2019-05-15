package com.netcommlabs.sarofficenet.model;

public class PendingRequestsModel {
    String ModuleID;
    String ModuleName;

    public String getModuleID() {
        return ModuleID;
    }

    public void setModuleID(String moduleID) {
        ModuleID = moduleID;
    }

    public String getModuleName() {
        return ModuleName;
    }

    public void setModuleName(String moduleName) {
        ModuleName = moduleName;
    }

    public String getPendingCount() {
        return PendingCount;
    }

    public void setPendingCount(String pendingCount) {
        PendingCount = pendingCount;
    }

    String PendingCount;
}
