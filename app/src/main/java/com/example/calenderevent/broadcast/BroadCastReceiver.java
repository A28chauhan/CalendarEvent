package com.example.calenderevent.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.example.calenderevent.activity.Utils;

/**
 * Created by Vivek on 20-12-2017.
 */

public class BroadCastReceiver extends BroadcastReceiver {


    @Override
    public void onReceive(Context context, Intent intent) {

        Utils.generateNotification(context,"Its time to take meditation");
    }
}
