package com.ydeng.popular_movies_app.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.provider.Settings;

public class ConnectionChecker implements ConnectionReceiver.ConnectionReceiverListener {
    private Activity activity;
    private Context mContext;

    public ConnectionChecker(Activity activity) {
        this.activity = activity;
        this.mContext = activity.getApplicationContext();
    }

    public boolean checkConnection() {
        if (!ConnectionReceiver.isConnected()) {
            showDialog();
            return false;
        }

        return true;
    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        if (!isConnected) {
            showDialog();
        } else {
            Intent intent = new Intent(mContext, activity.getClass());
            mContext.startActivity(intent);
        }
    }

    private void showDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setMessage("No internet connection. Please check your internet settings and try again.")
                .setCancelable(false)
                .setNeutralButton("Try again", (dialog, id)
                        -> {
                    Intent intent = new Intent(mContext, activity.getClass());
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    mContext.startActivity(intent);
                })
                .setNegativeButton("Check Internet", (dialog, id)
                        -> {
                    Intent intent = new Intent(Settings.ACTION_NETWORK_OPERATOR_SETTINGS);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    mContext.startActivity(intent);
                })
                .setPositiveButton("Quit", (dialog, id) -> activity.finish());
        AlertDialog alert = builder.create();
        alert.show();
    }
}
