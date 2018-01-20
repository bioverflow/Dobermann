package com.bioverflow.dobermann.client;

/**
 * Created by camie on 1/20/2018.
 */

public class ClientSettings {
    private String Name;
    private String ID;
    private String Email;

    public ClientSettings(){

    }

    public  ClientSettings(String clientName){
        this.Name = clientName;
    }

    public String getName(){
        return this.Name;
    }
}
