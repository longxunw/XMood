package com.wlx.xmood.ui.schedule;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;

import com.wlx.xmood.R;
import com.wlx.xmood.utils.Utils;


public class LessonAllFragment extends Fragment {

    private static LessonAllFragment fragment;

    public static LessonAllFragment newInstance() {

        if (fragment == null) {
            fragment = new LessonAllFragment();
        }
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_lesson_all, container, false);
        Button toDay = root.findViewById(R.id.schedule_to_today_btn);
        toDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.INSTANCE.replaceFragmentToActivity(getFragmentManager(), ScheduleFragment.instance, R.id.nav_host_fragment);
            }
        });
        return root;
//        //表示起始位置为0，占据2行
//        GridLayout.Spec rowSpec=GridLayout.spec(0, 2, GridLayout.UNDEFINED);
////表示起始位置为1，占据1列
//        GridLayout.Spec columnSpec=GridLayout.spec(i, 1, GridLayout.UNDEFINED);
//        GridLayout.LayoutParams params=new GridLayout.LayoutParams(rowSpec, columnSpec);
//        gridlayout.addView(view, params);


    }



}