package com.example.hommytv

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.hommytv.databinding.FragmentPlayListSheetBinding
import com.example.hommytv.roomdatabase.PlayListNameTable
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch


class PlayListSheetFrag(viewModel:TheViewModel,var dataInDatabase: DataHolder) : BottomSheetDialogFragment() {


    companion object {
        const val TAG = "PlayListModalSheet"
    }
    lateinit var views:FragmentPlayListSheetBinding
    lateinit var playlistAdapter:PlayListNameAdapter
    val viewModel:TheViewModel by activityViewModels()

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


        var updatedPlaylistNameFromPlayListNameAdapter:PlayListNameTable?=null
        val flowForUpdatedPlayListName=MutableSharedFlow<PlayListNameTable?>()

       lifecycleScope.launch {
           flowForUpdatedPlayListName.collect{

                    updatedPlaylistNameFromPlayListNameAdapter=it

           }
       }
        viewModel.showPlayListName().observe(viewLifecycleOwner, Observer {

//            setting up playlist names recycler views only if there is data in it's table
            if(it.isNotEmpty()){
                playlistAdapter=PlayListNameAdapter(requireActivity(),it,dataInDatabase,viewModel, flowForUpdatedPlaylistName = flowForUpdatedPlayListName)
                views.playlistNameRecyclerView.layoutManager=LinearLayoutManager(requireActivity())
                views.playlistNameRecyclerView.adapter=playlistAdapter
            }
            else{
               views.noPlaylistText.visibility=View.VISIBLE
                views.doneButton.visibility=View.GONE
            }



        })

        views.addPlayList.setOnClickListener {

            val createPlayListDialogBox= PlayListCreationDialogbox()
            createPlayListDialogBox.show(parentFragmentManager,"PlayListCreationDialogbox")
            dismiss()
        }



        views.doneButton.setOnClickListener {

        //  updating a selected playlist in play_list_name_table and also for closing this  sheet(not yet impelemented)
            lifecycleScope.launch {
                if(updatedPlaylistNameFromPlayListNameAdapter!=null){
                    viewModel.updatePlaylistName(updatedPlaylistNameFromPlayListNameAdapter!!)
                }

            }
            dismiss()

        }

    }




}