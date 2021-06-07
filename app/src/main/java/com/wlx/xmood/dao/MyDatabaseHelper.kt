package com.wlx.xmood.dao

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class MyDatabaseHelper(val context: Context, name: String, version: Int) :
    SQLiteOpenHelper(context, name, null, version) {
    private val createDaily = "create table Daily (" +
            "id INTEGER primary key autoincrement," +
            " day INTEGER," +
            " startTime INTEGER," +
            " endTime INTEGER," +
            " event TEXT," +
            " isAlarm INTEGER," +
            " alarmTime INTEGER," +
            " isFinish INTEGER)"

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
            " updateTime INTEGER," +
            " catalog TEXT," +
            " body TEXT)"


    private val createMood = "create table Mood (" +
            "id INTEGER primary key autoincrement" +
            ", date INTEGER" +
            ", rating INTEGER" +
            ", category TEXT" +
            ", event TEXT)"

    private val createSchedule = "create table Schedule (" +
            "id INTEGER primary key autoincrement," +
            " name TEXT," +
            " location TEXT," +
            " weekDay INTEGER," +
            " startTime INTEGER," +
            " endTime INTEGER," +
            " startWeek INTEGER," +
            " endWeek INTEGER," +
            " weekType INTEGER)"

    private val createInfo = "create table Info(semesterStartDate Date)";

    override fun onCreate(db: SQLiteDatabase?) {
        db?.let {
            db.execSQL(createSchedule)
//        db.execSQL(createUser)
            db.execSQL(createNote)
            db.execSQL(createMood)
            db.execSQL(createDaily)
            db.execSQL(createInfo)
        }
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {

    }
}