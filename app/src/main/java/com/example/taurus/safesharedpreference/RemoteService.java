package com.example.taurus.safesharedpreference;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.support.annotation.Nullable;

import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import cn.jiajunhui.safepreferences.SafePrefer;

public class RemoteService extends Service {

    private int mNumber;

    private final String KEY_STRING = "key_string";
    private final String KEY_INT = "key_int";

    private Timer mTimer;
    private TimerTask mTask;

    private SharedPreferences mSafePrefer;

    @Override
    public void onCreate() {
        super.onCreate();

        mSafePrefer = SafePrefer.getSharedPreference(getApplicationContext(), "safe_prefer");

        mSafePrefer.registerOnSharedPreferenceChangeListener(new SharedPreferences.OnSharedPreferenceChangeListener() {
            @Override
            public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
                System.out.println("onSharedPreferenceChanged : key = " + key);
            }
        });

        Map<String, ?> all = mSafePrefer.getAll();
        if(all!=null)
            System.out.println("all_size = " + all.size());

        mTimer = new Timer();
        mTask = new TimerTask() {
            @Override
            public void run() {
                String value = mSafePrefer.getString(KEY_STRING, null);
                System.out.println("service->>>now_input_number = " + mNumber + ", preferValue = " + value);
                SharedPreferences.Editor edit = mSafePrefer.edit();
                edit.putString(KEY_STRING, String.valueOf(mNumber++));
                edit.putInt(KEY_INT, mNumber);
                edit.apply();
            }
        };
        mTimer.schedule(mTask, 0, 3000);


    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        return super.onStartCommand(intent, flags, startId);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mTask.cancel();
        mTimer.cancel();
    }
}
