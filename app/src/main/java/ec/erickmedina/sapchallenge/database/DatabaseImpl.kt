package com.example.erickmedina.androidrxtests.database

import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.content.Context
import com.example.erickmedina.androidrxtests.database.dao.DBSearchDao
import ec.erickmedina.sapchallenge.database.tables.DBSearchResult

@Database(entities = arrayOf(DBSearchResult::class), version = 1)
abstract class DatabaseImpl : RoomDatabase() {

    abstract fun dbSearchDao() : DBSearchDao

    companion object {
        private var INSTANCE: DatabaseImpl? = null
        private const val dbName = "appdatabase.db"

        fun getInstance(context: Context): DatabaseImpl? {
            if (INSTANCE == null) {
                synchronized(DatabaseImpl::class) {
                    INSTANCE = Room.databaseBuilder(context.applicationContext,
                            DatabaseImpl::class.java, dbName)
                            .allowMainThreadQueries()
                            .build()
                }
            }
            return INSTANCE
        }

        fun destroyInstance() {
            INSTANCE = null
        }
    }
}