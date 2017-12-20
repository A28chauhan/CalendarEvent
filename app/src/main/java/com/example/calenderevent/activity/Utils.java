package com.example.calenderevent.activity;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

import com.example.calenderevent.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Created by Vivek on 15-12-2017.
 */

public class Utils {


    public static void generateNotification(Context context, String message) {

        NotificationManager mNM = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
        // The PendingIntent to launch our activity if the user selects this notification
        Intent pIntent =new Intent(context, CalenderEvent.class);
        pIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK| Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent contentIntent = PendingIntent.getActivity(context, 0,pIntent, 0);

        //Assign inbox style notification
        NotificationCompat.BigTextStyle bigText = new NotificationCompat.BigTextStyle();
        bigText.bigText(message);
        bigText.setBigContentTitle("Calender Notification");
        bigText.setSummaryText("Working Notification !");

        // Set the info for the views that show in the notification panel.
        NotificationCompat.Builder notification = new NotificationCompat.Builder(context)
                .setSmallIcon(R.mipmap.ic_launcher)  // the status icon
                .setContentIntent(contentIntent)  // The intent to send when the entry is clicked
                .setStyle(bigText);

        // Send the notification.
        mNM.notify(Utils.getNextId(context), notification.build());

    }

    public static int getNextId(Context ctx) {
        int nextId = 1;
       // nextId = PrefUtil.getIntPreferenceValue(ctx, PrefConstants.UNIQUE_NOTIFY_ID);
        //reset back after it reaches 2000 so that it does not overflow
        if(nextId == 2000) {
            nextId = 1;
        }
        nextId++;
        //PrefUtil.setPreferenceValue(ctx, PrefConstants.UNIQUE_NOTIFY_ID, nextId);

        return nextId;
    }



}
