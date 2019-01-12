package com.example.taurus.safesharedpreference;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import cn.jiajunhui.safepreferences.SafePrefer;

public class MainActivity extends AppCompatActivity {

    private int mNumber;

    private final String KEY_STRING = "key_string";

    private Handler mHandler = new Handler(Looper.getMainLooper()){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 0:
                    String value = mSafePrefer.getString(KEY_STRING, null);
                    System.out.println("activity->>>now_input_number = " + mNumber + ", preferValue = " + value);
                    SharedPreferences.Editor edit = mSafePrefer.edit();
                    edit.putString(KEY_STRING, String.valueOf(mNumber++));
                    edit.apply();
                    mHandler.sendEmptyMessageDelayed(0, 1000);
                    break;
            }
        }
    };

    private SharedPreferences mSafePrefer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mSafePrefer = SafePrefer.getSharedPreference(this, "safe_prefer");

        mSafePrefer.registerOnSharedPreferenceChangeListener(new SharedPreferences.OnSharedPreferenceChangeListener() {
            @Override
            public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {

            }
        });

        mHandler.sendEmptyMessage(0);

        Intent service = new Intent(this, RemoteService.class);
        startService(service);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mHandler.removeMessages(0);
        Intent service = new Intent(this, RemoteService.class);
        stopService(service);
    }
}
