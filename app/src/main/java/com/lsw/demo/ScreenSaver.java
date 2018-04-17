/*
 * Copyright (C) 2011 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.lsw.demo;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.os.Handler;
import android.service.dreams.DreamService;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import java.util.Calendar;

public class ScreenSaver extends DreamService {

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

    public ScreenSaver() {
        if (DEBUG) {
            Log.d(TAG, "Screensaver allocated");
        }
    }

    @Override
    public void onCreate() {
        if (DEBUG) {
            Log.d(TAG, "Screensaver created");
        }
        super.onCreate();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        if (DEBUG) {
            Log.d(TAG, "Screensaver configuration changed");
        }
        super.onConfigurationChanged(newConfig);

        // Ignore the configuration change if no window exists.
        if (getWindow() != null) {
            layoutClockSaver();
        }
    }

    @Override
    public void onAttachedToWindow() {
        if (DEBUG) {
            Log.d(TAG, "Screensaver attached to window");
        }
        super.onAttachedToWindow();

        // We want the screen saver to exit upon user interaction.
        setInteractive(false);

        setFullscreen(true);

        mIs24Hour = DateFormat.is24HourFormat(this);

        layoutClockSaver();

        updateTime();

        // Setup handlers for time reference changes and date updates.
        final IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_TIME_TICK);
        filter.addAction(Intent.ACTION_TIME_CHANGED);
        filter.addAction(Intent.ACTION_TIMEZONE_CHANGED);
        registerReceiver(mIntentReceiver, filter);

        // test();
    }

    @Override
    public void onDetachedFromWindow() {
        if (DEBUG) {
            Log.d(TAG, "Screensaver detached from window");
        }
        super.onDetachedFromWindow();

        unregisterReceiver(mIntentReceiver);
    }

    private void layoutClockSaver() {
        setContentView(R.layout.screensaver);

        View contentView = findViewById(R.id.screensaver_content_view);
        mHour0 = (ImageView) contentView.findViewById(R.id.time_hour0);
        mHour1 = (ImageView) contentView.findViewById(R.id.time_hour1);
        mMinute0 = (ImageView) contentView.findViewById(R.id.time_minute0);
        mMinute1 = (ImageView) contentView.findViewById(R.id.time_minute1);
        mMeridiem = (ImageView) contentView.findViewById(R.id.time_meridiem);

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

    private int hour = 0;
    private int minute = 0;

    private void test() {
        final Handler handler = new Handler();
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            showTime(hour % 24, minute % 60);
                        }
                    });
                    hour++;
                    minute++;

                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }
}
