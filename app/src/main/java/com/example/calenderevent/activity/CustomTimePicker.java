package com.example.calenderevent.activity;


import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.widget.DatePicker;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;

/**
 * Created by Vivek on 18-10-2017.
 */

public class CustomTimePicker extends  TimePickerDialog{

    public static final int TIME_PICKER_INTERVAL=15;
    private boolean mIgnoreEvent=false;


    public CustomTimePicker(Context context,OnTimeSetListener callBack, int hourOfDay, int minute, boolean is24HourView){
       super(context,callBack,hourOfDay,minute,is24HourView);
    }

    @Override
    public void onTimeChanged(TimePicker timePicker, int hourOfDay, int minute) {
        super.onTimeChanged(timePicker, hourOfDay, minute);
        if (!mIgnoreEvent){
            minute = getRoundedMinute(minute);
            mIgnoreEvent=true;
            timePicker.setCurrentMinute(minute);
            mIgnoreEvent=false;
        }

    }
    public static  int getRoundedMinute(int minute){
        if(minute % TIME_PICKER_INTERVAL != 0){
            int minuteFloor = minute - (minute % TIME_PICKER_INTERVAL);
            minute = minuteFloor + (minute == minuteFloor + 1 ? TIME_PICKER_INTERVAL : 0);
            if (minute == 60)  minute=0;
        }
        return minute;
    }


}

/*public class CustomTimePicker {

    Context mContext;
    TimePickerDialog mTimePickerDialog;
    onTimeSet onTimeSet;

    public void setTimeListener(onTimeSet onTimeset) {
        onTimeSet = onTimeset;
    }

    public CustomTimePicker(Context context){
        this.mContext= context;
        mTimePickerDialog = new TimePickerDialog(context, new TimePickerDialog.OnTimeSetListener() {

            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                onTimeSet.onTime(view, hourOfDay, minute);

            }
        }, 1, 1, true);
    }

    public interface onTimeSet{
        public void onTime(TimePicker timePicker, int hourOfDays , int hourOfMint);
    }

    public void show(){
        mTimePickerDialog.show();
    }

}*/
/*public class CustomDatePicker extends DialogFragment implements DatePickerDialog.OnDateSetListener{

    String type;

    private static final String DATE_TO_DIALOG = "dateToDialog";
    private static final String DATE_FROM_DIALOG = "dateFromDialog";
    private static final String DIALOG_TYPE = "DialogType";

    private String dateString;
    private long dateMillis;

    private void setDateString(int year, int month, int day){
        dateString = new StringBuilder().append(day).append("/").append(month + 1).append("/").append(year).toString();
    }

    private void setDateMillis(int year, int month, int day){
        Calendar cal = Calendar.getInstance();
        cal.set(year, month, day);
        dateMillis = cal.getTimeInMillis();
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current date as the default date in the picker
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        type = getArguments().getString(DIALOG_TYPE);

        // Create a new instance of DatePickerDialog and return it
        return new DatePickerDialog(getActivity(), this, year, month, day);
    }

    public void onDateSet(DatePicker view, int year, int month, int day) {
        // Do something with the date chosen by the user

        Toast.makeText(getActivity(),day+"-"+month+"-"+year, Toast.LENGTH_SHORT).show();

        setDateString(year, month, day);
        setDateMillis(year, month, day);

        //DisplayBPChartFragment displayBPChartFragment=new DisplayBPChartFragment();
      //  displayBPChartFragment.fromDateSelection(view,type, dateString, dateMillis);


    }

}*/


