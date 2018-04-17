package com.lsw.demo;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import java.util.Calendar;

public class ScreenActivity extends Activity {


    private static final boolean DEBUG = true;
    private static final String TAG = "DuerShow/Screensaver";

    private static final int[] NUMBER_IDS = {
            R.drawable.number_0,
            R.drawable.number_1,
            R.drawable.number_2,
            R.drawable.number_3,
            R.drawable.number_4,
            R.drawable.number_5,
            R.drawable.number_6,
            R.drawable.number_7,
            R.drawable.number_8,
            R.drawable.number_9
    };

    private ImageView mHour0;
    private ImageView mHour1;
    private ImageView mMinute0;
    private ImageView mMinute1;
    private ImageView mMeridiem;

    private boolean mIs24Hour;

    /**
     * Receiver to handle time reference changes.
     */
    private final BroadcastReceiver mIntentReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            if (DEBUG) {
                Log.v(TAG, "Screensaver onReceive, action: " + action);
            }

            if (action == null) {
                return;
            }

            if (action.equals(Intent.ACTION_TIME_CHANGED)
                    || action.equals(Intent.ACTION_TIMEZONE_CHANGED)
                    || action.equals(Intent.ACTION_TIME_TICK)) {
                updateTime();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.screensaver);
        mIs24Hour = DateFormat.is24HourFormat(this);
        View contentView = findViewById(R.id.screensaver_content_view);
        mHour0 = (ImageView) contentView.findViewById(R.id.time_hour0);
        mHour1 = (ImageView) contentView.findViewById(R.id.time_hour1);
        mMinute0 = (ImageView) contentView.findViewById(R.id.time_minute0);
        mMinute1 = (ImageView) contentView.findViewById(R.id.time_minute1);
        mMeridiem = (ImageView) contentView.findViewById(R.id.time_meridiem);

        updateTime();

        // Setup handlers for time reference changes and date updates.
        final IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_TIME_TICK);
        filter.addAction(Intent.ACTION_TIME_CHANGED);
        filter.addAction(Intent.ACTION_TIMEZONE_CHANGED);
        registerReceiver(mIntentReceiver, filter);

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                Log.i(TAG, "onTouchEvent: ");
                finish();
                break;
            default:
                break;
        }
        return super.onTouchEvent(event);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mIntentReceiver);
    }

    private void updateTime() {
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        showTime(hour, minute);
    }

    private void showTime(int hour, int minute) {
        if (mIs24Hour) {
            mMeridiem.setVisibility(View.GONE);
        } else {
            mMeridiem.setVisibility(View.VISIBLE);
            if (hour >= 12) {
                mMeridiem.setImageResource(R.drawable.time_pm);
            } else {
                mMeridiem.setImageResource(R.drawable.time_am);
            }

            if (hour > 12) {
                hour -= 12;
            }
        }

        if (hour >= 10) {
            mHour0.setVisibility(View.VISIBLE);
            mHour0.setImageResource(NUMBER_IDS[hour / 10]);
        } else {
            mHour0.setVisibility(View.GONE);
        }
        mHour1.setImageResource(NUMBER_IDS[hour % 10]);

        mMinute0.setImageResource(NUMBER_IDS[minute / 10]);
        mMinute1.setImageResource(NUMBER_IDS[minute % 10]);
    }
}
