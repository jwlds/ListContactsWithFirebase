package com.example.listcontacts.ui

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.listcontacts.R
import com.example.listcontacts.database.FirebaseDao.Companion.isAuth
import com.example.listcontacts.databinding.FragmentSplashBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase


class SplashFragment : Fragment() {

    private var _binding: FragmentSplashBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSplashBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Handler(Looper.getMainLooper()).postDelayed(this::checkAuth,3000)
    }

    private fun checkAuth(){
        if(isAuth()) {
            findNavController().navigate(R.id.action_splashFragment_to_listContactsFragment)
        } else {
            findNavController().navigate(R.id.action_splashFragment_to_navigation)
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}