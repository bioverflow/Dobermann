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

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;

@SuppressLint("OverrideAbstract")
@RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
public class NotificationCatcher extends NotificationListenerService {
    FirebaseDatabase database;
    DatabaseReference notificationRef;

    public NotificationCatcher() {
        database = FirebaseDatabase.getInstance();
        notificationRef = database.getReference("Notifications");
    }

    @Override
    public IBinder onBind(Intent intent) {
        return super.onBind(intent);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onNotificationPosted(StatusBarNotification sbn){
        try{
            /// Getting notification information
            Bundle extras = sbn.getNotification().extras;

            /// Formatting only important information in an entity to save in database in future
            NotificationEntity notification = new NotificationEntity(MainActivity.clientSettings, Calendar.getInstance().getTime(), sbn.getPackageName(), extras.getString(Notification.EXTRA_TITLE), (extras.getCharSequence(Notification.EXTRA_TEXT)).toString(), extras.getCharSequence(Notification.EXTRA_SUB_TEXT)!= null ? (extras.getCharSequence(Notification.EXTRA_SUB_TEXT)).toString():"");

            // Saving in database
            this.notificationRef.push().setValue(notification);
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
