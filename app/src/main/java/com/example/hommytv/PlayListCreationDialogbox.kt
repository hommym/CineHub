package com.example.hommytv

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.lifecycleScope
import com.example.hommytv.databinding.FragmentPlayListCreationDialogboxBinding
import com.example.hommytv.roomdatabase.PlayListNameTable
import kotlinx.coroutines.launch

class PlayListCreationDialogbox : DialogFragment() {

    lateinit var views:FragmentPlayListCreationDialogboxBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        views= FragmentPlayListCreationDialogboxBinding.inflate(inflater, container, false)
        return views.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

     dialog?.setCancelable(false)


     views.createPlaylistButton.setOnClickListener {

         if(views.nameOfPlayList.text.isNotEmpty()){
         lifecycleScope.launch {

//             adding data to play_lis_name_table
             (requireActivity() as MainActivity).viewModel.addToPlayListName(PlayListNameTable(views.nameOfPlayList.text.toString(),"",0))
             dialog?.cancel()
         }
         }
         else{
             Toast.makeText(requireContext(),"No Name Entered",Toast.LENGTH_SHORT).show()
         }

     }

    views.cancelButtonForPlayListCreation.setOnClickListener {
//        closing the dialog box
        dialog?.cancel()
    }


    }
}