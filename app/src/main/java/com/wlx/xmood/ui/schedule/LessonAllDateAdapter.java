package com.wlx.xmood.ui.schedule;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.wlx.xmood.R;

import java.util.List;

public class LessonAllDateAdapter extends BaseAdapter {
    private Context context;
    private List<String> currentWeekDate;

    public LessonAllDateAdapter(Context context, List<String> currentWeekDate) {
        this.context = context;
        this.currentWeekDate = currentWeekDate;
    }

    @Override
    public int getCount() {
        return currentWeekDate.size();
    }

    @Override
    public Object getItem(int i) {
        return currentWeekDate.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.schedule_lesson_day_item, viewGroup, false);
        }
        TextView tv = (TextView) view.findViewById(R.id.schedule_all_lesson_item);
        switch (i) {
            case 0:
                tv.setText("周一");
                break;
            case 1:
                tv.setText("周二");
                break;
            case 2:
                tv.setText("周三");
                break;
            case 3:
                tv.setText("周四");
                break;
            case 4:
                tv.setText("周五");
                break;
            case 5:
                tv.setText("周六");
                break;
            case 6:
                tv.setText("周日");
                break;

        }
        tv.setText(tv.getText() + "\n" + currentWeekDate.get(i));

        return view;
    }
}
