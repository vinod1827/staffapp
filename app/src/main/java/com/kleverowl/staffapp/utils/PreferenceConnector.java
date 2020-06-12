package com.kleverowl.staffapp.utils;

import android.content.Context;
import android.content.SharedPreferences;
import org.jetbrains.annotations.Nullable;

/**
 * @Created by Vinod Kulkarni
 * @Date on 7/25/2016.
 */
public class PreferenceConnector {
    public static final String PREF_NAME = "HOME_CONNECT_PREFERENCES";
    public static final int MODE = Context.MODE_PRIVATE;
    public static final String USER_DETAILS = "USER_VO";

    public static final String IS_FIRST_TIME_LAUNCH = "IsFirstTimeLaunch";
    public static final String USER_EMAIL = "user_email";
    public static final String DEVICE_ID = "device_id";
    public static final String KEY = "KEY";
    public static final String TYPE = "TYPE";

    public static void remove(String key, Context context) {
        SharedPreferences.Editor editor = getEditor(context);
        editor.remove(key);
        editor.commit();
    }


    public static void writeBoolean(Context context, String key, boolean value) {
        getEditor(context).putBoolean(key, value).commit();
    }

    public static boolean readBoolean(Context context, String key,
                                      boolean defValue) {
        return getPreferences(context).getBoolean(key, defValue);
    }

    public static void writeInteger(Context context, String key, int value) {
        getEditor(context).putInt(key, value).commit();

    }

    public static int readInteger(Context context, String key, int defValue) {
        return getPreferences(context).getInt(key, defValue);
    }

    public static void writeString(Context context, String key, String value) {
        getEditor(context).putString(key, value).commit();
    }

    public static String readString(Context context, String key, String defValue) {
        return getPreferences(context).getString(key, defValue);
    }

    public static void writeLong(Context context, String key, long value) {
        getEditor(context).putLong(key, value).commit();
    }

    public static long readLong(Context context, String key, long defValue) {
        return getPreferences(context).getLong(key, defValue);
    }

    public static SharedPreferences getPreferences(Context context) {
        return context.getSharedPreferences(PREF_NAME, MODE);
    }

    public static SharedPreferences.Editor getEditor(Context context) {
        return getPreferences(context).edit();
    }
}
