package com.wlx.xmood.ui.schedule.edit;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toolbar;

import com.wlx.xmood.BaseActivity;
import com.wlx.xmood.R;
import com.wlx.xmood.ui.schedule.LessonAllFragment;
import com.wlx.xmood.ui.schedule.ScheduleDataGet;
import com.wlx.xmood.ui.schedule.ScheduleFragment;
import com.wlx.xmood.utils.DatePicker.CustomDatePicker;
import com.wlx.xmood.utils.TimeUtil;
import com.wlx.xmood.utils.Utils;

public class SemesterDateSetActivity extends BaseActivity implements View.OnClickListener {
    private TextView mTvSelectedDate, mTvSelectedTime;
    private CustomDatePicker mDatePicker, mTimerPicker;
    private CustomDatePicker.Callback callback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_semester_date_set);

        findViewById(R.id.ll_date).setOnClickListener(this);
        mTvSelectedDate = findViewById(R.id.tv_selected_date);
        initDatePicker();

//        findViewById(R.id.ll_time).setOnClickListener(this);
//        mTvSelectedTime = findViewById(R.id.tv_selected_time);
//        initTimerPicker();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_date:
                // 日期格式为yyyy-MM-dd
                mDatePicker.show(mTvSelectedDate.getText().toString());
                break;

//            case R.id.ll_time:
//                // 日期格式为yyyy-MM-dd HH:mm
//                mTimerPicker.show(mTvSelectedTime.getText().toString());
//                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mDatePicker.onDestroy();
    }

    private void initDatePicker() {
        long beginTimestamp = TimeUtil.INSTANCE.Str2Long("2000-05-01", "yyyy-MM-dd");
        long endTimestamp = System.currentTimeMillis();

        mTvSelectedDate.setText(TimeUtil.INSTANCE.Long2Str(endTimestamp, "yyyy-MM-dd"));
        callback = timestamp -> {
            mTvSelectedDate.setText(TimeUtil.INSTANCE.Long2Str(timestamp, "yyyy-MM-dd"));
            findViewById(R.id.date_select_confirm).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ScheduleDataGet.INSTANCE.setStartDate(TimeUtil.INSTANCE.Long2Str(timestamp, "yyyy-MM-dd"));
                    Utils.INSTANCE.makeToast(getBaseContext(), "已保存");
                    finish();
                }
            });
        };

        // 通过时间戳初始化日期，毫秒级别
        mDatePicker = new CustomDatePicker(this, callback, beginTimestamp, endTimestamp);

        // 不允许点击屏幕或物理返回键关闭
        mDatePicker.setCancelable(false);
        // 不显示时和分
        mDatePicker.setCanShowPreciseTime(false);
        // 不允许循环滚动
        mDatePicker.setScrollLoop(false);
        // 不允许滚动动画
        mDatePicker.setCanShowAnim(false);


    }

//    private void initTimerPicker() {
//        String beginTime = "2018-10-17 18:00";
//        String endTime = TimeUtil.INSTANCE.Long2Str(System.currentTimeMillis(), "yyyy-MM-dd HH:mm");
//
//        mTvSelectedTime.setText(endTime);
//
//        // 通过日期字符串初始化日期，格式请用：yyyy-MM-dd HH:mm
//        mTimerPicker = new CustomDatePicker(this, new CustomDatePicker.Callback() {
//            @Override
//            public void onTimeSelected(long timestamp) {
//                mTvSelectedTime.setText(TimeUtil.INSTANCE.Long2Str(timestamp, "yyyy-MM-dd HH:mm"));
//            }
//        }, beginTime, endTime);
//        // 允许点击屏幕或物理返回键关闭
//        mTimerPicker.setCancelable(true);
//        // 显示时和分
//        mTimerPicker.setCanShowPreciseTime(true);
//        // 允许循环滚动
//        mTimerPicker.setScrollLoop(true);
//        // 允许滚动动画
//        mTimerPicker.setCanShowAnim(true);
//    }

}