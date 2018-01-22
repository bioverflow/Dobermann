package com.bioverflow.dobermann.client;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.Random;

/**
 * Created by camie on 1/20/2018.
 */

public class ClientSettings {
    private String Name;
    private String ID;
    private String Email;
    private SharedPreferences sharedPreferences;
    private Context _context;

    public ClientSettings(){}

    public ClientSettings(Context context){
        this._context = context;
        getClientSettingsFromStoredData();

        this.Name = getNameFromStoredData();
    }

    public  ClientSettings(Context context, String clientName){
        this._context = context;
        getClientSettingsFromStoredData();

        if(clientName.length() > 0){
            this.Name = clientName;
        }
        else{
            this.Name = getNameFromStoredData();
        }
    }

    private void getClientSettingsFromStoredData(){
        this.sharedPreferences = this._context.getSharedPreferences("ClientSettings", this._context.MODE_PRIVATE);
    }

    public String getName(){
        return this.Name;
    }

    public String getNameFromStoredData(){
        return this.sharedPreferences.getString("Name", "DEFAULT");
    }

    public  void setNameFromStoredData(String name){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("Name", name);
        editor.commit();
    }

    private String randomClientName() {
        Random generator = new Random();
        StringBuilder randomStringBuilder = new StringBuilder();
        int randomLength = generator.nextInt(256);
        char tempChar;
        for (int i = 0; i < randomLength; i++){
            tempChar = (char) (generator.nextInt(96) + 32);
            randomStringBuilder.append(tempChar);
        }
        return randomStringBuilder.toString();
    }
}
