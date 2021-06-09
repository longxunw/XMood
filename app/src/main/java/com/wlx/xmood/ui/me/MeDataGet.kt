package com.wlx.xmood.ui.me

import android.content.ContentValues
import android.graphics.Bitmap
import com.wlx.xmood.dao.MyDatabaseHelper
import com.wlx.xmood.utils.ImageUtil

object MeDataGet {
    lateinit var dbHelper: MyDatabaseHelper

    fun getProfileImg(): Bitmap {
        val db = dbHelper.writableDatabase
        lateinit var bitmap: Bitmap
        val sql = "select * from User"
        val cursor = db.rawQuery(sql, null, null)
        cursor.apply {
            if (moveToFirst()) {
                val byteArray = getBlob(getColumnIndex("profileImg"))
                bitmap = ImageUtil.byteArray2Bitmap(byteArray)
            }
            close()
        }
        return bitmap
    }

    fun getUserName(): String {
        val db = dbHelper.writableDatabase
        val sql = "select * from User"
        var str = ""
        val cursor = db.rawQuery(sql, null, null)
        cursor.apply {
            if (moveToFirst()) {
                str = getString(getColumnIndex("username"))
            }
            close()
        }
        return str
    }

    fun getAutograph(): String {
        val db = dbHelper.writableDatabase
        val sql = "select * from User"
        var str = ""
        val cursor = db.rawQuery(sql, null, null)
        cursor.apply {
            if (moveToFirst()) {
                str = getString(getColumnIndex("autograph"))
            }
            close()
        }
        return str
    }

    fun getStartDate(): String {
        val db = dbHelper.writableDatabase
        val sql = "select * from User"
        var str = ""
        val cursor = db.rawQuery(sql, null, null)
        cursor.apply {
            if (moveToFirst()) {
                str = getString(getColumnIndex("semesterStartDate"))
            }
            close()
        }
        return str
    }

    fun setProfileImg(bitmap: Bitmap) {
        val db = dbHelper.writableDatabase
        val value = ContentValues().apply {
            put("profileImg", ImageUtil.bitmap2ByteArray(bitmap))
        }
        db.update("User", value, "", arrayOf())
    }

    fun setUsername(string: String) {
        val db = dbHelper.writableDatabase
        val value = ContentValues().apply {
            put("username", string)
        }
        db.update("User", value, "", arrayOf())
    }

    fun setAutograph(string: String) {
        val db = dbHelper.writableDatabase
        val value = ContentValues().apply {
            put("autograph", string)
        }
        db.update("User", value, "", arrayOf())
    }

    fun setStartDate(string: String) {
        val db = dbHelper.writableDatabase
        val value = ContentValues().apply {
            put("semesterStartDate", string)
        }
        db.update("User", value, "", arrayOf())
    }
}