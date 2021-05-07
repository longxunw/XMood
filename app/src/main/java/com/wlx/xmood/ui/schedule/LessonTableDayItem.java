package com.wlx.xmood.ui.schedule;

import java.util.ArrayList;
import java.util.List;

public class LessonTableDayItem {
    private int weekday;
    private List<LessonItem> lessonDay = new ArrayList<>();

    public LessonTableDayItem() {
    }

    public LessonTableDayItem(List<LessonItem> lessonDay) {
        this.lessonDay = lessonDay;
    }

    public int getWeekday() {
        return weekday;
    }

    public void setWeekday(int weekday) {
        this.weekday = weekday;
    }

    public List<LessonItem> getLessonDay() {
        return lessonDay;
    }

    public void setLessonDay(List<LessonItem> lessonDay) {
        this.lessonDay = lessonDay;
    }
}
