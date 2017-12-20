package com.example.calenderevent.activity;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.nfc.Tag;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.calenderevent.R;
import com.example.calenderevent.preference.PrefConstant;
import com.example.calenderevent.preference.PrefManager;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * Created by Vivek on 19-12-2017.
 */

public class CalenderEvent extends AppCompatActivity implements View.OnClickListener {


    public static String TAG=CalenderEvent.class.getName();

    @BindView(R.id.button_sheet)
    TextView buttonSheets;

    @BindView(R.id.bottom_sheet)
    LinearLayout layoutBottomSheet;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    /*set Bottom sheet component slides*/
    BottomSheetBehavior bottomSheetBehavior;

    // set view in android custom
    @BindView(R.id.time_picker)
    TextView textview;

    // set switch in view
    @BindView(R.id.remind_switch)
    Switch switchCompat;

    @BindView(R.id.put_switch)
    Switch switchCompatCalender;

    //radio group set
    @BindView(R.id.radio_group)
    RadioGroup radioGroup;

    @BindView(R.id.radioButton1)
    RadioButton weekly_btn;

    @BindView(R.id.radioButton2)
    RadioButton weekday_btn;

    @BindView(R.id.radioButton3)
    RadioButton everyday_btn;

    @BindView(R.id.coordinator_layout)
    CoordinatorLayout coordinatorLayout;


    // Projection array. Creating indices for this array instead of doing
    // dynamic lookups improves performance.
    public static final String[] EVENT_PROJECTION = new String[] {
            CalendarContract.Calendars._ID,                           // 0
            CalendarContract.Calendars.ACCOUNT_NAME,                  // 1
            CalendarContract.Calendars.CALENDAR_DISPLAY_NAME,         // 2
            CalendarContract.Calendars.OWNER_ACCOUNT                  // 3
    };

    // The indices for the projection array above.
    private static final int PROJECTION_ID_INDEX = 0;
    private static final int PROJECTION_ACCOUNT_NAME_INDEX = 1;
    private static final int PROJECTION_DISPLAY_NAME_INDEX = 2;
    private static final int PROJECTION_OWNER_ACCOUNT_INDEX = 3;

    // static variable use the permission grant variable.
    private  static final int MY_PERMISSIONS_REQUEST_WRITE=1;
    private  static final int MY_PERMISSIONS_REQUEST_WRITE_REP=2;
    PrefManager prefManager;

    Calendar calendar;
    int CalendarHour,CalendarMinute;

    // 1st method call in onCreate method
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar_event);
        // library injection to bind all object data this library
        ButterKnife.bind(CalenderEvent.this);
        // set actionbar tool bar
       // setSupportActionBar(toolbar);

        prefManager = new PrefManager(this);
        //prefManager.setPreferenceValue(getApplicationContext(),PrefConstant.CALENDAR_EVENT_R_RULE,0);
        //prefManager.setPreferenceValue(getApplicationContext(),PrefConstant.PUT_TIME,(selectedHour + ":" + selectedMinute));

        String timeSet = prefManager.getPreferenceString(getApplicationContext(),PrefConstant.PUT_TIME);
        if(timeSet != null && !timeSet.equals("")){
            textview.setText(timeSet);

            // prefManager.setPreferenceValue(getApplicationContext(),PrefConstant.PUT_SEQUENCE,"On WeekDays");
            String sequence =prefManager.getPreferenceString(getApplicationContext(),PrefConstant.PUT_SEQUENCE);
            if(sequence != null && !sequence.equals("")) {
                buttonSheets.setText(sequence);
            }
            //prefManager.setPreferenceValue(getApplicationContext(),PrefConstant.PUT_CALENDAR,true);
            boolean calendar = prefManager.getPreferenceBoolean(getApplicationContext(),PrefConstant.PUT_CALENDAR);
            if(calendar){
                switchCompatCalender.setChecked(true);
            }else{
                switchCompatCalender.setChecked(false);
            }
        }

        bottomSheetBehavior = BottomSheetBehavior.from(layoutBottomSheet);
        bottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                switch (newState) {
                    case BottomSheetBehavior.STATE_HIDDEN:
                        break;
                    case BottomSheetBehavior.STATE_EXPANDED: {
                        //buttonSheets.setText("Close Sheet");
                    }
                    break;
                    case BottomSheetBehavior.STATE_COLLAPSED: {
                        //buttonSheets.setText("Expand Sheet");
                        //setBottomSheetRecord();
                    }
                    break;
                    case BottomSheetBehavior.STATE_DRAGGING:
                        break;
                    case BottomSheetBehavior.STATE_SETTLING:
                        break;
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {

            }
        });


        // switch compact on click call
        switchCompatCalender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String time_pick= textview.getText().toString();
                if(time_pick.equals("Set Time")){
                    Toast.makeText(CalenderEvent.this,"Please select put it calendar",Toast.LENGTH_SHORT).show();
                    switchCompatCalender.setChecked(false);
                }else {
                    Boolean switchState = switchCompatCalender.isChecked();
                    if (switchState) {
                        //setCalenderData();
                        prefManager.setPreferenceValue(getApplicationContext(), PrefConstant.PUT_CALENDAR, true);
                        initAndroidPermissionCheck();
                    } else {
                        initAndroidPermissionCheckAgain();
                    }
                }

            }
        });


        calendar = Calendar.getInstance();
        CalendarHour = calendar.get(Calendar.HOUR_OF_DAY);
        CalendarMinute = calendar.get(Calendar.MINUTE);

        textview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(CalenderEvent.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        textview.setText(selectedHour + ":" + selectedMinute);
                        prefManager.setPreferenceValue(getApplicationContext(),PrefConstant.PUT_TIME,(selectedHour + ":" + selectedMinute));
                        if(switchCompatCalender.isChecked()){
                            CalendarHour =selectedHour;
                            CalendarMinute =selectedMinute;
                            initAndroidPermissionCheck();
                        }


                    }
                }, CalendarHour, CalendarMinute, false);//Yes 24 hour time
                //mTimePicker.setTitle("Select Time");
                mTimePicker.show();
            }
        });

    }

    //"FREQ=DAILY;COUNT=0;BYDAY=SA,SU"
    //Frequently : FREQ=DAILY;COUNT=0;BYDAY=MO,TU,WE,TH,FR;WKST=MO
    //Weekends : FREQ=DAILY;COUNT=0;BYDAY=SA,SU          or           FREQ=WEEKLY;COUNT=20;WKST=SU
    //Weekday :  FREQ=DAILY;COUNT=0;BYDAY=MO,TU,WE,TH,FR;WKST=MO
    // Daily : FREQ=DAILY;COUNT=0;BYDAY=SU,MO,TU,WE,TH,FR,SA    .....  FREQ=DAILY;COUNT=0;BYDAY=SU,MO,TU,WE,TH,FR,SA
    // same day : FREQ=WEEKLY;COUNT=10;WKST=SU
    //FREQ=DAILY;UNTIL=19971224T000000Z
    String ruleId[] = {"FREQ=DAILY;COUNT=0;BYDAY=SU,MO,TU,WE,TH,FR,SA","FREQ=DAILY;COUNT=0;BYDAY=SA,SU","FREQ=DAILY;COUNT=0;BYDAY=MO,TU,WE,TH,FR;WKST=MO"};

    public void setBottomSheetRecord(){

        switch (radioGroup.getId()){
            case R.id.radioButton1:
                prefManager.setPreferenceValue(getApplicationContext(),PrefConstant.CALENDAR_EVENT_R_RULE,ruleId[0]);
                if(switchCompatCalender.isChecked()){
                    initAndroidPermissionCheck();
                }
                break;
            case R.id.radioButton2:
                prefManager.setPreferenceValue(getApplicationContext(),PrefConstant.CALENDAR_EVENT_R_RULE,ruleId[1]);
                if(switchCompatCalender.isChecked()){
                    initAndroidPermissionCheck();
                }
                break;

            case R.id.radioButton3:
                prefManager.setPreferenceValue(getApplicationContext(),PrefConstant.CALENDAR_EVENT_R_RULE,ruleId[2]);
                if(switchCompatCalender.isChecked()){
                    initAndroidPermissionCheck();
                }
                break;

            default:
                break;
        }
        // prefManager.setPreferenceValue(getApplicationContext(),PrefConstant.CALENDAR_EVENT_R_RULE,0);


    }


    @Override
    public void onClick(View v) {

    }

    @OnClick(R.id.button_sheet)
    public void toggleBottomSheet() {
        if (bottomSheetBehavior.getState() != BottomSheetBehavior.STATE_EXPANDED) {
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
            //buttonSheets.setText("Close sheet");
        } else {
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            //buttonSheets.setText("Expand sheet");
            String sequence =prefManager.getPreferenceString(getApplicationContext(),PrefConstant.PUT_SEQUENCE);
            if(sequence != null && !sequence.equals("")) {
                buttonSheets.setText(sequence);
            }
        }
    }




    // Permission required...
    public void initAndroidPermissionCheck(){
        // check self permission in write calendar ...
        if ((ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_CALENDAR) != PackageManager.PERMISSION_GRANTED)  ) {
            // Should we show an explanation ..
            // Show an explanation to the user *asynchronously* -- don't block
            // this thread waiting for the user's response! After the user
            // sees the explanation, try again to request the permission.
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_CALENDAR)) {
                // request permission check
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_CALENDAR}, MY_PERMISSIONS_REQUEST_WRITE);
            }else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_CALENDAR}, MY_PERMISSIONS_REQUEST_WRITE);
            }

        }else{
            setCalenderData();

        }

    }

    // check permission and show result you grant permission or not.
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_WRITE : {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    onPermissionGranted();
                }else{
                    switchCompatCalender.setChecked(false);
                }
            }
            break;
            case MY_PERMISSIONS_REQUEST_WRITE_REP: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    deleteCalendarData();
                }else{
                    switchCompatCalender.setChecked(true);
                }
            }break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
                // other 'case' lines to check for other
                // permissions this app might request
        }
    }

    // if permission granted by user then ..
    private void onPermissionGranted() {
        boolean bValue = prefManager.getPreferenceBoolean(getApplicationContext(),PrefConstant.CALENDAR_EVENT_UPDATE);
        prefManager.setPreferenceValue(getApplicationContext(),PrefConstant.CALENDAR_EVENT_R_RULE,0);
        if(bValue){
            setCalenderUpdate();
        }else {
            // custom method call
            setCalenderData();
        }
    }

    String[] selectionArgs = new String[] {"ankit007chauhan@gmail.com", "com.google","ankit007chauhan@gmail.com"};
    Cursor cur = null;
    ContentResolver cr;
    long calID = 0;
    String displayName = null;
    String accountName = null;
    String ownerName = null;

    // set the value of calender event ..
    public void setCalenderData(){
        // get mail id to given table
       // String email = UserEmailFetcher.getEmail(CalenderEvent.this);
        String email ="ankit007chauhan@gmail.com";
        //String email = UserEmailFetcher.getGoogleAccountName(CalenderEvent.this);
        if(email != null){
            selectionArgs = new String[] {email, "com.google",email};
        }
        // if email is null then nothing to show
        if(email == null && email.equalsIgnoreCase("")){
            email = "";
            Toast.makeText(getApplicationContext(),"No email found",Toast.LENGTH_SHORT).show();
            Log.d("calendar event"," hardcode generated email : "+email);
        }else{
        // Run query
        cr = getContentResolver();
        Uri uri = CalendarContract.Calendars.CONTENT_URI;
        String selection = "((" + CalendarContract.Calendars.ACCOUNT_NAME + " = ?) AND ("
                + CalendarContract.Calendars.ACCOUNT_TYPE + " = ?) AND ("
                + CalendarContract.Calendars.OWNER_ACCOUNT + " = ?))";

        if ((ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_CALENDAR) == PackageManager.PERMISSION_GRANTED)  ) {
            // Submit the query and get a Cursor object back.
            cur = cr.query(uri, EVENT_PROJECTION, selection, selectionArgs, null);

            while (cur.moveToNext()) {

                // Get the field values
                calID = cur.getLong(PROJECTION_ID_INDEX);
                displayName = cur.getString(PROJECTION_DISPLAY_NAME_INDEX);
                accountName = cur.getString(PROJECTION_ACCOUNT_NAME_INDEX);
                ownerName = cur.getString(PROJECTION_OWNER_ACCOUNT_INDEX);

                prefManager.setPreferenceValue(getApplicationContext(), PrefConstant.CALENDAR_EVENT_DEFAULT,calID);
                addEventCalender(calID);
                Log.d("calender vlaue :",calID+" : "+displayName+" : "+accountName+" : "+ownerName);
            }
        }else{
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_CALENDAR}, MY_PERMISSIONS_REQUEST_WRITE);
        }
        }
    }

    //  Event daily use then 10 Occurrences. example:  FREQ=WEEKLY;COUNT=10;WKST=SU
    //  Every other day - forever:  RRULE : FREQ=DAILY;INTERVAL=2
    public void addEventCalender(long calID){
        // set time zone
        String timeZone = TimeZone.getDefault().getID();

        long startMillis = 0;
        long endMillis = 0;
        Calendar beginTime = Calendar.getInstance();
        //beginTime.set(2017, 11, 18, 19, 00);
        beginTime.add(Calendar.HOUR ,CalendarHour);
        beginTime.add(Calendar.MINUTE ,CalendarMinute);
        //beginTime.add(Calendar.SECOND ,00);
        startMillis = beginTime.getTimeInMillis();
        /*Calendar endTime = Calendar.getInstance();
        endTime.set(2017, 11, 18, 19, 10);
        endMillis = endTime.getTimeInMillis();*/

        ContentResolver cr = getContentResolver();
        ContentValues values = new ContentValues();

        //values.put(CalendarContract.Calendars.ACCOUNT_NAME,selectionArgs[0]);
        //values.put(CalendarContract.Calendars.ACCOUNT_TYPE,CalendarContract.ACCOUNT_TYPE_LOCAL);
        values.put(CalendarContract.Events.CALENDAR_ID, calID);
        values.put(CalendarContract.Events.TITLE, "Blip Event");
        values.put(CalendarContract.Events.DESCRIPTION, "Group workout");
        values.put(CalendarContract.Events.DTSTART, startMillis);
        //values.put(CalendarContract.Events.DTEND, endMillis);
        // values.put(CalendarContract.Events.EVENT_TIMEZONE, "America/Los_Angeles");
        if(timeZone != null){
            values.put(CalendarContract.Events.EVENT_TIMEZONE, timeZone);
        }
        values.put(CalendarContract.Events.EVENT_LOCATION, "New Delhi");
        values.put(CalendarContract.Events.ALL_DAY, "0");
        //values.put(CalendarContract.Events.AVAILABILITY, "0");
        values.put(CalendarContract.Events.GUESTS_CAN_INVITE_OTHERS, "1");
        values.put(CalendarContract.Events.GUESTS_CAN_SEE_GUESTS, "1");

        // PT1H0M0S  : its meaning 1 Hours 0 Mint 0 second
        // PT10M  : its meaning in 10 mint to scheduling
        // PT15M  : its meaning in 15 mint to scheduling.
        values.put(CalendarContract.Events.DURATION,"PT10M");

       /* if(rRule != 0){
            values.put(CalendarContract.Events.RRULE,ruleId[rRule]);
        }else{
            values.put(CalendarContract.Events.RRULE,"FREQ=DAILY;COUNT=0;BYDAY=MO,TU,WE,TH,FR;WKST=MO");
        }*/
        //"FREQ=DAILY;COUNT=0;BYDAY=SA,SU"
        //Frequently : FREQ=DAILY;COUNT=0;BYDAY=MO,TU,WE,TH,FR;WKST=MO
        //Weekends : FREQ=DAILY;COUNT=0;BYDAY=SA,SU          or           FREQ=WEEKLY;COUNT=20;WKST=SU
        //Weekday :  FREQ=DAILY;COUNT=0;BYDAY=MO,TU,WE,TH,FR;WKST=MO
        // Daily : FREQ=DAILY;COUNT=0;BYDAY=SU,MO,TU,WE,TH,FR,SA    .....  FREQ=DAILY;COUNT=0;BYDAY=SU,MO,TU,WE,TH,FR,SA
        // same day : FREQ=WEEKLY;COUNT=10;WKST=SU
        //FREQ=DAILY;UNTIL=19971224T000000Z
        int rRule = prefManager.getPreferenceInt(CalenderEvent.this,PrefConstant.CALENDAR_EVENT_R_RULE);
        values.put(CalendarContract.Events.RRULE,ruleId[rRule]);


        if ((ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_CALENDAR) == PackageManager.PERMISSION_GRANTED)  ) {
            Uri uri = cr.insert(CalendarContract.Events.CONTENT_URI, values);

            // get the event ID that is the last element in the Uri
            long eventID = Long.parseLong(uri.getLastPathSegment());
            // set preference value in event id..
            prefManager.setPreferenceValue(getApplicationContext(), PrefConstant.CALENDAR_EVENT_ID,eventID);
            //attendance inserting method.
            //attendanceInsertTable(eventID);
            Snackbar snackbar =Snackbar.make(coordinatorLayout,"Saved..",Snackbar.LENGTH_SHORT);
            snackbar.show();

            Toast.makeText(getApplicationContext(),"note insert",Toast.LENGTH_SHORT).show();
            Log.i(TAG, "Rows updated: " + eventID);
        }
    }


    public void initAndroidPermissionCheckAgain(){

        // if permission check details
        if ((ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_CALENDAR) != PackageManager.PERMISSION_GRANTED)  ) {
            // Should we show an explanation ..
            // Show an explanation to the user *asynchronously* -- don't block
            // this thread waiting for the user's response! After the user
            // sees the explanation, try again to request the permission.
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_CALENDAR)) {
                // request permission check
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_CALENDAR}, MY_PERMISSIONS_REQUEST_WRITE);
            }else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_CALENDAR}, MY_PERMISSIONS_REQUEST_WRITE);
            }
        }else{
            deleteCalendarData();
        }
    }

    public void deleteCalendarData(){

        long eventID = prefManager.getPreferenceLong(getApplicationContext(),PrefConstant.CALENDAR_EVENT_ID);

        ContentResolver cr = getContentResolver();
        ContentValues values = new ContentValues();
        Uri deleteUri = null;
        deleteUri = ContentUris.withAppendedId(CalendarContract.Events.CONTENT_URI, eventID);
        int rows = getContentResolver().delete(deleteUri, null, null);
        if(rows != 0){
            Snackbar snackbar =Snackbar.make(coordinatorLayout,"Saved..",Snackbar.LENGTH_SHORT);
            snackbar.show();

        }
        Log.i(TAG, "Rows deleted: " + rows);

    }




    /// 1st click
    public void everyDayClick(View v){
        // 0 set
        prefManager.setPreferenceValue(getApplicationContext(),PrefConstant.CALENDAR_EVENT_R_RULE,0);
        if(switchCompatCalender.isChecked()){
            //setCalenderData();
            prefManager.setPreferenceValue(getApplicationContext(),PrefConstant.CALENDAR_EVENT_UPDATE,true);
            initAndroidPermissionUpdate();
        }else{}
        prefManager.setPreferenceValue(getApplicationContext(),PrefConstant.PUT_SEQUENCE,"EveryDays");
        toggleBottomSheet();

    }
    // 2nd click
    public void weekendClick(View v){
        // 1 set
        prefManager.setPreferenceValue(getApplicationContext(),PrefConstant.CALENDAR_EVENT_R_RULE,1);
        if(switchCompatCalender.isChecked()){
            //setCalenderData();
            prefManager.setPreferenceValue(getApplicationContext(),PrefConstant.CALENDAR_EVENT_UPDATE,true);
            initAndroidPermissionUpdate();
        }else{}
        prefManager.setPreferenceValue(getApplicationContext(),PrefConstant.PUT_SEQUENCE,"On Weekends");
        toggleBottomSheet();
    }
    // 3rd click
    public void weekDayClick(View v){
        // 2 set
        prefManager.setPreferenceValue(getApplicationContext(),PrefConstant.CALENDAR_EVENT_R_RULE,2);
        if(switchCompatCalender.isChecked()){
            //setCalenderData();
            prefManager.setPreferenceValue(getApplicationContext(),PrefConstant.CALENDAR_EVENT_UPDATE,true);
            initAndroidPermissionUpdate();
        }else{}
        prefManager.setPreferenceValue(getApplicationContext(),PrefConstant.PUT_SEQUENCE,"On WeekDays");
        toggleBottomSheet();
    }

    public void initAndroidPermissionUpdate(){
        // check self permission in write calendar ...
        if ((ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_CALENDAR) != PackageManager.PERMISSION_GRANTED)  ) {
            // Should we show an explanation ..
            // Show an explanation to the user *asynchronously* -- don't block
            // this thread waiting for the user's response! After the user
            // sees the explanation, try again to request the permission.
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_CALENDAR)) {
                // request permission check
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_CALENDAR}, MY_PERMISSIONS_REQUEST_WRITE);
            }else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_CALENDAR}, MY_PERMISSIONS_REQUEST_WRITE);
            }

        }else{
            setCalenderUpdate();
        }
    }

    public void setCalenderUpdate(){
        /*long calID = prefManager.getPreferenceLong(getApplicationContext(),PrefConstant.CALENDAR_EVENT_ID);
        ContentValues values = new ContentValues();
        // The new display name for the calendar
        int rRule = prefManager.getPreferenceInt(getApplicationContext(),PrefConstant.CALENDAR_EVENT_R_RULE);
        values.put(CalendarContract.Events.RRULE,ruleId[rRule]);
        values.put(CalendarContract.Calendars.CALENDAR_DISPLAY_NAME, "Blip Calendar");
        Uri updateUri = ContentUris.withAppendedId(CalendarContract.Calendars.CONTENT_URI, calID);
        int rows = getContentResolver().update(updateUri, values, null, null);
        Log.i(TAG, "Rows updated: " + rows);*/

        long calID = prefManager.getPreferenceLong(getApplicationContext(),PrefConstant.CALENDAR_EVENT_ID);
        ContentResolver cr = getContentResolver();
        ContentValues values = new ContentValues();
        Uri updateUri = null;

        int rRule = prefManager.getPreferenceInt(getApplicationContext(),PrefConstant.CALENDAR_EVENT_R_RULE);
        values.put(CalendarContract.Events.RRULE,ruleId[rRule]);
        values.put(CalendarContract.Events.TITLE, "Kickboxing");
        updateUri = ContentUris.withAppendedId(CalendarContract.Events.CONTENT_URI, calID);
        int rows = getContentResolver().update(updateUri, values, null, null);

        if(rows != 0){
            Snackbar snackbar =Snackbar.make(coordinatorLayout,"Saved..",Snackbar.LENGTH_SHORT);
            snackbar.show();
        }
        Log.i(TAG, "Rows updated: " + rows);
    }
}



