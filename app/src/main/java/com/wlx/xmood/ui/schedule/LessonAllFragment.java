package com.wlx.xmood.ui.schedule;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.wlx.xmood.R;


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
        return inflater.inflate(R.layout.fragment_lesson_all, container, false);
//        //表示起始位置为0，占据2行
//        GridLayout.Spec rowSpec=GridLayout.spec(0, 2, GridLayout.UNDEFINED);
////表示起始位置为1，占据1列
//        GridLayout.Spec columnSpec=GridLayout.spec(i, 1, GridLayout.UNDEFINED);
//        GridLayout.LayoutParams params=new GridLayout.LayoutParams(rowSpec, columnSpec);
//        gridlayout.addView(view, params);



    }



}