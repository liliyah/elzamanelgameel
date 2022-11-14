package com.elzaman.android.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.elzaman.android.zamangameel.ViewPagerSong
import com.elzaman.android.zamangameel.ViewPagerSongWords

class viewpagerAdapter(fragmentManager: FragmentManager,lifecycle: Lifecycle, val singerIndex:Int):FragmentStateAdapter(fragmentManager,lifecycle) {
    override fun getItemCount(): Int {
return 2
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> return ViewPagerSong()
            1 -> ViewPagerSongWords(singerIndex)
            else -> return ViewPagerSong()
        }
    }
}