package com.wlx.xmood.dao

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.graphics.BitmapFactory
import com.wlx.xmood.R
import com.wlx.xmood.utils.ImageUtil

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

    private val createUser = "create table User (" +
            "username TEXT," +
            "profileImg BLOB, " +
            "autograph TEXT," +
            "semesterStartDate TEXT," +
            "lessonImg BLOB)"

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


    private val insertData1 = "insert into Note(head, updateTime, catalog, body)" +
            " values('示例标题', 1, '默认分类', '示例内容')"

    private val insertData2 = "insert into Note(head, updateTime, catalog, body)" +
            " values('示例标题2', 2, '默认分类', '示例内容2')"
//    private val createInfo = "create table Info(" +
//            "semesterStartDate TEXT)"


    override fun onCreate(db: SQLiteDatabase?) {
        db?.let {
            db.execSQL(createSchedule)
            db.execSQL(createUser)
            db.execSQL(createNote)
            db.execSQL(createMood)
            db.execSQL(createDaily)
            db.execSQL(insertData1)
            db.execSQL(insertData2)
//            db.execSQL(createInfo)
            val profileImg =
                BitmapFactory.decodeResource(context.resources, R.mipmap.me_face__example_img)
            val lessonImg =
                BitmapFactory.decodeResource(
                    context.resources,
                    R.drawable.schedule_all_background_bingbing
                )
            val userValue = ContentValues().apply {
                put("username", "XMooder")
                put("autograph", "A XMooder!")
                put("profileImg", ImageUtil.bitmap2ByteArray(profileImg))
                put("semesterStartDate", "")
                put("lessonImg", ImageUtil.bitmap2ByteArray(lessonImg))
            }
            db.insert("User", null, userValue)
        }
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {

    }
}