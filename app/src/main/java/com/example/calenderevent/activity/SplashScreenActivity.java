package com.example.calenderevent.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.example.calenderevent.R;

import static java.lang.Thread.sleep;

/**
 * Created by Vivek on 15-12-2017.
 */

public class SplashScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);


        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    sleep(2000);
                } catch (Exception e) {
                    e.printStackTrace();
                }finally {
                    launchHomeScreen();
                }
            }
        });
        t.start();

        /*final Runnable runnable= new Runnable() {
            @Override
            public void run() {
                try {
                    sleep(2000);

                } catch (Exception e) {
                    e.printStackTrace();
                }finally {
                    launchHomeScreen();
                }
            }
        };
        runnable.run();*/

    }

    public void launchHomeScreen(){
        //Intent it = new Intent(SplashScreenActivity.this,CalenderViewActivity.class);
        //Intent it = new Intent(SplashScreenActivity.this,ReminderSet.class);
        Intent it = new Intent(SplashScreenActivity.this,CalenderEvent.class);
        startActivity(it);
        finish();
    }

}
