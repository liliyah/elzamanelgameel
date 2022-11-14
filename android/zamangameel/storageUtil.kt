package com.elzaman.android.zamangameel

import android.os.Build

inline fun <T>sdk29andup(onsdk29:()->T):T? {
    return  if (Build.VERSION.SDK_INT>= Build.VERSION_CODES.Q){
        onsdk29()
    }else{
        return null
    }
}