package ec.erickmedina.sapchallenge.database.tables

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

@Entity
data class DBSearchResult(

        @ColumnInfo(name = "id")
        @PrimaryKey(autoGenerate = true)
        val id: Int,

        @ColumnInfo(name = "image_address")
        val image_address: String,

        @ColumnInfo(name = "image_tags")
        val image_tags: String,

        @ColumnInfo(name = "trash_type")
        val trash_type: String,

        @ColumnInfo(name = "trash_bin")
        val trash_bin: String,

        @ColumnInfo(name = "trash_description")
        val trash_description: String
)