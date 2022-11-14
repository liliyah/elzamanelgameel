package com.elzaman.android.zamangameel

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.elzaman.android.dataclass.Song
import com.elzaman.android.room.SongDataBaseDao
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class MainScreenViewModel(val database: SongDataBaseDao, application: Application) :
    AndroidViewModel(application) {

    private val mutablecurrentplayingsong = MutableLiveData<Song>()
    val currentplayingsong: LiveData<Song> get() = mutablecurrentplayingsong
    lateinit var SongsListForParticularSinger: LiveData<List<Song>>


    var thesong: Song? = null

    var firstsong: Song? = null


    fun getFirstSong(SingerImage: Int) {
        runBlocking {

            firstsong = getSong(1,SingerImage)

        }

    }

    fun getCurrentSingerImage(singerImageid: Int): Int {
        var image = 0
        when (singerImageid) {
            1 -> image = R.drawable.halim
            2 -> image =R.drawable.sabahtry
            3 ->image = R.drawable.saadabdelwahab
            4 -> image =R.drawable.laylamorad
            5 -> image =R.drawable.asmahantry
            6 -> image =R.drawable.abdelwahab
            7 -> image =R.drawable.mohamedkandil
            8 ->image = R.drawable.sayedmekkawy
            9 ->image = R.drawable.kalthoumtry
            10 ->image = R.drawable.shadiyatry
            11 ->image = R.drawable.fawzytry
            12 ->image = R.drawable.faridtry
            13 ->image = R.drawable.karemmahoud
            14 -> image =R.drawable.mohamedroshdy

        }

        return image


    }


    fun getSongsListForParticularSinger(singer: Int) {

        SongsListForParticularSinger = database.getAllSongsForParticularSinger(singer)

    }


    fun currentsong(song: Song) {
        mutablecurrentplayingsong.value = song

    }


    fun updateTheselectedsong(isSelected: Int, SongIndex: Int,SingerImage:Int) {

        runBlocking {

            updateselectedSong(isSelected, SongIndex,SingerImage)
        }

    }

    private suspend fun updateselectedSong(isselected: Int, SongIndex: Int, SingerImage :Int) {
        database.setSelectedSong(isselected, SongIndex,SingerImage)
    }

    fun updatenotSelectedSongs() {
        runBlocking {
            updatesallSongs()
        }

    }

    fun getrequiredsong(SongIndex: Int,singerImageIndex:Int): Song? {

        runBlocking {

            thesong = getSong(SongIndex,singerImageIndex)
        }
        return thesong
    }

    private suspend fun updatesallSongs() {
        database.updateNotSelectedSongs(0)
    }

    private suspend fun getSong( SongIndex:Int,  singerImageIndex: Int): Song {
        var song = database.getSingerSong(SongIndex, singerImageIndex)
        return song
    }


}