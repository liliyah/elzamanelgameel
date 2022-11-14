package com.elzaman.android.Utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.ConnectivityManager.TYPE_ETHERNET
import android.net.ConnectivityManager.TYPE_WIFI
import android.net.NetworkCapabilities.*
import android.os.Build
import android.provider.ContactsContract.CommonDataKinds.Email.TYPE_MOBILE
import androidx.core.content.getSystemService

class connectivity {
    companion object{

fun iscoonectedToTheInternet(context: Context):Boolean{
val connectivitymanager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
    val activenetwork= connectivitymanager.activeNetwork ?: return false
    val capabilities = connectivitymanager.getNetworkCapabilities(activenetwork) ?: return false
    return  when{
capabilities.hasTransport(TRANSPORT_WIFI) -> return true

        capabilities.hasTransport(TRANSPORT_CELLULAR) -> return true
        capabilities.hasTransport(TRANSPORT_ETHERNET) -> return true
else->false
    }
}else{

    connectivitymanager.activeNetworkInfo?.run {
        when(type){
TYPE_WIFI -> true
            TYPE_MOBILE-> true
            TYPE_ETHERNET -> true

else -> false
        }
    }


}

    return false
}

    }
}