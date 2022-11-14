package com.elzaman.android.zamangameel

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.elzaman.android.dataclass.Song
import com.elzaman.android.zamangameel.databinding.ButtomSheetDialogueBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.io.BufferedInputStream
import java.io.File
import java.io.FileInputStream
import java.io.IOException
import java.util.jar.Manifest
import kotlin.math.log


class CustomBottomSheetFragment(song: Song) : BottomSheetDialogFragment() {

    private var currentsong = song
    private var isRingtone = 0
    private var isNotification = 0
    private var isAlarm = 0
    private var songFile: File? = null
    private val TAG = "CustomBottomSheetFragme"
    private lateinit var storageRef: StorageReference
    private lateinit var storage: FirebaseStorage
    private lateinit var binding: ButtomSheetDialogueBinding
    private var WritePermissionGranted = false
    lateinit var permissionLauncher: ActivityResultLauncher<String>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        songFile = getFile(currentsong.songId, currentsong.sonUri!!)
        Log.d(TAG, "onCreate:${currentsong.songId} ")

        permissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission())
        {

            if (it == true) {
                WritePermissionGranted = it
                            }
        }

    }

    @SuppressLint("UseCompatLoadingForDrawables")
    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding =
            DataBindingUtil.inflate(inflater,
                com.elzaman.android.zamangameel.R.layout.buttom_sheet_dialogue,
                container,
                false)
        binding.textsingername.text = currentsong.songName.toString()
        binding.imagealarm.setOnClickListener {
            isRingtone = 0
            isNotification = 0
            isAlarm = 1
            checkSystemWriteSettings()
            binding.imagealarm.setImageDrawable(requireContext().resources.getDrawable(com.elzaman.android.zamangameel.R.drawable.alarmgreen))
            binding.imagecall.setImageDrawable(requireContext().resources.getDrawable(com.elzaman.android.zamangameel.R.drawable.yellowringtone))
            binding.imagenotification.setImageDrawable(requireContext().resources.getDrawable(com.elzaman.android.zamangameel.R.drawable.yellownotification))

        }
        binding.imagecall.setOnClickListener {
            isRingtone = 1
            isNotification = 0
            isAlarm = 0
            binding.imagealarm.setImageDrawable(requireContext().resources.getDrawable(com.elzaman.android.zamangameel.R.drawable.yellowalarm))
            binding.imagecall.setImageDrawable(requireContext().resources.getDrawable(com.elzaman.android.zamangameel.R.drawable.phoneringtonegreen))
            binding.imagenotification.setImageDrawable(requireContext().resources.getDrawable(com.elzaman.android.zamangameel.R.drawable.yellownotification))


        }
        binding.imagenotification.setOnClickListener {
            isRingtone = 0
            isNotification = 1
            isAlarm = 0
            binding.imagealarm.setImageDrawable(requireContext().resources.getDrawable(com.elzaman.android.zamangameel.R.drawable.yellowalarm))
            binding.imagecall.setImageDrawable(requireContext().resources.getDrawable(com.elzaman.android.zamangameel.R.drawable.yellowringtone))
            binding.imagenotification.setImageDrawable(requireContext().resources.getDrawable(com.elzaman.android.zamangameel.R.drawable.notificationgreen))


        }
        binding.btnconfirm.setOnClickListener {
            if (checkSystemWriteSettings() && updateOrRequestWritePermissions()) {

                if (isNotification == 1) {
                    saveasRingtoneNotificationsOrAlarm(songFile!!,
                        RingtoneManager.TYPE_NOTIFICATION)

                } else if (isAlarm == 1) {
                    saveasRingtoneNotificationsOrAlarm(songFile!!, RingtoneManager.TYPE_ALARM)


                } else if (isRingtone == 1) {

                    saveasRingtoneNotificationsOrAlarm(songFile!!, RingtoneManager.TYPE_RINGTONE)

                } else if (isAlarm == 0 && isRingtone == 0 && isNotification == 0) {
                    Toast.makeText(requireContext(),
                        "يجب اختيار اذا كنت ترغب بوضع الرنه رنين او منبه او اشعارات",
                        Toast.LENGTH_SHORT).show()

                }

            } else {

                Toast.makeText(requireContext(),
                    "يجب اعطاء الاذن للتطبيق بالتحكم فى نغمات الهاتف",
                    Toast.LENGTH_SHORT).show()
            }

        }
        binding.butncancel.setOnClickListener {

            dismiss()
        }
        return binding.root
    }

    companion object {

        const val TAG = "CustomBottomSheetDialogFragment"
        private const val theTAG = "CustomBottomSheetFragme"

    }

    private fun updateOrRequestWritePermissions(): Boolean {


        val hasWritePermission = ContextCompat.checkSelfPermission(requireContext(),
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
        val minsdk29andabove = Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q
        val value = Build.VERSION.SDK_INT
        Log.d(TAG, "nuildversion$value ")
        WritePermissionGranted = minsdk29andabove || hasWritePermission
        if (!WritePermissionGranted) {
            permissionLauncher.launch(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
        }
        if (!WritePermissionGranted)
            return false
        else
            return true


    }

    @RequiresApi(Build.VERSION_CODES.M)
    fun checkSystemWriteSettings(): Boolean {
        if (Settings.System.canWrite(context)){
            Log.d(TAG, "checkSystemWriteSettings: true")
            return true;

        }
        else {
            Log.d(TAG, "checkSystemWriteSettings: false")

            openAndroidPermissionsMenu()
            return false
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun openAndroidPermissionsMenu() {
        val intent = Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS)
        intent.data = Uri.parse("package:" + requireContext().packageName)
        requireContext().startActivity(intent)
    }

    fun saveasRingtoneNotificationsOrAlarm(songFile: File, type: Int) {

        val uriAudio: Uri?
        val audio = sdk29andup {

            MediaStore.Audio.Media.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY)

        } ?: MediaStore.Audio.Media.EXTERNAL_CONTENT_URI

        val contentValues = ContentValues().apply {
            put(MediaStore.Audio.Media.ARTIST, "Some Artist")
            put(MediaStore.Audio.Media.TITLE, "test")
            put(MediaStore.MediaColumns.MIME_TYPE, "audio/mp3")
            if (RingtoneManager.TYPE_RINGTONE == type) {
                put(MediaStore.Audio.Media.IS_RINGTONE, true)
            } else if (RingtoneManager.TYPE_NOTIFICATION == type) {
                put(MediaStore.Audio.Media.IS_NOTIFICATION, true)
            } else if (RingtoneManager.TYPE_ALARM == type) {
                put(MediaStore.Audio.Media.IS_ALARM, true)
            }

            put(MediaStore.Audio.Media.IS_MUSIC, false)

        }
        try {
            uriAudio = requireActivity().contentResolver.insert(audio, contentValues)
            requireActivity().contentResolver.openOutputStream(uriAudio!!).use {

                    outstream ->
                val size = songFile.length().toString().toInt()
                val bytes = ByteArray(size)
                try {
                    val buf = BufferedInputStream(FileInputStream(songFile))
                    buf.read(bytes, 0, bytes.size)
                    buf.close()
                    outstream?.write(bytes)
                    outstream?.close()
                    outstream?.flush()
                    Toast.makeText(requireContext(), "تمت العمليه بنجاح", Toast.LENGTH_SHORT).show()


                } catch (e: IOException) {
                    e.printStackTrace()
                    Toast.makeText(requireContext(), "حدث خطا ما ", Toast.LENGTH_SHORT).show()
                }
            }

            RingtoneManager.setActualDefaultRingtoneUri(
                requireActivity(),
                RingtoneManager.TYPE_RINGTONE,
                uriAudio!!)

        } catch (e: IOException) {


            e.printStackTrace()

        }

    }

    private fun getFile(id: Int, childReference: String): File {
        storage = FirebaseStorage.getInstance()
        storageRef = storage.reference
        File(requireActivity().getExternalFilesDir(Environment.DIRECTORY_RINGTONES),
            "song.mp3")
        val islandRef = storageRef.child(childReference)

        val localFile = File(requireActivity().getExternalFilesDir(Environment.DIRECTORY_RINGTONES),
            "song.mp3")

        islandRef.getFile(localFile).addOnSuccessListener {

        }.addOnFailureListener {
            // Handle any errors
        }

        return localFile
    }

}

