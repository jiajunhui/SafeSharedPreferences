package cn.jiajunhui.safepreferences;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.Set;

/**
 * Created by Taurus on 2019/1/12.
 */
public class SafePrefer {

    public static SharedPreferences getSharedPreference(Context context, String preferName){
        return SafeSharedPreferences.getSharedPreference(context, preferName);
    }

    public static void putInt(Context context, String preferName, String key, int value){
        SharedPreferences sharedPreference = getSharedPreference(context, preferName);
        SharedPreferences.Editor edit = sharedPreference.edit();
        edit.putInt(key, value);
        edit.apply();
    }

    public static void putLong(Context context, String preferName, String key, long value){
        SharedPreferences sharedPreference = getSharedPreference(context, preferName);
        SharedPreferences.Editor edit = sharedPreference.edit();
        edit.putLong(key, value);
        edit.apply();
    }

    public static void putFloat(Context context, String preferName, String key, float value){
        SharedPreferences sharedPreference = getSharedPreference(context, preferName);
        SharedPreferences.Editor edit = sharedPreference.edit();
        edit.putFloat(key, value);
        edit.apply();
    }

    public static void putBoolean(Context context, String preferName, String key, boolean value){
        SharedPreferences sharedPreference = getSharedPreference(context, preferName);
        SharedPreferences.Editor edit = sharedPreference.edit();
        edit.putBoolean(key, value);
        edit.apply();
    }

    public static void putString(Context context, String preferName, String key, String value){
        SharedPreferences sharedPreference = getSharedPreference(context, preferName);
        SharedPreferences.Editor edit = sharedPreference.edit();
        edit.putString(key, value);
        edit.apply();
    }

    public static void putStringSet(Context context, String preferName, String key, Set<String> value){
        SharedPreferences sharedPreference = getSharedPreference(context, preferName);
        SharedPreferences.Editor edit = sharedPreference.edit();
        edit.putStringSet(key, value);
        edit.apply();
    }

}
