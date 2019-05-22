package com.netcommlabs.sarofficenet.reciever;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;

import java.util.Objects;

public class NetworkChangeReceiver extends BroadcastReceiver {
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onReceive(Context context, Intent intent) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = Objects.requireNonNull(cm).getActiveNetworkInfo();

        if (netInfo != null && netInfo.isConnected()) {
            // Do something

            ComponentName comp = new ComponentName(context.getPackageName(),  OnlineDataSendService.class.getName());
            intent.putExtra("context",context.toString());
            context.startService((intent.setComponent(comp)));

            Log.d("Network Available ", "Flag No 1");
        }
    }
}
