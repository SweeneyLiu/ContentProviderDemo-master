package com.lsw.demo;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.SparseBooleanArray;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;


import java.util.Arrays;

public class WeekSelector extends LinearLayout implements View.OnClickListener {

    private static final int[] WEEK_IDS = {
            R.string.Monday,
            R.string.Tuesday,
            R.string.Wednesday,
            R.string.Thursday,
            R.string.Friday,
            R.string.Saturday,
            R.string.Sunday
    };

    private View[] mDayViews = new View[WEEK_IDS.length + 1];

    public WeekSelector(Context context) {
        super(context);
        initViews(context);
    }

    public WeekSelector(Context context,
                        @Nullable AttributeSet attrs) {
        super(context, attrs);
        initViews(context);
    }

    public WeekSelector(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initViews(context);
    }

    public WeekSelector(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initViews(context);
    }

    public void setDays(int[] days) {
        if (days != null) {
            for (int selectedIndex : days) {
                if (selectedIndex > 0 && selectedIndex < mDayViews.length
                        && mDayViews[selectedIndex] != null) {
                    mDayViews[selectedIndex].setSelected(true);
                }
            }
        }
    }

    public int[] getDays() {
        final SparseBooleanArray rt = new SparseBooleanArray(mDayViews.length);
        for (int i = 1; i < mDayViews.length; i++) {
            if (mDayViews[i] != null && mDayViews[i].isSelected()) {
                rt.put(i, true);
            }
        }

        final int[] rta = new int[rt.size()];
        for (int i = 0; i < rta.length; i++) {
            rta[i] = rt.keyAt(i);
        }
        Arrays.sort(rta);
        return rta;
    }

    private void initViews(Context context) {
        setOrientation(HORIZONTAL);

        for (int i = 0; i < WEEK_IDS.length; i++) {
            if (i != 0) {
                View space = new View(context);
                LayoutParams spaceLayoutParams = new LayoutParams(
                        0,
                        ViewGroup.LayoutParams.WRAP_CONTENT);
                spaceLayoutParams.weight = 1;
                addView(space, spaceLayoutParams);
            }

            TextView view = new TextView(context);
            LayoutParams layoutParams = new LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutParams.gravity = Gravity.CENTER_VERTICAL;
            view.setText(WEEK_IDS[i]);
            view.setTextAppearance(R.style.categoryTitleTextAppearance);
            view.setClickable(true);
            view.setOnClickListener(this);

            // 0~6 => 1~7
            mDayViews[((i + 1) % WEEK_IDS.length) + 1] = view;

            addView(view, layoutParams);
        }
    }

    @Override
    public void onClick(View v) {
        v.setSelected(!v.isSelected());
    }
}
