package com.bioverflow.dobermann.client;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.support.annotation.RequiresApi;
import android.widget.Toast;

@SuppressLint("OverrideAbstract")
@RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
public class NotificationCatcher extends NotificationListenerService {
    public NotificationCatcher() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return super.onBind(intent);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onNotificationPosted(StatusBarNotification sbn){
        try{
            Bundle extras = sbn.getNotification().extras;
            String notificationTitle = extras.getString(Notification.EXTRA_TITLE);
            CharSequence notificationText = extras.getCharSequence(Notification.EXTRA_TEXT);
            CharSequence notificationSubText = extras.getCharSequence(Notification.EXTRA_SUB_TEXT);

            if(notificationSubText != null) {
                showToast(notificationTitle + ": " + notificationText + ". " + notificationSubText);
            }
            else{
                showToast(notificationTitle + ": " + notificationText);
            }
        }
        catch (Exception e){
            showToast("Exception: " + e.getMessage());
        }
    }

    void showToast(String message){
        Context context = getApplicationContext();
        CharSequence text = message;
        int duration = Toast.LENGTH_LONG;

        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
    }
}
