package com.wlx.xmood.ui.schedule;

import java.util.ArrayList;
import java.util.List;

public class LessonTable {
    private List<LessonTableDayItem> lessonTableWeek = new ArrayList<>();

    public LessonTable() {
    }

    public LessonTable(List<LessonTableDayItem> lessonTableWeek) {
        this.lessonTableWeek = lessonTableWeek;
    }

    public List<LessonTableDayItem> getLessonTableWeek() {
        return lessonTableWeek;
    }

    public void setLessonTableWeek(List<LessonTableDayItem> lessonTableWeek) {
        this.lessonTableWeek = lessonTableWeek;
    }
}
