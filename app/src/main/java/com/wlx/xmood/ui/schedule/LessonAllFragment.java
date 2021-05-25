package com.wlx.xmood.ui.schedule;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.wlx.xmood.R;
import com.wlx.xmood.ui.schedule.edit.ScheduleEditActivity;
import com.wlx.xmood.utils.DensityUtil;
import com.wlx.xmood.utils.TimeUtil;
import com.wlx.xmood.utils.Utils;

import org.jetbrains.annotations.NotNull;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import kotlin.Result;

public class LessonAllFragment extends Fragment {
    private LessonAllViewModel viewModel;
    private View view;
    private int gridWidth, gridHeight;
    private RelativeLayout monLayout;
    private RelativeLayout tuesLayout;
    private RelativeLayout wedLayout;
    private RelativeLayout thursLayout;
    private RelativeLayout friLayout;
    private RelativeLayout satLayout;
    private RelativeLayout sunLayout;
    private static boolean isFirst = true;
    private final int margin = 4;
    private Context context;
    private Toolbar.OnMenuItemClickListener onMenuItemClickListener = null;


    private static LessonAllFragment lessonAllFragment;

    public static LessonAllFragment newInstance() {
        if (lessonAllFragment == null) {
            lessonAllFragment = new LessonAllFragment();
        }
        return lessonAllFragment;
    }


    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        context = requireContext();
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_lesson_all, container, false);
            monLayout = view.findViewById(R.id.schedule_all_lesson_mon_col);
            tuesLayout = view.findViewById(R.id.schedule_all_lesson_tues_col);
            wedLayout = view.findViewById(R.id.schedule_all_lesson_wed_col);
            thursLayout = view.findViewById(R.id.schedule_all_lesson_thu_col);
            friLayout = view.findViewById(R.id.schedule_all_lesson_fri_col);
            satLayout = view.findViewById(R.id.schedule_all_lesson_sat_col);
            sunLayout = view.findViewById(R.id.schedule_all_lesson_sun_col);
        }

        if (viewModel == null) {
            viewModel = new ViewModelProvider(this).get(LessonAllViewModel.class);
        }
        viewModel.searchSchedule(-1);

        if (onMenuItemClickListener == null) {
            onMenuItemClickListener = item -> {
                if (item.getItemId() == R.id.add_schedule) {
                    Intent intent = new Intent(context, ScheduleEditActivity.class);
                    intent.putExtra("Id", -1);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                }
                return true;
            };
            Toolbar toolbar = view.findViewById(R.id.toolbar_schedule);
            toolbar.inflateMenu(R.menu.schedule_tool_bar);
            toolbar.setOnMenuItemClickListener(onMenuItemClickListener);
        }

        initDate();
        TextView weekText = view.findViewById(R.id.schedule_week_text);
        weekText.setText("第 " + TimeUtil.INSTANCE.getWeekCount(ScheduleDataGet.INSTANCE.getStartDate()) + " 周");

        view.post(() -> {
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

        });

        Button toTodayBtn = view.findViewById(R.id.schedule_to_today_btn);
        toTodayBtn.setOnClickListener(view -> Utils.INSTANCE.replaceFragmentToActivity(getFragmentManager(), ScheduleFragment.instance, R.id.nav_host_fragment));

        viewModel.getScheduleLiveData().observe(getViewLifecycleOwner(), arrayListResult -> {
            ArrayList<LessonItem> scheduleList = ScheduleDataGet.INSTANCE.getScheduleList();
            if (scheduleList != null) {
                viewModel.getScheduleList().clear();
                viewModel.getScheduleList().addAll(scheduleList);
            }
            clearAllLesson();
            long week = TimeUtil.INSTANCE.getWeekCount(ScheduleDataGet.INSTANCE.getStartDate());
            for (LessonItem lessonItem : viewModel.getScheduleList()) {
                try {
                    if (lessonItem.getStartWeek() > week || lessonItem.getEndWeek() < week) continue;
                    if (lessonItem.getWeekType() == 0) {
                        // 没有单双周 判断在起始周范围内
                        addLesson(lessonItem);
                    } else if (lessonItem.getWeekType() == 1 && week % 2 == 1L) {
                        // 单周 判断当前周是不是单周
                        addLesson(lessonItem);
                    } else if (lessonItem.getWeekType() == 2 && week % 2 == 0L) {
                        // 双周
                        addLesson(lessonItem);
                    }

                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        });

        return view;
    }

    private void initDate() {
        List<String> result = TimeUtil.INSTANCE.getCurrentWeekDate();
        GridView gridView = view.findViewById(R.id.schedule_all_lesson_day_list);
        gridView.setAdapter(new LessonAllDateAdapter(this.getContext(), result));
    }


    @SuppressLint("UseCompatLoadingForDrawables")
    private TextView createTextView(int start, int end, LessonItem lessonItem) {
        TextView tv = new TextView(this.getActivity());
        int marginHeight = 4;
        if (start >= 5) {
            marginHeight += 4;
        }
        if (start >= 9) {
            marginHeight += 4;
        }
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(gridWidth - 2, gridHeight * (end - start + 1) - 2);
        params.setMargins(1, 1, 1, 1);

        tv.setPadding(10, 1, 10, 1);
        tv.setY(gridHeight * (start - 1) + marginHeight);

        tv.setLayoutParams(params);
        tv.setGravity(Gravity.CENTER);
        tv.setTextSize(13);
        tv.setTextColor(0xff333333);
        tv.setBackground(getResources().getDrawable(R.drawable.shape_lesson_item));
        tv.setText(lessonItem.getName());
        tv.setEllipsize(TextUtils.TruncateAt.valueOf("END"));

        tv.setOnClickListener(new View.OnClickListener() {
            @SuppressLint({"SetTextI18n", "SimpleDateFormat"})
            @Override
            public void onClick(View view) {
                showDialog(lessonItem);
            }
        });

        return tv;
    }

    private void addLesson(LessonItem lessonItem) throws ParseException {
        TextView tv;
        RelativeLayout layout;
        switch (lessonItem.getWeekDay()) {
            case 1:
                layout = monLayout;
                break;
            case 2:
                layout = tuesLayout;
                break;
            case 3:
                layout = wedLayout;
                break;
            case 4:
                layout = thursLayout;
                break;
            case 5:
                layout = friLayout;
                break;
            case 6:
                layout = satLayout;
                break;
            case 7:
                layout = sunLayout;
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + lessonItem.getWeekDay());
        }

        int start = TimeUtil.INSTANCE.getStartPeriod(lessonItem);
        int end = TimeUtil.INSTANCE.getEndPeriod(lessonItem);
        tv = createTextView(start, end, lessonItem);
        layout.addView(tv);
    }


    private void showDialog(LessonItem lessonItem) {
        Dialog lessonInfoDialog = new Dialog(getContext(), R.style.MyDialogTheme);
        View contentView = LayoutInflater.from(getContext()).inflate(
                R.layout.set_lesson_info_dialog_content, null
        );
        TextView name = contentView.findViewById(R.id.dialog_lesson_info_name_text);
        name.setText(lessonItem.getName());
        TextView address = contentView.findViewById(R.id.dialog_lesson_info_address_text);
        address.setText(lessonItem.getLocation());
        TextView time = contentView.findViewById(R.id.dialog_lesson_info_time_text);
        String timeStr = TimeUtil.INSTANCE.Date2Str(new Date(lessonItem.getStartTime()), "HH:mm") +
                "--" + TimeUtil.INSTANCE.Date2Str(new Date(lessonItem.getEndTime()), "HH:mm");
        time.setText(timeStr);
        lessonInfoDialog.setContentView(contentView);
        ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) contentView.getLayoutParams();
        layoutParams.width = getResources().getDisplayMetrics().widthPixels - DensityUtil.INSTANCE.dp2px(context, 40f);
        contentView.setLayoutParams(layoutParams);
        lessonInfoDialog.getWindow().setGravity(Gravity.CENTER);
        TextView cancel = lessonInfoDialog.getWindow().findViewById(R.id.dialog_lesson_info_cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lessonInfoDialog.dismiss();
            }
        });
        TextView edit = lessonInfoDialog.getWindow().findViewById(R.id.dialog_lesson_info_edit);
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ScheduleEditActivity.class);
                intent.putExtra("ScheduleId", lessonItem.getId());
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
                lessonInfoDialog.dismiss();
            }
        });
        lessonInfoDialog.show();
    }

    private void clearAllLesson() {
        monLayout.removeAllViews();
        tuesLayout.removeAllViews();
        wedLayout.removeAllViews();
        thursLayout.removeAllViews();
        friLayout.removeAllViews();
        satLayout.removeAllViews();
        sunLayout.removeAllViews();
    }

}