package com.example.hommytv

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.hommytv.databinding.FragmentPlayListSheetBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment


class PlayListSheetFrag(viewModel:TheViewModel,var dataInDatabase: DataHolder) : BottomSheetDialogFragment() {


    companion object {
        const val TAG = "PlayListModalSheet"
    }
    lateinit var views:FragmentPlayListSheetBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        views=FragmentPlayListSheetBinding.inflate(inflater, container, false)
        return views.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        views.addPlayList.setOnClickListener {

            val createPlayListDialogBox= PlayListCreationDialogbox()
            createPlayListDialogBox.show(parentFragmentManager,"PlayListCreationDialogbox")
            dismiss()
        }

    }

}