package com.elzaman.android.zamangameel

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.elzaman.android.zamangameel.databinding.FragmentSplashScrreenBinding
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking


class SplashScrreenFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
val binding:FragmentSplashScrreenBinding = DataBindingUtil.inflate(inflater,R.layout.fragment_splash_scrreen,container,false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        lifecycleScope.launchWhenCreated {
            goToStartFragment()
        }
    }
    private  suspend fun goToStartFragment() {
        delay(2000)
        findNavController()
            .navigate(R.id.action_splashScrreenFragment_to_startScreenFragment)
    }
}