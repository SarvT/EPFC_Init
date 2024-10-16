package com.example.epfcinit

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.os.FileObserver.CREATE
import android.provider.BaseColumns
import com.example.epfcinit.EPFCContract.EPFCEntry.TABLE_NAME

class DBHelper(context:Context):SQLiteOpenHelper(context, DB_NAME, null, DB_VERSION) {


    companion object{
        const val DB_NAME = "SseEpfc.db"
        const val DB_VERSION = 1
        private const val SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS ${EPFCContract.EPFCEntry.TABLE_NAME}"
        private const val SQL_CREATE_ENTRIES =
            "CREATE TABLE ${EPFCContract.EPFCEntry.TABLE_NAME} (" +
                    "${BaseColumns._ID} INTEGER PRIMARY KEY," +
                    "${EPFCContract.EPFCEntry.COLUMN_NAME_TITLE} TEXT," +
                    "${EPFCContract.EPFCEntry.COLUMN_NAME_SUBTITLE} TEXT)"

    }

    override fun onCreate(p0: SQLiteDatabase?) {
        p0?.execSQL(Companion.SQL_CREATE_ENTRIES)
    }

    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {
        p0?.execSQL(Companion.SQL_DELETE_ENTRIES)
        onCreate(p0)
    }

    override fun onDowngrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        super.onDowngrade(db, oldVersion, newVersion)
    }

}