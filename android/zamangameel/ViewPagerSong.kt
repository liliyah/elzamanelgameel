package com.elzaman.android.zamangameel

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.core.view.children
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.elzaman.android.adapter.songAdapter
import com.elzaman.android.dataclass.Song
import com.elzaman.android.room.SongsDatabase
import com.elzaman.android.zamangameel.databinding.FragmentViewPagerSongBinding


class ViewPagerSong : Fragment() {
    lateinit var adapter: songAdapter
    lateinit var binding: FragmentViewPagerSongBinding
    companion object {
        var currentsong: Song? = null
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {

        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_view_pager_song, container, false)
        val application = requireNotNull(this.activity).application
        val dataSource = SongsDatabase.getInstance(application).songDataBaseDao
        val viewModelFactory = MainScreenViewModelFactory(dataSource, application)

        val SongsViewModel =
            ViewModelProvider(requireActivity(),
                viewModelFactory).get(MainScreenViewModel::class.java)

       var singerIndex = MainScreenFragment.SingerIndex
        SongsViewModel.getSongsListForParticularSinger(singerIndex)

        adapter = songAdapter((MusicItemListner {
                songitem: Song ->
            currentsong = songitem
            with(SongsViewModel) {
                currentsong(songitem)
                updatenotSelectedSongs()
                updateTheselectedsong(1, songitem.songindex!!,songitem.singerImage!!)
            }

        }), requireContext(), AlarmListner {

            showBottomSheet(it)

        })
        adapter.submitList((SongsViewModel.SongsListForParticularSinger).value)
        SongsViewModel.SongsListForParticularSinger.observe(viewLifecycleOwner, Observer {

            adapter.submitList(it.toList())

        })

       binding.recyclerview.adapter = adapter
        binding.setLifecycleOwner(this)
        return binding.root
    }

    private fun showBottomSheet(song: Song) {
        CustomBottomSheetFragment(song).show(parentFragmentManager, CustomBottomSheetFragment.TAG)
    }

   override fun onDetach() {
        requireActivity().viewModelStore.clear()
        super.onDetach()
    }
}