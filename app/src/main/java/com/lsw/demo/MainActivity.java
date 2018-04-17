package com.lsw.demo;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.provider.AlarmClock;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;

public class MainActivity extends Activity {

    private static final String TAG = "MainActivity";
    private CountTimer countTimerView;
    private Button screenSave;
    private Button webview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();

        init();

        webview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                createAlarm(MainActivity.this,"测试",16,20,"");
                Intent intent = new Intent(MainActivity.this, SkillStoreActivity.class);
                startActivity(intent);
            }
        });


        screenSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if(Utils.isLockScreenTime("22:00:00","07:00:00")){
//                    timeStart();
//                }
//
                Intent intent = new Intent(MainActivity.this,ScreenActivity.class);
                startActivity(intent);

            }
        });

    }


    private void init() {
        //初始化CountTimer，设置倒计时为2分钟。
        countTimerView=new CountTimer(30000,1000,MainActivity.this);
    }

    private void timeStart(){
        new Handler(getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                if(Utils.isLockScreenTime("22:00:00","07:00:00")){
                    countTimerView.start();
                    Log.i(TAG, "run: true");
                }else{
                    Log.i(TAG, "run: false");
                }
            }
        });
    }

    /**
     * 设置闹钟
     *
     * @param context
     * @param msg     闹钟标题
     * @param hour    时
     * @param min     分
     */
    private void createAlarm(Context context, String msg, int hour, int min, String type) {
        Intent intent = new Intent(AlarmClock.ACTION_SET_ALARM);
        intent.putExtra(AlarmClock.EXTRA_MESSAGE, msg);
        intent.putExtra(AlarmClock.EXTRA_HOUR, hour);
        intent.putExtra(AlarmClock.EXTRA_MINUTES, min);
        if ("DAILY".equals(type)) {
            ArrayList<Integer> list = new ArrayList<>();
            list.add(Calendar.WEDNESDAY);
            intent.putExtra(AlarmClock.EXTRA_DAYS, list);
        }
        if (intent.resolveActivity(context.getPackageManager()) != null)
            context.startActivity(intent);
    }

    private void initView() {
        screenSave = (Button) findViewById(R.id.screen_save);
        webview = (Button) findViewById(R.id.webview);
    }


    /**
     * 主要的方法，重写dispatchTouchEvent
     * @param ev
     * @return
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
//        switch (ev.getAction()){
//            //获取触摸动作，如果ACTION_UP，计时开始。
//            case MotionEvent.ACTION_UP:
//                countTimerView.start();
//                break;
//            //否则其他动作计时取消
//            default:countTimerView.cancel();
//                break;
//        }
        return super.dispatchTouchEvent(ev);
    }
    @Override
    protected void onPause() {
        super.onPause();
        countTimerView.cancel();
    }
    @Override
    protected void onResume() {
        super.onResume();
//        timeStart();
    }

}
