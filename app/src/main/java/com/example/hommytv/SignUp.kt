package com.example.hommytv

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.hommytv.databinding.FragmentSignUpBinding


class SignUp : Fragment() {
    lateinit var views: FragmentSignUpBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        views=FragmentSignUpBinding.inflate(inflater,container,false)
        return views.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)




        views.signupBackButton.setOnClickListener{

            parentFragmentManager.popBackStack()

        }
    }



}