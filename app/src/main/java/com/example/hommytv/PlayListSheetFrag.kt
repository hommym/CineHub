package com.example.hommytv

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.hommytv.databinding.FragmentPlayListSheetBinding
import com.example.hommytv.roomdatabase.PlayListItemTable
import com.example.hommytv.roomdatabase.PlayListNameTable
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch


class PlayListSheetFrag(viewModel:TheViewModel,var dataInDatabase: DataHolder) : BottomSheetDialogFragment() {


    companion object {
        const val TAG = "PlayListModalSheet"


        data class PlaylistNameAndItemHolder(val currentPlaylistName:PlayListNameTable,val playlistItemToStore:PlayListItemTable)
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

        var dataSavedInTable:List<PlayListItemTable>? =null

        val liveDataObj=   viewModel.showPlayListItem()

        val observer=Observer <List<PlayListItemTable>>{
            // setting data in play_list_item_table to dataSavedInTable
             dataSavedInTable=it

        }

        liveDataObj.observe((context as AppCompatActivity), observer)

        var updatedPlaylistNameFromPlayListNameAdapter:PlaylistNameAndItemHolder?=null

        val flowForUpdatedPlayListName=MutableSharedFlow<PlaylistNameAndItemHolder?>()

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

        //  updating a selected pl aylist in play_list_name_table and also for closing this  sheet(not yet impelemented)
            lifecycleScope.launch {

                if(updatedPlaylistNameFromPlayListNameAdapter!=null){
                    var indexOfCurrentDataInPlaylistItemTable=0
                for(item in dataSavedInTable!!){

                    if(item.name== updatedPlaylistNameFromPlayListNameAdapter!!.currentPlaylistName.name &&
                        item.contentTitle== updatedPlaylistNameFromPlayListNameAdapter!!.playlistItemToStore.contentTitle){

                        break
                    }
                    else if(dataSavedInTable!!.lastIndex==indexOfCurrentDataInPlaylistItemTable ){
//                        saving and updating data in play_list_item_table and play_list_name_table respectively
                        viewModel.addToPlayListItem(updatedPlaylistNameFromPlayListNameAdapter!!.playlistItemToStore)

                        viewModel.updatePlaylistName(updatedPlaylistNameFromPlayListNameAdapter!!.currentPlaylistName)

                        Toast.makeText(context,"Added PlayList",Toast.LENGTH_SHORT).show()
                    }
                    indexOfCurrentDataInPlaylistItemTable++
            }
                if(dataSavedInTable!!.isEmpty()){
                    //                        saving and updating data in play_list_item_table and play_list_name_table respectively
                    viewModel.addToPlayListItem(updatedPlaylistNameFromPlayListNameAdapter!!.playlistItemToStore)

                    viewModel.updatePlaylistName(updatedPlaylistNameFromPlayListNameAdapter!!.currentPlaylistName)

                    Toast.makeText(context,"Added PlayList",Toast.LENGTH_SHORT).show()
                }
                }

            }
            dismiss()

        }

    }




}