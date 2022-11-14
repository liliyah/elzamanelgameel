package com.elzaman.android.zamangameel

import android.view.View
import com.elzaman.android.dataclass.Song

class MusicItemListner(val clicklistner :(songitem:Song)-> Unit) {
fun onClick(itemsong:Song)=clicklistner(itemsong)

}

