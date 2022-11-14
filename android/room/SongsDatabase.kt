package com.elzaman.android.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.elzaman.android.dataclass.Song

@Database(entities = [Song::class], version = 1, exportSchema = false)
abstract class SongsDatabase : RoomDatabase() {
    abstract val songDataBaseDao: SongDataBaseDao

    companion object {
        private var INSTANCE: SongsDatabase? = null
        fun getInstance(context: Context): SongsDatabase {
            synchronized(this) {
                var instance = INSTANCE
                if (instance == null) {

                    instance = Room.databaseBuilder(context.applicationContext,
                        SongsDatabase::class.java,
                        "songstable")
                        .fallbackToDestructiveMigration().createFromAsset("database/elzamanelgameel.db").build()
                    INSTANCE = instance

                }
                return instance
            }

        }
    }
}