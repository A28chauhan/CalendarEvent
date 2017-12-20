package com.example.calenderevent.activity;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Context;
import android.os.Build;

/**
 * Created by Vivek on 19-12-2017.
 */

public class UserEmailFetcher {

    public static String getGoogleAccountName(Context context){
        AccountManager accountManager = AccountManager.get(context);
        Account[] accounts = accountManager.getAccounts();
        //int count =accounts.length;
        Account account = null;
        //for(int i=0;i<count;i++){
        if(account == null) {
            if(accounts.length > 0) {
                account = accounts[0];
                if (account.type.equals("com.google")) {
                    return account.name;
                }
            }else{
                account = null;
            }
        }
        //}

        return null;
    }

    public Account getStoredAccount(Context context) {
       // final SharedPreferences preferences = getSharedPreferences(PREFS_ACCOUNT_PREFS, Activity.MODE_PRIVATE);
       // String accountname = preferences.getString(PREFS_ACCOUNT_NAME, null);
        AccountManager accountManager = AccountManager.get(context);
        Account account = null;
        /*if(accountname != null) {
            Account[] accounts = accountManager.getAccountsByType("com.google");
            for(Account a : accounts) {
                if(a.toString().equalsIgnoreCase(accountname)) {
                    account = a;
                }
            }
        }*/
        return account;
    }

    public static String getEmail(Context context) {
        AccountManager accountManager = AccountManager.get(context);
        Account account = getAccount(accountManager);
        if (account == null) {
            return "";
        } else {
            return account.name;
        }
    }

    public static Account getAccount(AccountManager accountManager) {
        Account[] accounts = accountManager.getAccountsByType("com.google");
        Account account;
        if (accounts.length > 0) {
            account = accounts[0];
        } else {
            account = null;
        }
        return account;
    }

    public static String getDeviceName() {
        String manufacturer = Build.MANUFACTURER;
        String model = Build.MODEL;
        if (model.startsWith(manufacturer)) {
            return capitalize(model);
        } else {
            return capitalize(manufacturer) + " " + model;
        }
    }


    private static String capitalize(String s) {
        if (s == null || s.length() == 0) {
            return "";
        }
        char first = s.charAt(0);
        if (Character.isUpperCase(first)) {
            return s;
        } else {
            return Character.toUpperCase(first) + s.substring(1);
        }
    }
}
