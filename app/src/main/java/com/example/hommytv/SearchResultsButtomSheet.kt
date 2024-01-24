package com.example.hommytv

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import android.widget.Toast
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class SearchResultsButtomSheet():  BottomSheetDialogFragment() {


    lateinit var favButton:TextView
    lateinit var views:View

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        views=inflater.inflate(R.layout.buttom_sheet_for_search_result, container, false)
        favButton=views.findViewById(R.id.fav_button_modal_sheet)
        return  views
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

      favButton.setOnClickListener {

//          using this toast message for testing
          Toast.makeText(requireActivity(),"is Working",Toast.LENGTH_SHORT).show()
      }



    }

    companion object {
        const val TAG = "SearchResultsModalSheet"
    }

}