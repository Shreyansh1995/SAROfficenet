package com.netcommlabs.sarofficenet.utils;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;

import com.netcommlabs.sarofficenet.activity.FrameActivity;

/**
 * Created by Flash_Netcomm on 2/16/2019.
 */

public class AppAlertDialog {
    private static AlertDialog.Builder builder;


    public static void showDialogSelfFinish(Context tmContext, String Title, String Msg) {
        builder = new AlertDialog.Builder(tmContext);
        builder.setTitle(Title).setMessage(Msg)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                })
                .show();
        // FragmentTransections.popFragment(tmContext);
    }

    public static void showDialogFinishWithActivity(final Context tmContext, String Title, String Msg) {
        builder = new AlertDialog.Builder(tmContext);
        builder.setTitle(Title).setMessage(Msg)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        ((Activity) tmContext).finish();
                    }
                })
                .show();
    }

    public static void showDialogOnSuccess(final Context tmContext, String message, final String FragmentName, final String FragmentTag, final String FragmentTitle, final String Tab) {
        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(tmContext);
        builder.setTitle("").setMessage(message)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        Intent intent = new Intent(tmContext, FrameActivity.class);
                        intent.putExtra("frag_name", FragmentName);
                        intent.putExtra("frag_tag", FragmentTag);
                        intent.putExtra("title", FragmentTitle);
                        intent.putExtra("tab", Tab);
                        tmContext.startActivity(intent);
                    }
                })
                .show();
    }


    public static void showDialogFinish(Context tmContext, String Msg) {
        builder = new AlertDialog.Builder(tmContext);
        builder.setMessage(Msg)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                })
                .show();
    }

}
