package com.example.calenderevent.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.CalendarView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.calenderevent.R;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by Vivek on 15-12-2017.
 */

public class CalenderViewActivity extends AppCompatActivity {

    CalendarView calendarView;
    TextView textView;


    // 1st call onCreate
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.calender_view);

        calendarView = (CalendarView) findViewById(R.id.calendarView);
        textView = (TextView) findViewById(R.id.textView);

        Calendar calendar = Calendar.getInstance();
        long sys = System.currentTimeMillis();
        Date date = calendar.getTime();

        textView.setText(sys+" "+date.toString());


        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView calendarView, int i, int i1, int i2) {
                textView.setText("Date: " + i2 + " / " + (i1+1) + " / " + i);
                // calender value set
                Toast.makeText(getApplicationContext(), "Selected Date:\n" + "Day = " + i2 + "\n" + "Month = " + i1 + "\n" + "Year = " + i, Toast.LENGTH_LONG).show();
            }
        });


    }

    // 2nd call onStart
    @Override
    protected void onStart() {
        super.onStart();
    }

    // 3rd call onPause
    @Override
    protected void onPause() {
        super.onPause();
    }


}
