package com.elzaman.android.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.elzaman.android.dataclass.Song

@Dao
interface SongDataBaseDao {


    @Query("SELECT * from songstable WHERE singerImage = :value ")
    fun getAllSongsForParticularSinger(value: Int): LiveData<List<Song>>

    @Query("SELECT * from songstable WHERE songIndex = :Index  AND singerImage = :SingerIndex")
    suspend fun getSingerSong(Index: Int, SingerIndex: Int): Song

    @Query("UPDATE  songstable SET  isselected= :isSelected   WHERE songindex = :songid  AND singerImage = :SingerImageIndex")
    suspend fun setSelectedSong(isSelected: Int, songid: Int, SingerImageIndex: Int)

    @Query("UPDATE  songstable SET  isselected=  :isSelected ")
    suspend fun updateNotSelectedSongs(isSelected: Int)

    @Insert
    suspend fun insert(song: Song)
}