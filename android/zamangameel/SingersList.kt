package com.elzaman.android.zamangameel

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.elzaman.android.zamangameel.databinding.FragmentSingersListBinding
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference


class SingersList : Fragment() {
    companion object {

        var SingerId = 0

    }

    private lateinit var Binding: FragmentSingersListBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        Binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_singers_list, container, false)


        Binding.ImageHalim.setOnClickListener {
            SingerId = 1
            val action = SingersListDirections.actionSingersListToMainScreenFragment(SingerId)
            findNavController().navigate(action)


        }

        Binding.imgSabah.setOnClickListener {
            SingerId = 2
            val action = SingersListDirections.actionSingersListToMainScreenFragment(SingerId)
            findNavController().navigate(action)

        }
        Binding.imgSaad.setOnClickListener {

            SingerId = 3
            val action = SingersListDirections.actionSingersListToMainScreenFragment(SingerId)
            findNavController().navigate(action)
        }

        Binding.imagLaila.setOnClickListener {
            SingerId = 4
            val action = SingersListDirections.actionSingersListToMainScreenFragment(SingerId)
            findNavController().navigate(action)

        }

        Binding.imgAsmahan.setOnClickListener {
            SingerId = 5
            val action = SingersListDirections.actionSingersListToMainScreenFragment(SingerId)
            findNavController().navigate(action)

        }
        Binding.imgMohamedAbdelwahab.setOnClickListener {
            SingerId = 6
            val action = SingersListDirections.actionSingersListToMainScreenFragment(SingerId)
            findNavController().navigate(action)

        }
        Binding.imagMohamedKandil.setOnClickListener {
            SingerId = 7
            val action = SingersListDirections.actionSingersListToMainScreenFragment(SingerId)
            findNavController().navigate(action)

        }
        Binding.imagSayed.setOnClickListener {
            SingerId = 8
            val action = SingersListDirections.actionSingersListToMainScreenFragment(SingerId)
            findNavController().navigate(action)

        }
        Binding.imagOmkalthoum.setOnClickListener {
            SingerId = 9
            val action = SingersListDirections.actionSingersListToMainScreenFragment(SingerId)
            findNavController().navigate(action)

        }
        Binding.imagShadiya.setOnClickListener {
            SingerId = 10
            val action = SingersListDirections.actionSingersListToMainScreenFragment(SingerId)
            findNavController().navigate(action)

        }
        Binding.imagMohamedFawzy.setOnClickListener {
            SingerId = 11
            val action = SingersListDirections.actionSingersListToMainScreenFragment(SingerId)
            findNavController().navigate(action)

        }
        Binding.imagFarid.setOnClickListener {

            SingerId = 12
            val action = SingersListDirections.actionSingersListToMainScreenFragment(SingerId)
            findNavController().navigate(action)
        }
        Binding.imagKarem.setOnClickListener {

            SingerId = 13
            val action = SingersListDirections.actionSingersListToMainScreenFragment(SingerId)
            findNavController().navigate(action)

        }
        Binding.imagMohamedrochdy.setOnClickListener {

            SingerId = 14
            val action = SingersListDirections.actionSingersListToMainScreenFragment(SingerId)
            findNavController().navigate(action)
        }
        return Binding.root
    }


}