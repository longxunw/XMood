package com.wlx.xmood.dao

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class MyDatabaseHelper(val context: Context, name: String, version: Int) :
    SQLiteOpenHelper(context, name, null, version) {
    private val createSchedule = "create table Schedule (" +
            "id INTEGER primary key autoincrement," +
            " day DATE," +
            " startTime TIME," +
            " endTime TIME," +
            " event TEXT," +
            " isAlarm INTEGER," +
            " alarmTime TIMESTAMP," +
            " isFinish INTEGER)";

//    private val createUser = "create table User (" +
//            "id INTEGER primary key autoincrement, " +
//            "username TEXT, password TEXT, " +
//            "token TEXT, " +
//            "profileImg BLOB, " +
//            "scheduleBackground BLOB, " +
//            "theme TEXT)";

    private val createNote = "create table Note (" +
            "id INTEGER primary key autoincrement," +
            " head TEXT," +
            " updateTIme DATE," +
            " catalog TEXT," +
            " body TEXT)";

    private val createMood = "create table Mood (" +
            "id INTEGER primary key autoincrement," +
            " score INTEGER," +
            " type TEXT," +
            " day DATESTAMP," +
            " event TEXT)";

    private val createTime = "create table Time (" +
            "id INTEGER primary key autoincrement," +
            " name TEXT," +
            " location TEXT," +
            " weekDay INTEGER," +
            " startTime TIME," +
            " endTime TIME," +
            " startWeek INTEGER," +
            " endWeek INTEGER," +
            " weekType INTEGER";

    private val createInfo = "create table Info(semesterStartDate Date)";

    override fun onCreate(db: SQLiteDatabase?) {
        db.execSQL(createSchedule)
//        db.execSQL(createUser)
        db.execSQL(createNote)
        db.execSQL(createMood)
        db.execSQL(createTime)
        db.execSQL(createInfo)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {

    }
}