package com.bioverflow.dobermann.client;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by camie on 1/21/2018.
 */

public class Utils {
    public static void showToast(Context context, String message){
        CharSequence text = message;
        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
    }

    public static void postError(String message){

    }
}
