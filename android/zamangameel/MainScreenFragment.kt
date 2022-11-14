package com.elzaman.android.zamangameel

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.*
import android.media.AudioManager
import android.media.MediaPlayer
import android.net.ConnectivityManager
import android.net.Uri
import android.os.*
import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.session.MediaControllerCompat
import android.support.v4.media.session.PlaybackStateCompat
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import com.elzaman.android.BroadcastReceivers.checkNetworkBroadcast
import com.elzaman.android.adapter.viewpagerAdapter
import com.elzaman.android.dataclass.Song
import com.elzaman.android.room.SongsDatabase
import com.elzaman.android.zamangameel.databinding.FragmentMainScreenBinding
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.MobileAds
import com.google.android.material.tabs.TabLayoutMediator
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.lang.NullPointerException
import java.util.concurrent.TimeUnit

class MainScreenFragment : Fragment() {
    lateinit var binding: FragmentMainScreenBinding
    lateinit var SongsViewModel: MainScreenViewModel


    private lateinit var storage: FirebaseStorage
    private lateinit var storageRef: StorageReference
    val args: MainScreenFragmentArgs by navArgs()
    private val TAG = "MainScreenFragment"

    var tabletitle = arrayOf("الاغانى", "الكلمات")

    companion object {
        var mediaPlayer: MediaPlayer? = null
        var handler: Handler = Handler(Looper.getMainLooper())
        var runnable: Runnable = Runnable { }
        var mediaplayerresource: kotlin.String? = ""
        var currentsongIndex = 1
        var SingerIndex = 1
        val br: BroadcastReceiver = checkNetworkBroadcast()

        var currentplayinsong: Song? = null
    }

    init {

        mediaPlayer = MediaPlayer()
        mediaPlayer!!.setAudioStreamType(AudioManager.STREAM_MUSIC)

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        storage = FirebaseStorage.getInstance()
        storageRef = storage.reference
    }


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_main_screen, container, false)
        MobileAds.initialize(requireActivity()) {}
        val adRequest = AdRequest.Builder().build()
        binding.adView.loadAd(adRequest)
        binding.lifecycleOwner = viewLifecycleOwner
        val application = requireNotNull(this.activity).application
        val dataSource = SongsDatabase.getInstance(application).songDataBaseDao
        val viewModelFactory = MainScreenViewModelFactory(dataSource, application)
        SongsViewModel = ViewModelProvider(requireActivity(),
            viewModelFactory).get(MainScreenViewModel::class.java)
        binding.setLifecycleOwner(this)
        SingerIndex = args.singersid
        SongsViewModel.getFirstSong(SingerIndex)

        val firstSong = SongsViewModel.firstsong
        binding.textsongname.text = firstSong?.songName.toString()
        val imageResource = SongsViewModel.getCurrentSingerImage(SingerIndex)
        binding.singerImage.setImageResource(imageResource)
        val firstsongPath = firstSong?.sonUri
binding.progress.visibility= View.INVISIBLE

        currentplayinsong = firstSong
        mediaplayerresource = firstsongPath

        SongsViewModel.updatenotSelectedSongs()
        SongsViewModel.updateTheselectedsong(1, 1,SingerIndex)

        SongsViewModel.currentplayingsong.observe(viewLifecycleOwner, Observer {
            binding.textsongname.text = it.songName.toString()

            startplayingsong(it.sonUri!!)
            mediaplayerresource = it.sonUri
            currentsongIndex = it.songindex!!
            currentplayinsong = it
        })

        binding.imgPlay.setOnClickListener {

            playpauseMediaplayer()
        }
        binding.imgForward.setOnClickListener {
            playnextSong(currentsongIndex)

        }
        binding.imgBackwards.setOnClickListener {

            playpreviousSong(currentsongIndex)

        }

        binding.viewpager.adapter =
            viewpagerAdapter(requireActivity().supportFragmentManager, lifecycle,SingerIndex)
        TabLayoutMediator(binding.tablayout, binding.viewpager) { tab, position ->
            tab.text = tabletitle[position]
        }.attach()

        val filter = IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
        requireActivity().registerReceiver(br, filter)

        return binding.root
    }

   @RequiresApi(Build.VERSION_CODES.O)
    fun startplayingsong(SongPath: kotlin.String) {
        var songUri: Uri? = null
       binding.progress.visibility = View.VISIBLE
        storageRef.child(SongPath).downloadUrl.addOnSuccessListener {
            songUri = it
            Log.d(TAG, "getSongUrionsucess: $songUri")
            enablePausePlayButtons()
            startmediaplayer(songUri)

        }.addOnFailureListener {
            it.printStackTrace()
            Log.d(TAG, "getSongUrionerror: $songUri")
            disablePlayPauseButtons()
            Toast.makeText(requireContext(), "حدث شى خطا", Toast.LENGTH_SHORT).show()
            songUri = null
        }


    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onDetach() {
        clearmediaplayer()
        requireActivity().viewModelStore.clear()
        requireActivity().unregisterReceiver(br)
        super.onDetach()
    }

    @SuppressLint("DefaultLocale")
    @RequiresApi(Build.VERSION_CODES.O)
    fun startmediaplayer(songresource: Uri?) {

        clearmediaplayer()
        binding.firstanimation.visibility = View.VISIBLE
        binding.secondanimation.visibility = View.VISIBLE

       try {
            if (mediaPlayer == null) {
                mediaPlayer = MediaPlayer()
                mediaPlayer!!.setAudioStreamType(AudioManager.STREAM_MUSIC)
            }
            mediaPlayer!!.reset()
            mediaPlayer!!.setDataSource(requireActivity(), songresource!!)

            mediaPlayer!!.setOnPreparedListener {
                binding.progress.visibility = View.INVISIBLE

                mediaPlayer!!.start()
                binding.seekbar.setProgress(0)
                binding.seekbar.max = mediaPlayer!!.duration
            }

            mediaPlayer!!.prepareAsync()

            mediaPlayer!!.isLooping = false
            mediaPlayer!!.setOnCompletionListener {

                playnextSong(currentsongIndex)
            }

            mediaPlayer!!.setOnErrorListener(object : MediaPlayer.OnErrorListener {
                override fun onError(p0: MediaPlayer?, p1: Int, p2: Int): Boolean {
                    mediaPlayer!!.start()
                    return true
                }

            })
        } catch (e: Exception) {

            Log.d(TAG, "startmediaplayer: ${e.toString()}")
        }
        runnable = Runnable {

            kotlin.run {

                if (mediaPlayer != null) {

                    val currentposition = mediaPlayer!!.getCurrentPosition()
                    binding.seekbar.setProgress(currentposition.toInt())
                    val time = String.format("%02d:%02d ",
                        TimeUnit.MILLISECONDS.toMinutes(currentposition.toLong()),
                        TimeUnit.MILLISECONDS.toSeconds(currentposition.toLong()) -
                                TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(
                                    currentposition.toLong())))
                    binding.textduration.text = time
                    handler.postDelayed(runnable, 1000)
                }
            }
        }

        handler.postDelayed(runnable, 0)
        binding.imgPlay.setImageResource(R.drawable.btnpause)
        binding.seekbar.setOnSeekBarChangeListener(
            object : SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(
                    seekBar: SeekBar?,
                    progress: Int,
                    fromUser: Boolean,
                ) {
                    if (fromUser) {
                        if (mediaPlayer != null) {
                            mediaPlayer!!.seekTo(progress)
                            binding.seekbar.setProgress(progress);
                        }
                    }
                }

                override fun onStartTrackingTouch(seekBar: SeekBar?) = Unit

                override fun onStopTrackingTouch(seekBar: SeekBar?) {
                    if (mediaPlayer != null) {
                        mediaPlayer!!.seekTo(binding.seekbar.getProgress());

                    }
                }
            }
        )
    }


    fun stopmediaplayer() {
        binding.firstanimation.visibility = View.INVISIBLE
        binding.secondanimation.visibility = View.INVISIBLE
        mediaPlayer!!.stop()
        mediaPlayer!!.reset()
        mediaPlayer!!.release()
        mediaPlayer = null
        handler.removeCallbacks(runnable)
        binding.imgPlay.setImageResource(R.drawable.buttonplay)

    }

    fun enablePausePlayButtons() {

        binding.imgPlay.isEnabled = true
        binding.imgBackwards.isEnabled = true
        binding.imgForward.isEnabled = true
    }

    fun disablePlayPauseButtons() {

        binding.imgPlay.isEnabled = false
        binding.imgBackwards.isEnabled = false
        binding.imgForward.isEnabled = false
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun playpauseMediaplayer() {

        if (mediaPlayer != null && mediaPlayer!!.isPlaying) {
            stopmediaplayer()

        } else {


            startplayingsong(mediaplayerresource!!)
        }

    }

    fun playpreviousSong(currentIndexid: Int) {
        try {
            var SongIndex = 0
            if (currentIndexid == 1) {

                SongIndex = 10
            } else {

                SongIndex = currentIndexid - 1
            }
            val song = SongsViewModel.getrequiredsong(SongIndex, SingerIndex)
            SongsViewModel.currentsong(song!!)
            SongsViewModel.updatenotSelectedSongs()
            SongsViewModel.updateTheselectedsong(1, song.songindex!!,song.singerImage!!)
        } catch (e: NullPointerException) {
            e.printStackTrace()
        }

    }

    fun playnextSong(currentIndexid: Int) {
        try {

                 var SongIndex = 0
            if (currentIndexid == 10) {

                SongIndex = 1
            } else {

                SongIndex = currentIndexid + 1
            }
            val song = SongsViewModel.getrequiredsong(SongIndex, SingerIndex)

            if (song != null) {
                SongsViewModel.currentsong(song)
                SongsViewModel.updatenotSelectedSongs()
                SongsViewModel.updateTheselectedsong(1, song.songindex!!, SingerIndex)
            }


        } catch (e: NullPointerException) {
            e.printStackTrace()
        }
    }
    @RequiresApi(Build.VERSION_CODES.O)
    fun clearmediaplayer() {
        if (mediaPlayer != null && mediaPlayer!!.isPlaying) {
            mediaPlayer!!.stop()
            mediaPlayer!!.reset()
            mediaPlayer!!.release()
            mediaPlayer = null
        }

    }

}