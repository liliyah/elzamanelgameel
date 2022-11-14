package com.elzaman.android.zamangameel

import android.content.ActivityNotFoundException
import android.content.Intent
import android.icu.text.IDNA
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import com.elzaman.android.zamangameel.databinding.FragmentStartScreenBinding
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.MobileAds
import com.google.android.play.core.review.ReviewInfo
import com.google.android.play.core.review.ReviewManager
import com.google.android.play.core.review.ReviewManagerFactory
import com.google.android.play.core.review.model.ReviewErrorCode
import java.net.URI


class StartScreenFragment : Fragment() {
lateinit var manager: ReviewManager


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        val binding: FragmentStartScreenBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_start_screen, container, false)
        MobileAds.initialize(requireActivity()) {}

        val adRequest = AdRequest.Builder().build()
        binding.adView.loadAd(adRequest)
        manager= ReviewManagerFactory.create(requireContext())

        binding.imagListen.setOnClickListener {
            navigateToMainScreen()
        }
        binding.imagRateus.setOnClickListener {
            rateusIntent()
        }
        binding.imgShareapp.setOnClickListener {
            shareappIntent()
        }
        return binding.root
    }

    private fun shareappIntent() {

val shareIntent = Intent()
        shareIntent.setAction(Intent.ACTION_SEND)
        shareIntent.putExtra(Intent.EXTRA_TEXT,
            "Hey check out my app at: https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID);
        shareIntent.setType("text/plain");
        startActivity(shareIntent);
    }

    private fun requestflow(reviewInfo: ReviewInfo?) {
        if (reviewInfo!=null){

            val flow = manager.launchReviewFlow(requireActivity(), reviewInfo)
            flow.addOnCompleteListener { _ ->

            }

        }

    }

    private fun rateusIntent() {
        val request = manager.requestReviewFlow()
        request.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val reviewInfo = task.result
                requestflow(reviewInfo)
            } else {
                val reviewErrorCode = task.getException().toString()
            }
        }
    }

    private fun navigateToMainScreen() {
        findNavController().navigate(R.id.action_startScreenFragment_to_singersList)

    }

//    private fun displayFavourites() {
//        findNavController().navigate(R.id.action_startScreenFragment_to_favouriteSongsFragment)
//    }


}