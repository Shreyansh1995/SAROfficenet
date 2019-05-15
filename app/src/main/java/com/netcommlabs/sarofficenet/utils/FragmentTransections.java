package com.netcommlabs.sarofficenet.utils;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by Flash_Netcomm on 2/16/2019.
 */

public class FragmentTransections {
    public static void addFragment(Context context, Fragment fragment, int layout, String Tag) {
        FragmentManager fm = ((AppCompatActivity) context).getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        boolean fragmentPopped = fm.popBackStackImmediate(Tag, 0);
        if (!fragmentPopped) {
            ft.addToBackStack(Tag);
            ft.add(layout, fragment, Tag).commit();
        }
    }

    public static void replaceFragmentKeepPrevious(Context context, Fragment fragment, int layout, String Tag) {
        FragmentManager fm = ((AppCompatActivity) context).getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        boolean fragmentPopped = fm.popBackStackImmediate(Tag, 0);
        if (!fragmentPopped) {
            ft.replace(layout, fragment, Tag);
            ft.addToBackStack(Tag);
            ft.commit();
        }
    }


    public static void replaceFragmentRemovePrevious(Context context, Fragment fragment, int layout, String Tag) {
        FragmentManager fm = ((AppCompatActivity) context).getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        fm.popBackStack();
        ft.replace(layout, fragment, Tag);
        ft.addToBackStack(Tag);
        ft.commit();
    }

    public static void popFragment(Context context) {
        FragmentManager fm = ((AppCompatActivity) context).getSupportFragmentManager();
        fm.popBackStack();
    }
}
