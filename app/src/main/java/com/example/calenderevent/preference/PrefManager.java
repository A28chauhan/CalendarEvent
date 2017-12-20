package com.example.calenderevent.preference;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Vivek on 19-12-2017.
 */

public class PrefManager {

    SharedPreferences sh;
    Context mContext;
    SharedPreferences.Editor editor;
    public PrefManager(){}

    public PrefManager(Context context){
        this.mContext = context;
        sh = mContext.getSharedPreferences(PrefConstant.PREFERENCE_NAME,Context.MODE_PRIVATE);
        editor = sh.edit();
    }

    public void setPreferenceValue(Boolean b,String PrefName){
        editor.putBoolean(PrefName,b);
        editor.commit();
    }


    public void setPreferenceValue(Context context,String PrefName,Boolean b){
        sh = context.getSharedPreferences(PrefConstant.PREFERENCE_NAME,Context.MODE_PRIVATE);
        editor = sh.edit();
        editor.putBoolean(PrefName,b);
        editor.commit();
    }

    public boolean isGetPreferenceValue(String PrefName){
        return sh.getBoolean(PrefName,true);
    }

    public void setPreferenceValue(Context context,String PrefValue,String PrefAnswer){
        sh = context.getSharedPreferences(PrefConstant.PREFERENCE_NAME,Context.MODE_PRIVATE);
        editor = sh.edit();
        editor.putString(PrefValue,PrefAnswer);
        editor.commit();

    }

    public void setPreferenceValue(Context context,String PrefValue,Long PrefAnswer){
        sh = context.getSharedPreferences(PrefConstant.PREFERENCE_NAME,Context.MODE_PRIVATE);
        editor = sh.edit();
        editor.putLong(PrefValue,PrefAnswer);
        editor.commit();

    }

    public void setPreferenceValue(Context context,String PrefValue,int PrefAnswer){
        sh = context.getSharedPreferences(PrefConstant.PREFERENCE_NAME,Context.MODE_PRIVATE);
        editor = sh.edit();
        editor.putInt(PrefValue,PrefAnswer);
        editor.commit();

    }

    public int getPreferenceInt(Context context,String PrefAnswer){
        int value=0;
        sh = context.getSharedPreferences(PrefConstant.PREFERENCE_NAME,Context.MODE_PRIVATE);
        value = sh.getInt(PrefAnswer,0);
        return value;
    }

    public String getPreferenceString(Context context,String PrefAnswer){
        String value =null;
        sh = context.getSharedPreferences(PrefConstant.PREFERENCE_NAME,Context.MODE_PRIVATE);
        value = sh.getString(PrefAnswer,null);
        return value;
    }

    public boolean getPreferenceBoolean(Context context,String PrefAnswer){
        Boolean b=false;
        sh = context.getSharedPreferences(PrefConstant.PREFERENCE_NAME,Context.MODE_PRIVATE);
        b = sh.getBoolean(PrefAnswer,false);
        return b;
    }

    public Long getPreferenceLong(Context context,String PrefAnswer){
        long value =0L;
        sh = context.getSharedPreferences(PrefConstant.PREFERENCE_NAME,Context.MODE_PRIVATE);
        value = sh.getLong(PrefAnswer,0);
        return value;
    }

}
