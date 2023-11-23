package com.example.hommytv

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.ViewCompat
import androidx.fragment.app.activityViewModels
import androidx.transition.TransitionInflater
import coil.load
import com.example.hommytv.databinding.FragmentProfileBinding

class ProfileFragment : Fragment() {


    lateinit var views:FragmentProfileBinding

    val viewModel:TheViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        codes to make but nav bar visible again
        activity?.onBackPressedDispatcher?.addCallback(this){
            isEnabled=true

            parentFragmentManager.popBackStack()
            viewModel.setFragmentIsProfileValue(false)

        }
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        views= FragmentProfileBinding.inflate(inflater, container, false)

        sharedElementEnterTransition = TransitionInflater.from(requireContext())
            .inflateTransition(R.transition.shared_profile)



        return views.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        //        setting shared view transition name
        ViewCompat.setTransitionName(views.profileImageForProfileFrag,"profilefrag_profile_image")

        val ft=parentFragmentManager.beginTransaction()
        ft.addSharedElement(views.profileImageForProfileFrag,"profilefrag_profile_image")
        ft.commit()





        val pickProfile= registerForActivityResult(ActivityResultContracts.PickVisualMedia()){


            if(it!=null){
                views.profileImageForProfileFrag.load(it)
            }

            else{
                views.profileImageForProfileFrag.setImageResource(R.drawable.baseline_person_24)
            }

        }


        views.setProfileImage.setOnClickListener {

            pickProfile.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))

        }

    }




}