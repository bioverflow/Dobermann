package com.bioverflow.dobermann.client;

import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private static final String ENABLED_NOTIFICATION_LISTENERS = "enabled_notification_listeners";
    private static final String ACTION_NOTIFICATION_LISTENER_SETTINGS = "android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS";

    private AlertDialog enableNotificationListenerAlertDialog;

    private ClientSettings clientSettings;

    private EditText clientNameInput;
    private Button setNameButton;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        startApp();
    }

    void startApp(){
        /// Show UI
        setContentView(R.layout.activity_main);

        this.context = this;

        /// UI definitions
        this.clientNameInput = (EditText)findViewById(R.id.client_name_input);
        this.setNameButton = findViewById(R.id.set_name_button);

        /// Client settings definition
        this.clientSettings = getClientSettings();
        this.clientNameInput.setText(this.clientSettings.getName());

        /// UI Event listeners
        this.setNameButton .setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                clientSettings = new ClientSettings(context, clientNameInput.getText().toString());
                clientSettings.setNameFromStoredData(clientNameInput.getText().toString());

                Utils.showToast(context, "Device name is saved");
            }
        });

        if(!isNotificationServiceEnabled()){
            enableNotificationListenerAlertDialog = buildNotificationServiceAlertDialog();
            enableNotificationListenerAlertDialog.show();
        }
    }

    ClientSettings getClientSettings(){
        return new ClientSettings(this);
    }

    /**
     * Is Notification Service Enabled.
     * Verifies if the notification listener service is enabled.
     * Got it from: https://github.com/kpbird/NotificationListenerService-Example/blob/master/NLSExample/src/main/java/com/kpbird/nlsexample/NLService.java
     * @return True if eanbled, false otherwise.
     */
    private boolean isNotificationServiceEnabled(){
        String pkgName = getPackageName();
        final String flat = Settings.Secure.getString(getContentResolver(),
                ENABLED_NOTIFICATION_LISTENERS);
        if (!TextUtils.isEmpty(flat)) {
            final String[] names = flat.split(":");
            for (int i = 0; i < names.length; i++) {
                final ComponentName cn = ComponentName.unflattenFromString(names[i]);
                if (cn != null) {
                    if (TextUtils.equals(pkgName, cn.getPackageName())) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * Build Notification Listener Alert Dialog.
     * Builds the alert dialog that pops up if the user has not turned
     * the Notification Listener Service on yet.
     * @return An alert dialog which leads to the notification enabling screen
     */
    private AlertDialog buildNotificationServiceAlertDialog(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle("Enable notifications listener");
        alertDialogBuilder.setMessage("If you no set this settings you cannot use core of this app, if you enable this app and show this message dismiss it.");
        alertDialogBuilder.setPositiveButton("yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        startActivity(new Intent(ACTION_NOTIFICATION_LISTENER_SETTINGS));
                        startApp();
                    }
                });
        alertDialogBuilder.setNegativeButton("no",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        startApp();
                    }
                });
        return(alertDialogBuilder.create());
    }

}
