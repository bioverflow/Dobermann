package com.bioverflow.dobermann.client;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class DobermannBroadcastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        switch (intent.getAction()){
            case "android.intent.action.BOOT_COMPLETED":
                /// Start notification catcher service
                context.startService(new Intent(context, NotificationCatcherService.class));
                context.startService(new Intent(context, UpdateService.class));
                break;
            default:
                break;
        }
    }
}
