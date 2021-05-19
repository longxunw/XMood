package com.wlx.xmood.ui.schedule;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.wlx.xmood.R;
import com.wlx.xmood.ui.schedule.edit.ScheduleEditActivity;
import com.wlx.xmood.utils.TimeUtil;
import com.wlx.xmood.utils.Utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class LessonAllFragment extends Fragment {
    private View view;
    private int gridWidth, gridHeight;
    private RelativeLayout layout;
    private static boolean isFirst = true;
    private final int margin = 4;
    private List<LessonItem> lessonItemList = new ArrayList<>();
    private Toolbar.OnMenuItemClickListener onMenuItemClickListener = null;

    public List<LessonItem> getLessonItemList() {
        return lessonItemList;
    }

    public void setLessonItemList(List<LessonItem> lessonItemList) {
        this.lessonItemList = lessonItemList;
    }

    private static LessonAllFragment lessonAllFragment;

    public static LessonAllFragment newInstance() {
        if (lessonAllFragment == null) {
            lessonAllFragment = new LessonAllFragment();
        }
        return lessonAllFragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        if (view == null) {
            view = inflater.inflate(R.layout.fragment_lesson_all, container, false);
            view.findViewById(R.id.schedule_all_lesson).getBackground().setAlpha(190);
        }

        if (onMenuItemClickListener == null) {
            onMenuItemClickListener = item -> {
                if (item.getItemId() == R.id.add_schedule) {
                    Toast.makeText(getContext(), "add_schedule", Toast.LENGTH_SHORT).show();
                }
                return true;
            };
            Toolbar toolbar = view.findViewById(R.id.toolbar_schedule);
            toolbar.inflateMenu(R.menu.schedule_tool_bar);
            toolbar.setOnMenuItemClickListener(onMenuItemClickListener);
        }

        initDate();

        view.post(new Runnable() {
            @Override
            public void run() {
                if (isFirst) {
                    isFirst = false;
                    gridWidth = view.findViewById(R.id.schedule_all_lesson_table).getWidth() / 7;
                    gridHeight = (view.findViewById(R.id.schedule_all_lesson_mon_col).getHeight() - margin * 2) / 10;
                    view.findViewById(R.id.schedule_all_lesson_mon_col).setLayoutParams(new LinearLayout.LayoutParams(gridWidth, ViewGroup.LayoutParams.MATCH_PARENT));
                    view.findViewById(R.id.schedule_all_lesson_tues_col).setLayoutParams(new LinearLayout.LayoutParams(gridWidth, ViewGroup.LayoutParams.MATCH_PARENT));
                    view.findViewById(R.id.schedule_all_lesson_wed_col).setLayoutParams(new LinearLayout.LayoutParams(gridWidth, ViewGroup.LayoutParams.MATCH_PARENT));
                    view.findViewById(R.id.schedule_all_lesson_thu_col).setLayoutParams(new LinearLayout.LayoutParams(gridWidth, ViewGroup.LayoutParams.MATCH_PARENT));
                    view.findViewById(R.id.schedule_all_lesson_fri_col).setLayoutParams(new LinearLayout.LayoutParams(gridWidth, ViewGroup.LayoutParams.MATCH_PARENT));
                    view.findViewById(R.id.schedule_all_lesson_sat_col).setLayoutParams(new LinearLayout.LayoutParams(gridWidth, ViewGroup.LayoutParams.MATCH_PARENT));
                    view.findViewById(R.id.schedule_all_lesson_sun_col).setLayoutParams(new LinearLayout.LayoutParams(gridWidth, ViewGroup.LayoutParams.MATCH_PARENT));
                }

                try {
                    init();
                    for (LessonItem lessonItem : lessonItemList) {
                        try {
                            addLesson(lessonItem);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        });

        Button toTodayBtn = view.findViewById(R.id.schedule_to_today_btn);
        toTodayBtn.setOnClickListener(view -> Utils.INSTANCE.replaceFragmentToActivity(getFragmentManager(), ScheduleFragment.instance, R.id.nav_host_fragment));

        return view;
    }

    private void initDate() {
        List<String> result = getCurrentWeekDate();
        GridView gridView = view.findViewById(R.id.schedule_all_lesson_day_list);
        gridView.setAdapter(new LessonAllDateAdapter(this.getContext(), result));

    }

    private List<String> getCurrentWeekDate() {
        List<String> result = new ArrayList<>();

        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd");
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int dayWeek = cal.get(Calendar.DAY_OF_WEEK);
        if (1 == dayWeek) {
            cal.add(Calendar.DAY_OF_MONTH, -1);
        }
        cal.setFirstDayOfWeek(Calendar.MONDAY);
        int day = cal.get(Calendar.DAY_OF_WEEK);
        cal.add(Calendar.DATE, cal.getFirstDayOfWeek() - day);
        String monday = sdf.format(cal.getTime());
        result.add(monday);
        for (int i = 1; i < 7; ++i) {
            cal.add(Calendar.DATE, 1);
            result.add(sdf.format(cal.getTime()));
        }
        return result;
    }




    @SuppressLint("UseCompatLoadingForDrawables")
    private TextView createTextView(int start, int end, LessonItem lessonItem) {
        TextView tv = new TextView(this.getActivity());
        int marginHeight = 4;
        if (start >= 5) marginHeight += 4;
        if (start >= 9) marginHeight += 4;
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(gridWidth - 2, gridHeight * (end - start + 1) - 2);
        params.setMargins(1, 1, 1, 1);

        tv.setPadding(10, 1, 10, 1);
        tv.setY(gridHeight * (start - 1) + marginHeight);

//        System.out.println("" + params.width + " " + params.height);
        tv.setLayoutParams(params);
        tv.setGravity(Gravity.CENTER);
        tv.setTextSize(13);
        tv.setTextColor(0xff333333);
        tv.setBackground(getResources().getDrawable(R.drawable.shape_lesson_item));
        tv.setText(lessonItem.getName());
        tv.setMaxEms(1);
        tv.setEllipsize(TextUtils.TruncateAt.valueOf("END"));

        tv.setOnClickListener(new View.OnClickListener() {
            @SuppressLint({"SetTextI18n", "SimpleDateFormat"})
            @Override
            public void onClick(View view) {
                View alertView = getLayoutInflater().inflate(R.layout.schedule_lesson_content, null);
                TextView contentView = alertView.findViewById(R.id.schedule_lesson_content_item);

                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                int startPeriod = 0, endPeriod = 0;
                try {
                    startPeriod = getStartPeriod(lessonItem);
                    endPeriod = getEndPeriod(lessonItem);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                contentView.setText("课程名称：" + lessonItem.getName() + "\n" +
                        "上课地点：" + lessonItem.getLocation() + "\n" +
                        "上课时间：第" + startPeriod + "-" + endPeriod + "节 " +
                        (new SimpleDateFormat("HH:mm")).format(new Date(lessonItem.getStartTime())) +
                        "-" + (new SimpleDateFormat("HH:mm")).format(new Date(lessonItem.getEndTime())));


                AlertDialog dialog = builder.setTitle("课程详情")
                        .setCustomTitle(getLayoutInflater().inflate(R.layout.schedule_lesson_content_title, null))
                        .setView(alertView)
                        .setPositiveButton("编辑", (dialogInterface, i) -> {
                            Intent intent = new Intent(getActivity(), ScheduleEditActivity.class);
                            intent.putExtra("ScheduleId", lessonItem.getId());
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            getContext().startActivity(intent);
                        })
                        .setNegativeButton("取消", (dialogInterface, i) -> { })
                        .create();

                dialog.show();

                System.out.println(contentView.getWidth() + " " + params.width);

            }
        });

        return tv;
    }

    private void addLesson(LessonItem lessonItem) throws ParseException {
        TextView tv;
        switch (lessonItem.getWeekDay()) {
            case 1:
                layout = (RelativeLayout) view.findViewById(R.id.schedule_all_lesson_mon_col);
                break;
            case 2:
                layout = (RelativeLayout) view.findViewById(R.id.schedule_all_lesson_tues_col);
                break;
            case 3:
                layout = (RelativeLayout) view.findViewById(R.id.schedule_all_lesson_wed_col);
                break;
            case 4:
                layout = (RelativeLayout) view.findViewById(R.id.schedule_all_lesson_thu_col);
                break;
            case 5:
                layout = (RelativeLayout) view.findViewById(R.id.schedule_all_lesson_fri_col);
                break;
            case 6:
                layout = (RelativeLayout) view.findViewById(R.id.schedule_all_lesson_sat_col);
                break;
            case 7:
                layout = (RelativeLayout) view.findViewById(R.id.schedule_all_lesson_sun_col);
                break;
        }

        int start = getStartPeriod(lessonItem);
        int end = getEndPeriod(lessonItem);
        tv = createTextView(start, end, lessonItem);
        layout.addView(tv);
    }

    private int getStartPeriod(LessonItem lessonItem) throws ParseException {
        long startTime = lessonItem.getStartTime();
        long period1 = new SimpleDateFormat("HH:mm").parse("08:00").getTime();
        long period2 = new SimpleDateFormat("HH:mm").parse("08:55").getTime();
        long period3 = new SimpleDateFormat("HH:mm").parse("10:00").getTime();
        long period4 = new SimpleDateFormat("HH:mm").parse("10:55").getTime();
        long period5 = new SimpleDateFormat("HH:mm").parse("13:00").getTime();
        long period6 = new SimpleDateFormat("HH:mm").parse("13:55").getTime();
        long period7 = new SimpleDateFormat("HH:mm").parse("15:00").getTime();
        long period8 = new SimpleDateFormat("HH:mm").parse("15:55").getTime();
        long period9 = new SimpleDateFormat("HH:mm").parse("18:00").getTime();
        long period10 = new SimpleDateFormat("HH:mm").parse("18:55").getTime();

        if (startTime == period1) return 1;
        else if (startTime == period2) return 2;
        else if (startTime == period3) return 3;
        else if (startTime == period4) return 4;
        else if (startTime == period5) return 5;
        else if (startTime == period6) return 6;
        else if (startTime == period7) return 7;
        else if (startTime == period8) return 8;
        else if (startTime == period9) return 9;
        else if (startTime == period10) return 10;
        return 0;
    }

    private int getEndPeriod(LessonItem lessonItem) throws ParseException {
        long endTime = lessonItem.getEndTime();
        long period1 = new SimpleDateFormat("HH:mm").parse("08:45").getTime();
        long period2 = new SimpleDateFormat("HH:mm").parse("09:40").getTime();
        long period3 = new SimpleDateFormat("HH:mm").parse("10:45").getTime();
        long period4 = new SimpleDateFormat("HH:mm").parse("11:40").getTime();
        long period5 = new SimpleDateFormat("HH:mm").parse("13:45").getTime();
        long period6 = new SimpleDateFormat("HH:mm").parse("14:40").getTime();
        long period7 = new SimpleDateFormat("HH:mm").parse("15:45").getTime();
        long period8 = new SimpleDateFormat("HH:mm").parse("16:40").getTime();
        long period9 = new SimpleDateFormat("HH:mm").parse("18:45").getTime();
        long period10 = new SimpleDateFormat("HH:mm").parse("19:40").getTime();

        if (endTime == period1) return 1;
        else if (endTime == period2) return 2;
        else if (endTime == period3) return 3;
        else if (endTime == period4) return 4;
        else if (endTime == period5) return 5;
        else if (endTime == period6) return 6;
        else if (endTime == period7) return 7;
        else if (endTime == period8) return 8;
        else if (endTime == period9) return 9;
        else if (endTime == period10) return 10;
        return 0;
    }

    private void init() throws ParseException {

        lessonItemList.add(
                new LessonItem(0, "计算理论基础1", "田家炳教育书院236", 1, 1,
                        new SimpleDateFormat("HH:mm").parse("08:00").getTime(),
                        new SimpleDateFormat("HH:mm").parse("09:40").getTime())
        );

        lessonItemList.add(
                new LessonItem(1, "计算理论基础2", "田家炳教育书院236", 2, 2,
                        new SimpleDateFormat("HH:mm").parse("10:00").getTime(),
                        new SimpleDateFormat("HH:mm").parse("11:40").getTime())
        );

        lessonItemList.add(
                new LessonItem(2, "计算理论基础5", "田家炳教育书院236", 5, 3,
                        new SimpleDateFormat("HH:mm").parse("08:00").getTime(),
                        new SimpleDateFormat("HH:mm").parse("10:45").getTime())
        );

        lessonItemList.add(
                new LessonItem(3, "计算理论基础3上午", "田家炳教育书院236", 3, 4,
                        new SimpleDateFormat("HH:mm").parse("10:00").getTime(),
                        new SimpleDateFormat("HH:mm").parse("11:40").getTime())
        );
        lessonItemList.add(
                new LessonItem(3, "计算理论基础1下午", "田家炳教育书院236", 1, 4,
                        new SimpleDateFormat("HH:mm").parse("10:00").getTime(),
                        new SimpleDateFormat("HH:mm").parse("11:40").getTime())
        );

        lessonItemList.add(
                new LessonItem(3, "计算理论基础3下午", "田家炳教育书院236", 3, 4,
                        new SimpleDateFormat("HH:mm").parse("13:00").getTime(),
                        new SimpleDateFormat("HH:mm").parse("15:45").getTime())
        );

        lessonItemList.add(
                new LessonItem(4, "计算理论基础5晚", "田家炳教育书院236", 5, 4,
                        new SimpleDateFormat("HH:mm").parse("18:00").getTime(),
                        new SimpleDateFormat("HH:mm").parse("19:40").getTime())
        );

        lessonItemList.add(
                new LessonItem(4, "计算理论基础5晚", "田家炳教育书院236", 1, 4,
                        new SimpleDateFormat("HH:mm").parse("18:00").getTime(),
                        new SimpleDateFormat("HH:mm").parse("19:40").getTime())
        );


    }

}