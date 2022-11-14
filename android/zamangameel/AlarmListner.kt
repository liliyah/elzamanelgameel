package com.elzaman.android.zamangameel

import com.elzaman.android.dataclass.Song

class AlarmListner (val clicklistner :(songitem: Song)-> Unit) {
    fun onClick(itemsong: Song) = clicklistner(itemsong)
}
