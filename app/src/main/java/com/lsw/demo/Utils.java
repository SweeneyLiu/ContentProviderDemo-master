package com.lsw.demo;

import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Utils {

    private static String format = "HH:mm:ss";
    private static final String TAG = "Utils";

    /**
     * 判断当前时间是否在[startTime, endTime]区间，注意时间格式要一致
     *
     * @param nowTime 当前时间
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return
     * @author jqlin
     */
    public static boolean isEffectiveDate(Date nowTime, Date startTime, Date endTime) {
        if (nowTime.getTime() == startTime.getTime()
                || nowTime.getTime() == endTime.getTime()) {
            return true;
        }

        Calendar date = Calendar.getInstance();
        date.setTime(nowTime);

        Calendar begin = Calendar.getInstance();
        begin.setTime(startTime);

        Calendar end = Calendar.getInstance();
        end.setTime(endTime);

        if (date.after(begin) && date.before(end)) {
            return true;
        } else {
            return false;
        }
    }


    public static boolean isLockScreenTime(String startTime,String endTime){
        try {
            Date nowTimeDate = new SimpleDateFormat(format).parse("06:27:00");
            Date startTimeDate = new SimpleDateFormat(format).parse(startTime);
            Date middleTimeEnd = new SimpleDateFormat(format).parse("24:00:00");
            Date middleTimeStart = new SimpleDateFormat(format).parse("00:00:00");
            Date endTimeDate = new SimpleDateFormat(format).parse(endTime);

            if (isEffectiveDate(nowTimeDate, startTimeDate, middleTimeEnd) || isEffectiveDate(nowTimeDate, middleTimeStart, endTimeDate)) {
                Log.i(TAG, "onCreate: 在区间内");
                return true;
            } else {
                Log.i(TAG, "onCreate: 在区间外");
                return false;
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }

        return false;
    }
}
