package com.example.hommytv

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.addCallback
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.net.toFile
import androidx.core.net.toUri
import androidx.core.view.ViewCompat
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.transition.TransitionInflater
import coil.load
import com.example.hommytv.databinding.FragmentProfileBinding
import kotlinx.coroutines.launch
import java.io.File

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

        val dataStoreObject=App.objectOFDataStore



        //        setting shared view transition name
        ViewCompat.setTransitionName(views.profileImageForProfileFrag,"profilefrag_profile_image")
        val ft=parentFragmentManager.beginTransaction()
        ft.addSharedElement(views.profileImageForProfileFrag,"profilefrag_profile_image")
        ft.commit()


        lifecycleScope.launch {
//            setting profile image if it exist
            val dataStorObj= App.objectOFDataStore

            dataStorObj.readData.collect{

                if(it[AppDataStore.isProfileSet]==true){

                    for (file in requireActivity().filesDir.listFiles()!!){
                        if(file.name=="ProfileImage"){

                            views.profileImageForProfileFrag.setImageURI(file.absolutePath.toUri())

                        }
                    }



                }

            }


        }





        val pickProfile= registerForActivityResult(ActivityResultContracts.PickVisualMedia()){uri->


            if(uri!=null){
                views.profileImageForProfileFrag.load(uri)


               lifecycleScope.launch {
//          saving the image choosen in the app internal storage
                   requireActivity().contentResolver.openInputStream(uri).use { fileInputS->

                       requireActivity().openFileOutput("ProfileImage",Context.MODE_PRIVATE).use {

                           fileInputS?.copyTo(it)
                       }
                   }
                   dataStoreObject.writeDataToIsProfileSet(true)

//                   sending the newly set profile to server(not yet implemented)
               }

            }



        }


        views.setProfileImage.setOnClickListener {

            pickProfile.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))

        }

        views.backButtonProfile.setOnClickListener {

            parentFragmentManager.popBackStack()
            viewModel.setFragmentIsProfileValue(false)
        }

        views.logoutButton.setOnClickListener {

            parentFragmentManager.popBackStack()
            viewModel.logOut()

        }

    }




}