package com.netcommlabs.sarofficenet.model;

/**
 * Created by Flash_Netcomm on 2/18/2019.
 */

public class TeamListModel {
    String Name;
    String Image;
    String EmpCode;

    public String getEmpCode() {
        return EmpCode;
    }

    public void setEmpCode(String empCode) {
        EmpCode = empCode;
    }

    public String getEmpDateOfJoining() {
        return EmpDateOfJoining;
    }

    public void setEmpDateOfJoining(String empDateOfJoining) {
        EmpDateOfJoining = empDateOfJoining;
    }

    String EmpDateOfJoining;

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }

    public String getDesignation() {
        return Designation;
    }

    public void setDesignation(String designation) {
        Designation = designation;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    String Designation;
    String Id;
}
