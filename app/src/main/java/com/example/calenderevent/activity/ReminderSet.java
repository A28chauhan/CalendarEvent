package com.example.calenderevent.activity;

import android.Manifest;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.provider.CalendarContract.Attendees;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.example.calenderevent.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimeZone;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Vivek on 15-12-2017.
 */

public class ReminderSet extends AppCompatActivity {


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

    private  static final int MY_PERMISSIONS_REQUEST_WRITE=1;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reminder);

        ButterKnife.bind(ReminderSet.this);

        setSupportActionBar(toolbar);


        bottomSheetBehavior = BottomSheetBehavior.from(layoutBottomSheet);
        bottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {

                switch (newState) {
                    case BottomSheetBehavior.STATE_HIDDEN:
                        break;
                    case BottomSheetBehavior.STATE_EXPANDED: {
                        buttonSheets.setText("Close Sheet");
                    }
                    break;
                    case BottomSheetBehavior.STATE_COLLAPSED: {
                        buttonSheets.setText("Expand Sheet");
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




        switchCompatCalender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Boolean switchState = switchCompatCalender.isChecked();

                if(switchState){
                    //setCalenderData();
                    initAndroidPermissionCheck();
                }else{

                }
            }
        });



    }


    @OnClick(R.id.button_sheet)
    public void toggleBottomSheet() {
        if (bottomSheetBehavior.getState() != BottomSheetBehavior.STATE_EXPANDED) {
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
            buttonSheets.setText("Close sheet");
        } else {
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            buttonSheets.setText("Expand sheet");
        }
    }

    // switch calender set value..
    public void onclickCalender(){
        // set calendar value
        //setCalenderData();
    }

    // Data calender set...
    public void initAndroidPermissionCheck(){


        if ((ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_CALENDAR) != PackageManager.PERMISSION_GRANTED)  ) {
            // Submit the query and get a Cursor object back.


            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_CALENDAR)) {

                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_CALENDAR}, MY_PERMISSIONS_REQUEST_WRITE);
            }else {
                //setCalenderData();
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_CALENDAR}, MY_PERMISSIONS_REQUEST_WRITE);
            }
            // Show an explanation to the user *asynchronously* -- don't block
            // this thread waiting for the user's response! After the user
            // sees the explanation, try again to request the permission.
            //SurveyLogs.d(TAG, "shouldShowRequestPermissionRationale in if");
            // showPermissionRationale(MainActivity.this);
            // showPermissionRationale();


        }else{
            //ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_CALENDAR}, MY_PERMISSIONS_REQUEST_WRITE);
            setCalenderData();
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {

        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_WRITE : {

                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    onPermissionGranted();
                } /*else {
                    onAndroidPermissionDenied();
                }*/
            }
            break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
                // other 'case' lines to check for other
                // permissions this app might request
        }
    }

    private void onPermissionGranted() {
        setCalenderData();

    }

    private void onAndroidPermissionDenied() {

        initAndroidPermissionCheck();
    }

    String[] selectionArgs = new String[] {"ankit007chauhan@gmail.com", "com.google","ankit007chauhan@gmail.com"};
    public void setCalenderData(){
        // Run query
        Cursor cur = null;
        ContentResolver cr = getContentResolver();
        Uri uri = CalendarContract.Calendars.CONTENT_URI;
        String selection = "((" + CalendarContract.Calendars.ACCOUNT_NAME + " = ?) AND ("
                + CalendarContract.Calendars.ACCOUNT_TYPE + " = ?) AND ("
                + CalendarContract.Calendars.OWNER_ACCOUNT + " = ?))";

        if ((ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_CALENDAR) == PackageManager.PERMISSION_GRANTED)  ) {

            cur = cr.query(uri, EVENT_PROJECTION, selection, selectionArgs, null);

            while (cur.moveToNext()) {
                long calID = 0;
                String displayName = null;
                String accountName = null;
                String ownerName = null;

                // Get the field values
                calID = cur.getLong(PROJECTION_ID_INDEX);
                displayName = cur.getString(PROJECTION_DISPLAY_NAME_INDEX);
                accountName = cur.getString(PROJECTION_ACCOUNT_NAME_INDEX);
                ownerName = cur.getString(PROJECTION_OWNER_ACCOUNT_INDEX);

                //syncAdaptercall(calID,selectionArgs[0],selectionArgs[1]);
                addEventCalender(calID);
                // Do something with the values...
                Log.d("calender vlaue :",calID+" : "+displayName+" : "+accountName+" : "+ownerName);
            }




        }else{
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_CALENDAR}, MY_PERMISSIONS_REQUEST_WRITE);
        }

    }

    /*public void syncAdaptercall(long eventID,String accountName,String accountType){
        //long eventID = 208;
        Uri uri = ContentUris.withAppendedId(CalendarContract.Events.CONTENT_URI, eventID);
        Intent intent = new Intent(Intent.ACTION_VIEW)
                .setData(uri);
        startActivity(intent);


        //asSyncAdapter(uri,accountName,accountType);
    }*/


    private static final String DEBUG_TAG = "MyActivity";
    public void modifyCalender(long calID1){

        long calID = 188;
        if(calID1 == 0){
            calID1=  calID;
        }
        String mSelectionClause = CalendarContract.Calendars.ACCOUNT_NAME+ " = ? AND ";
        mSelectionClause = mSelectionClause + CalendarContract.Calendars.ACCOUNT_TYPE+ " = ?";
        String[] mSelectionArgs = {"ankit007chauhan@gmail.com", "com.google"};

        ContentValues values = new ContentValues();
        // The new display name for the calendar
        values.put(CalendarContract.Calendars.CALENDAR_DISPLAY_NAME, "Trevor's Calendar");
        Uri updateUri = ContentUris.withAppendedId(CalendarContract.Calendars.CONTENT_URI, calID1);
        int rows = getContentResolver().update(updateUri, values, null, null);
        Log.i(DEBUG_TAG, "Rows updated: " + rows);

        instanceTable(calID1);
        attendanceInsertTable(calID1);

    }

    public static final String[] INSTANCE_PROJECTION = new String[] {
            CalendarContract.Instances.EVENT_ID,      // 0
            CalendarContract.Instances.BEGIN,         // 1
            CalendarContract.Instances.TITLE          // 2
    };

    // The indices for the projection array above.
    //private static final int PROJECTION_ID_INDEX = 0;
    private static final int PROJECTION_BEGIN_INDEX = 1;
    private static final int PROJECTION_TITLE_INDEX = 2;

    // Specify the date range you want to search for recurring
    // event instances
    public void instanceTable(long callID){
    // The ID of the recurring event whose instances you are searching
    // for in the Instances table
        String selection = CalendarContract.Instances.EVENT_ID + " = ?";
        String[] selectionArgs = new String[] {String.valueOf(callID)};

        long startMillis = 0;
        long endMillis = 0;
        Calendar beginTime = Calendar.getInstance();
        beginTime.set(2017, 05, 18, 05, 00);
        startMillis = beginTime.getTimeInMillis();
        Calendar endTime = Calendar.getInstance();
        endTime.set(2017, 05, 18, 8, 45);
        endMillis = endTime.getTimeInMillis();

        // Construct the query with the desired date range.
        Uri.Builder builder = CalendarContract.Instances.CONTENT_URI.buildUpon();
        ContentUris.appendId(builder, startMillis);
        ContentUris.appendId(builder, endMillis);

        /*Cursor cur = null;
        ContentResolver cr = getContentResolver();
        // Submit the query
        cur =  cr.query(builder.build(),INSTANCE_PROJECTION,selection,selectionArgs,null);

        while (cur.moveToNext()) {

            String begin = cur.getString(cur.getColumnIndex(CalendarContract.Instances.BEGIN));

            String title = null;
            long eventID = 0;
            long beginVal = 0;

            // Get the field values
            eventID = cur.getLong(PROJECTION_ID_INDEX);
            beginVal = cur.getLong(PROJECTION_BEGIN_INDEX);
            title = cur.getString(PROJECTION_TITLE_INDEX);

            // Do something with the values.
            Log.i(DEBUG_TAG, "Event:  " + title+" Id :"+eventID);
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(beginVal);
            DateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
            Log.i(DEBUG_TAG, begin+" id : "+"Date: " + formatter.format(calendar.getTime()));

          //  instanceDataInsert();
        }*/

       // dataInsert();


    }

   /* public void dataInsert(){
        long startMillis = System.currentTimeMillis();
        Uri.Builder builder = CalendarContract.CONTENT_URI.buildUpon();
        builder.appendPath("time");
        ContentUris.appendId(builder, startMillis);
        Intent intent = new Intent(Intent.ACTION_VIEW)
                .setData(builder.build());
        startActivity(intent);


    }*/

   /* static Uri asSyncAdapter(Uri uri, String account, String accountType) {
        return uri.buildUpon()
                .appendQueryParameter(android.provider.CalendarContract.CALLER_IS_SYNCADAPTER,"true")
                .appendQueryParameter(CalendarContract.Calendars.ACCOUNT_NAME, account)
                .appendQueryParameter(CalendarContract.Calendars.ACCOUNT_TYPE, accountType).build();
    }*/

   /* public void instanceDataInsert(){
        Calendar beginTime = Calendar.getInstance();
        beginTime.set(2017, 05, 18, 05, 00);
        Calendar endTime = Calendar.getInstance();
        endTime.set(2017, 05, 18, 8, 45);

        Intent intent = new Intent(Intent.ACTION_INSERT)
                .setData(CalendarContract.Events.CONTENT_URI)
                .putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, beginTime.getTimeInMillis())
                .putExtra(CalendarContract.EXTRA_EVENT_END_TIME, endTime.getTimeInMillis())
                .putExtra(CalendarContract.Events.TITLE, "Yoga")
                .putExtra(CalendarContract.Events.DESCRIPTION, "Group class")
                .putExtra(CalendarContract.Events.EVENT_LOCATION, "The gym")
                .putExtra(CalendarContract.Events.AVAILABILITY, CalendarContract.Events.AVAILABILITY_BUSY)
                .putExtra(Intent.EXTRA_EMAIL, "ankit007chauhan@gmail.com,ankit007chauhan@gmail.com");
        startActivity(intent);
    }*/


    //  Event daily use then 10 Occurrences. example:  FREQ=WEEKLY;COUNT=10;WKST=SU
    //  Every other day - forever:  RRULE : FREQ=DAILY;INTERVAL=2
    public void addEventCalender(long calID){

        // set time zone
        String lastRefreshTimeDBFormat = TimeZone.getDefault().getID();

        //long calID = 3;
        long startMillis = 0;
        long endMillis = 0;
        Calendar beginTime = Calendar.getInstance();
        //beginTime.set(2017, 11, 18, 19, 00);
        startMillis = beginTime.getTimeInMillis();
        Calendar endTime = Calendar.getInstance();
        endTime.set(2017, 11, 18, 19, 10);
        endMillis = endTime.getTimeInMillis();

        ContentResolver cr = getContentResolver();
        ContentValues values = new ContentValues();
        //String[] selectionArgs = new String[] {"ankit007chauhan@gmail.com", "com.google","ankit007chauhan@gmail.com"};

        //values.put(CalendarContract.Calendars.ACCOUNT_NAME,selectionArgs[0]);
        //values.put(CalendarContract.Calendars.ACCOUNT_TYPE,CalendarContract.ACCOUNT_TYPE_LOCAL);
        values.put(CalendarContract.Events.CALENDAR_ID, calID);
        values.put(CalendarContract.Events.TITLE, "blip");
        values.put(CalendarContract.Events.DESCRIPTION, "Group workout");
        values.put(CalendarContract.Events.DTSTART, startMillis);
        //values.put(CalendarContract.Events.DTEND, endMillis);
       // values.put(CalendarContract.Events.EVENT_TIMEZONE, "America/Los_Angeles");
        values.put(CalendarContract.Events.EVENT_TIMEZONE, lastRefreshTimeDBFormat);
        values.put(CalendarContract.Events.EVENT_LOCATION, "New Delhi");
        values.put(CalendarContract.Events.ALL_DAY, "0");
        //values.put(CalendarContract.Events.AVAILABILITY, "0");
        values.put(CalendarContract.Events.GUESTS_CAN_INVITE_OTHERS, "1");
        values.put(CalendarContract.Events.GUESTS_CAN_SEE_GUESTS, "1");

        // PT1H0M0S  : its meaning 1 Hours 0 Mint 0 second
        // PT10M  : its meaning in 10 mint to scheduling
        // PT15M  : its meaning in 15 mint to scheduling.
        values.put(CalendarContract.Events.DURATION,"PT10M");
        values.put(CalendarContract.Events.RRULE,"FREQ=DAILY;COUNT=20;BYDAY=MO,TU,WE,TH,FR;WKST=MO");
        //Frequently : FREQ=DAILY;COUNT=20;BYDAY=MO,TU,WE,TH,FR;WKST=MO
        //Weekend : FREQ=WEEKLY;COUNT=10;WKST=SU
        //Weekly :


        if ((ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_CALENDAR) == PackageManager.PERMISSION_GRANTED)  ) {

            //Uri uri = getContentResolver().insert(CalendarContract.Events.CONTENT_URI, values);
            /*Uri uri = CalendarContract.Calendars.CONTENT_URI;
            uri = uri.buildUpon().appendQueryParameter(android.provider.CalendarContract.CALLER_IS_SYNCADAPTER,"true")
                    .appendQueryParameter(CalendarContract.Calendars.ACCOUNT_NAME, "ankit007chauhan@gmail.com")
                    .appendQueryParameter(CalendarContract.Calendars.ACCOUNT_TYPE,"com.google").build();*/

            //Uri uri = cr.insert(CalendarContract.Calendars.CONTENT_URI, values,selectionArgs,);
            Uri uri = cr.insert(CalendarContract.Events.CONTENT_URI, values);

            // get the event ID that is the last element in the Uri
            long eventID = Long.parseLong(uri.getLastPathSegment());
            modifyCalender(eventID);

            Log.i(DEBUG_TAG, "Rows updated: " + eventID);
        }


        //TimePickerFragment timePickerFragment = new TimePickerFragment();
        // DialogFragment fragment = new TimePickerFragment();
        // fragment.show(getSupportFragmentManager(),"timePicker");
        //timePickerFragment.onTimeSet();
        // second type method create
                /*CustomTimePicker timePickerDialog = new CustomTimePicker(CalenderEvent.this, timeSetListener,
                        Calendar.getInstance().get(Calendar.HOUR),
                        CustomTimePicker.getRoundedMinute(Calendar.getInstance().get(Calendar.MINUTE) + CustomTimePicker.TIME_PICKER_INTERVAL), true);
                timePickerDialog.setTitle("Set hours and minutes");
                timePickerDialog.show();*/


        // show custom Time picker Dialog
               /* CustomTimePicker customTimePicker = new CustomTimePicker(CalenderEvent.this);
                customTimePicker.show();
                customTimePicker.setTimeListener(new CustomTimePicker.onTimeSet() {
                    @Override
                    public void onTime(TimePicker timePicker, int hourOfDays, int hourOfMint) {
                        textview.setText(" "+ hourOfDays +" : "+hourOfMint);
                        Toast.makeText(CalenderEvent.this,
                                "time is " + hourOfDays + ":" + hourOfMint, Toast.LENGTH_LONG).show();
                    }
                });*/
    /*private CustomTimePicker.OnTimeSetListener timeSetListener = new CustomTimePicker.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            textview.setText(String.format("%02d", hourOfDay) + ":" +String.format("%02d", minute));
        }
    };*/

  /*  public void setTimePicker(TimePicker mView,int hourOfDays,int hourOfMint){
        TextView textViewp =(TextView)mView.findViewById(R.id.time_picker);
        textViewp.setText(hourOfDays +" : "+ hourOfMint);

    }*/



        //
        // ... do something with event ID
        //
        //
    }

    public void attendanceInsertTable(long eventId){

        ContentResolver cr = getContentResolver();
        ContentValues values = new ContentValues();

        values.put(Attendees.ATTENDEE_NAME, "Trevor");
        values.put(Attendees.ATTENDEE_EMAIL, "trevor@example.com");
        values.put(Attendees.ATTENDEE_RELATIONSHIP, Attendees.RELATIONSHIP_ATTENDEE);
        values.put(Attendees.ATTENDEE_TYPE, Attendees.TYPE_OPTIONAL);
        values.put(Attendees.ATTENDEE_STATUS, Attendees.ATTENDEE_STATUS_INVITED);
        values.put(Attendees.EVENT_ID, eventId);
        if ((ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_CALENDAR) == PackageManager.PERMISSION_GRANTED)  ) {
            Uri uri = cr.insert(Attendees.CONTENT_URI, values);

            long eventID = Long.parseLong(uri.getLastPathSegment());
            Log.i(DEBUG_TAG, "Rows updated: " + eventID);
            //ContentResolver cr1 = getContentResolver();
            ContentValues values1 = new ContentValues();
            values1.put(CalendarContract.Reminders.MINUTES, 15);
            values1.put(CalendarContract.Reminders.EVENT_ID, eventID);
            values1.put(CalendarContract.Reminders.METHOD, CalendarContract.Reminders.METHOD_ALERT);

            Uri uriAlert = cr.insert(CalendarContract.Reminders.CONTENT_URI, values1);

            long eventIDLast = Long.parseLong(uriAlert.getLastPathSegment());
            Log.i(DEBUG_TAG, "Rows updated: " + eventIDLast);
        }

    }



    /*public void attendanceInsertTable(long eventId){

        ContentResolver cr = getContentResolver();
        ContentValues values = new ContentValues();

        values.put(CalendarContract.Attendees.ATTENDEE_NAME, "Calendar Event Change");
        values.put(CalendarContract.Attendees.ATTENDEE_EMAIL, "noreply@gmail.com");
        values.put(CalendarContract.Attendees.ATTENDEE_RELATIONSHIP, CalendarContract.Attendees.RELATIONSHIP_ATTENDEE);
        values.put(CalendarContract.Attendees.ATTENDEE_TYPE, CalendarContract.Attendees.TYPE_OPTIONAL);
        values.put(CalendarContract.Attendees.ATTENDEE_STATUS, CalendarContract.Attendees.ATTENDEE_STATUS_INVITED);
        values.put(CalendarContract.Attendees.EVENT_ID, eventId);
        if ((ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_CALENDAR) == PackageManager.PERMISSION_GRANTED)  ) {
            Uri uri = cr.insert(CalendarContract.Attendees.CONTENT_URI, values);

            long eventID = Long.parseLong(uri.getLastPathSegment());
            Log.i(TAG, "Rows updated: " + eventID);
            //ContentResolver cr1 = getContentResolver();
            ContentValues values1 = new ContentValues();
            values1.put(CalendarContract.Reminders.MINUTES, 10);
            values1.put(CalendarContract.Reminders.EVENT_ID, eventID);
            values1.put(CalendarContract.Reminders.METHOD, CalendarContract.Reminders.METHOD_ALERT);

            Uri uriAlert = cr.insert(CalendarContract.Reminders.CONTENT_URI, values1);

            long eventIDLast = Long.parseLong(uriAlert.getLastPathSegment());

            Log.i(TAG, "Rows updated: " + eventIDLast);
        }

    }*/


  /*  public class TimePickerFragmentClass extends DialogFragment implements TimePickerDialog.OnTimeSetListener{
        Context mContext;
        public TimePickerFragmentClass(Context context){
            this.mContext =context;
        }

        @NonNull
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current time as the default values for the picker
            final Calendar c = Calendar.getInstance();
            int hour = c.get(Calendar.HOUR_OF_DAY);
            int minute = c.get(Calendar.MINUTE);
            return new TimePickerDialog(getActivity(), AlertDialog.THEME_DEVICE_DEFAULT_DARK, this, hour, minute, DateFormat.is24HourFormat(getActivity()));
        }

        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

            //CalenderEvent calenderEvent = new CalenderEvent();
            //  calenderEvent.setTimePicker(view,hourOfDay,minute);

            //text.setText(hourOfDays +" : "+ hourOfMint);

        }
    }*/
}
