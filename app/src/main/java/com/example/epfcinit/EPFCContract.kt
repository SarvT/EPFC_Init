package com.example.epfcinit

import android.provider.BaseColumns

object EPFCContract {
    object EPFCEntry:BaseColumns{
            const val TABLE_NAME = "entry"
            const val COLUMN_NAME_TITLE = "title"
            const val COLUMN_NAME_SUBTITLE = "subtitle"
    }
}