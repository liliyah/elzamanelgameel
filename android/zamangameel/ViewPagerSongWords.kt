package com.elzaman.android.zamangameel

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.elzaman.android.room.SongsDatabase
import com.elzaman.android.zamangameel.databinding.FragmentViewPagerSongBinding
import com.elzaman.android.zamangameel.databinding.FragmentViewPagerSongWordsBinding


class ViewPagerSongWords(val songIndex:Int) : Fragment() {
    lateinit var binding: FragmentViewPagerSongWordsBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_view_pager_song_words, container, false)
        val application = requireNotNull(this.activity).application
        val dataSource = SongsDatabase.getInstance(application).songDataBaseDao
        val viewModelFactory = MainScreenViewModelFactory(dataSource, application)
        val SongsViewModel =
            ViewModelProvider(requireActivity(), viewModelFactory).get(MainScreenViewModel::class.java)
        SongsViewModel.getFirstSong(songIndex)
        binding.textsongwords.text= SongsViewModel.firstsong?.songlyrics.toString()
        SongsViewModel.currentplayingsong.observe(viewLifecycleOwner, Observer {

            binding.textsongwords.text= it.songlyrics

        })
        return binding.root
    }
}