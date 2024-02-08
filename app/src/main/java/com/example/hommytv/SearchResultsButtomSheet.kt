package com.example.hommytv

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import com.example.hommytv.databinding.ButtomSheetForSearchResultBinding
import com.example.hommytv.roomdatabase.FavTable
import com.example.hommytv.roomdatabase.HistoryTable
import com.example.hommytv.roomdatabase.WatchLaterTable
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.coroutines.launch

class SearchResultsButtomSheet(val dataBeingShown:String, val viewModelObj:TheViewModel
, var dataInDatabase: DataHolder):  BottomSheetDialogFragment() {


    lateinit var favButton:TextView
//    lateinit var views:View
    lateinit var views:ButtomSheetForSearchResultBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {


        views= ButtomSheetForSearchResultBinding.inflate(inflater, container, false)
//        views=inflater.inflate(R.layout.buttom_sheet_for_search_result, container, false)
//        favButton=views.findViewById(R.id.fav_button_modal_sheet)
        return  views.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if(dataBeingShown=="Search"){
//          making delete button on the sheet disappear if the sheet is displayed in the search results activity
            views.delete.visibility=View.GONE


        }

        else if(dataBeingShown=="Fav"){
//changing the text in delete button and making the button for adding to fav disappear
//// when the sheet is displayed in you fragment and item being clicked is from the fav table
            views.delete.text="Remove from favorites"
            views.favButtonModalSheet.visibility=View.GONE

//            adding click listner to the delete button on the sheet to delete selected item from
//            fav table
            viewModelObj.dataInFavTable().observe(viewLifecycleOwner, Observer {dataInFav->


                lifecycleScope.launch {
                    views.delete.setOnClickListener {
                        //          using this toast message for testing

                        for (item in dataInFav){
                            if(item.contentTitle==dataInDatabase.contentTitle){
                                val data=FavTable(dataInDatabase.contentTitle,dataInDatabase.imgUrl,dataInDatabase.contentId,dataInDatabase.mediaType)
                                data.id=item.id
                                lifecycleScope.launch {
                                    viewModelObj.removeFromFav(data)
                                    Toast.makeText(requireActivity(),"${data.contentTitle} has being Removed",Toast.LENGTH_SHORT).show()
                                }

                            }
                        }


                    }
                }


            })


        }

        else if(dataBeingShown=="History"){
//changing the text in delete button
//// when the sheet is displayed in you fragment and item being clicked is from the history table
            views.delete.text="Remove from History"

//            adding click listner to the delete button on the sheet to delete selected item from
//            history table
            viewModelObj.showHistory().observe(viewLifecycleOwner, Observer {dataInHistory->
                lifecycleScope.launch {
                    views.delete.setOnClickListener {


                        for (item in dataInHistory){
                            if(item.contentTitle==dataInDatabase.contentTitle){
                                val data=HistoryTable(dataInDatabase.contentTitle,dataInDatabase.imgUrl,dataInDatabase.contentId,dataInDatabase.mediaType)
                                data.id=item.id
                                lifecycleScope.launch {
                                    viewModelObj.removeFromHistory(data)
                                    Toast.makeText(requireActivity(),"${data.contentTitle} has being Removed From History",Toast.LENGTH_SHORT).show()
                                }

                            }
                        }


                    }
                }
            })


        }

        else{
//making add to watch later button disappear
//// when the sheet is displayed in you fragment and item being clicked is from the watch later table
            views.addToWatchLater.visibility=View.GONE

//            adding click listener to the delete button on the sheet to delete selected item from
//            watch later table
            viewModelObj.dataInWatchLaterTable().observe(viewLifecycleOwner, Observer { dataInWatchLater->
                lifecycleScope.launch {
                    views.delete.setOnClickListener {
                        //          using this toast message for testing

                        for (item in dataInWatchLater){
                            if(item.contentTitle==dataInDatabase.contentTitle){
                                val data=WatchLaterTable(dataInDatabase.contentTitle,dataInDatabase.imgUrl,dataInDatabase.contentId,dataInDatabase.mediaType)
                                data.id=item.id
                                lifecycleScope.launch {
                                     viewModelObj.removeFromWatchLater(data)
                                    Toast.makeText(requireActivity(),"${data.contentTitle} has being Removed ",Toast.LENGTH_SHORT).show()
                                }

                            }
                        }


                    }
                }
            })
        }


viewModelObj.dataInFavTable().observe(viewLifecycleOwner, Observer {dataInFav->

//            adding click listener to the fav button on the sheet to add selected item from
//            fav table
    views.favButtonModalSheet.setOnClickListener {




            lifecycleScope.launch {
                val data= FavTable(dataInDatabase.contentTitle,
                    dataInDatabase.imgUrl, dataInDatabase.contentId, dataInDatabase.mediaType)

//                adds the data to fav table if fav table is not empty
                for (items in dataInFav){

                    if(items.contentTitle!= dataInDatabase.contentTitle && dataInFav[dataInFav.lastIndex]==items){

                        viewModelObj.addToFav(data)
                        Toast.makeText(requireActivity(),"Added to Fav",Toast.LENGTH_SHORT).show()
                    }

                }

//                adds data to fav table if fav table is empty
                if(dataInFav.isEmpty()){
                    viewModelObj.addToFav(data)
                }


            }


    }


})


        viewModelObj.dataInWatchLaterTable().observe(viewLifecycleOwner, Observer {dataInWatchLaterTable->

//            adding click listener to the watch later button on the sheet to add selected item from
//            watch later table
            views.addToWatchLater.setOnClickListener {

                lifecycleScope.launch {
//                adds the data to watch later table if watch later table  is not empty
                    val data= WatchLaterTable(dataInDatabase.contentTitle,
                        dataInDatabase.imgUrl, dataInDatabase.contentId, dataInDatabase.mediaType)
                    for (items in dataInWatchLaterTable){

                        if(items.contentTitle!= dataInDatabase.contentTitle && dataInWatchLaterTable[dataInWatchLaterTable.lastIndex]==items){

                            viewModelObj.addToWatchLater(data)
                            Toast.makeText(requireActivity(),"Added to Watch Later",Toast.LENGTH_SHORT).show()
                        }

                    }
                    //adds the data to watch later table if watch later table  is empty
                    if(dataInWatchLaterTable.isEmpty()){
                        viewModelObj.addToWatchLater(data)
                    }

                }


            }
        })


//        features left to implement

        views.playNextInQueue.setOnClickListener {
            //          using this toast message for testing
            Toast.makeText(requireActivity(),"is Working",Toast.LENGTH_SHORT).show()
        }

        views.saveToPlaylist.setOnClickListener {
            //  Displaying sheet for playlist
            val playListSheet=PlayListSheetFrag(viewModelObj,dataInDatabase)
            playListSheet.show(parentFragmentManager,PlayListSheetFrag.TAG)
           dismiss()

        }



    }

    companion object {
        const val TAG = "SearchResultsModalSheet"
    }

}