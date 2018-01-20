package com.bioverflow.dobermann.client;

import java.util.Date;

/**
 * Created by camie on 1/20/2018.
 */

public class NotificationEntity {
    public String PackageName;
    public String Title;
    public String Text;
    public String SubText;
    public Date CurrentTime;

    public NotificationEntity(){
        // Default constructor required for calls to DataSnapshot.getValue(NotificationEntity.class)
    }

    public  NotificationEntity(Date currentTime, String packageName, String title, String text, String subText){
        this.CurrentTime = currentTime;
        this.PackageName = packageName;
        this.Title = title;
        this.Text = text;
        this.SubText = subText;
    }
}
