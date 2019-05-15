package com.netcommlabs.sarofficenet.model;

/**
 * Created by Flash_Netcomm on 2/19/2019.
 */

public class AbbreviationModel {
    String Full;

    public String getFull() {
        return Full;
    }

    public void setFull(String full) {
        Full = full;
    }

    public String getShort() {
        return Short;
    }

    public void setShort(String aShort) {
        Short = aShort;
    }

    public String getCount() {
        return Count;
    }

    public void setCount(String count) {
        Count = count;
    }

    public String getColor() {
        return Color;
    }

    public void setColor(String color) {
        Color = color;
    }

    String Short;
    String Count;
    String Color;
}
