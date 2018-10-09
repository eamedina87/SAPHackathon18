package com.example.erickmedina.androidrxtests.database.dao

import android.arch.persistence.room.*
import ec.erickmedina.sapchallenge.database.tables.DBSearchResult

@Dao
interface DBSearchDao {

    @Insert
    fun insertSearchResult(result:DBSearchResult):Long

}