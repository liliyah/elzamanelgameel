package com.elzaman.android.dataclass

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "songstable")
data class Song(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "songId")
    val songId: Int,
    @ColumnInfo(name = "songName")
    val songName: String?,
    @ColumnInfo(name = "sonUri")
    val sonUri: String?,
    @ColumnInfo(name = "singerImage")
    val singerImage: Int?,
    @ColumnInfo(name = "songlyrics")
    val songlyrics: String?,
    @ColumnInfo(name = "isselected")
    var isselected: Int?,
    @ColumnInfo(name = "isfavourite")
    var isfavourite: Int?  ,
    @ColumnInfo(name = "songindex")
    val songindex:Int?)

