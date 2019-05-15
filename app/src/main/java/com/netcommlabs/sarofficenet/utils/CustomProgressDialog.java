package com.netcommlabs.sarofficenet.utils;

import android.app.Dialog;
import android.content.Context;
import android.view.Window;
import android.view.WindowManager;

import com.netcommlabs.sarofficenet.R;

/**
 * Created by Flash_Netcomm on 2/16/2019.
 */

public class CustomProgressDialog {
    private static CustomProgressDialog object;
    private Dialog dialog;
    private Context mContext;


    public static CustomProgressDialog getInstance(Context mContext) {
        if (object != null) {
            return object;
        } else return new CustomProgressDialog(mContext);
    }


    public CustomProgressDialog(Context mContext) {
        this.mContext = mContext;
    }

    public void showProgressBar() {
        dialog = new Dialog(mContext);
        dialog.setCancelable(false);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.loader_layout);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;
        dialog.getWindow().setAttributes(lp);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.show();
    }

    public void hideProgressBar() {
        if (dialog != null)
            dialog.dismiss();
    }
}
