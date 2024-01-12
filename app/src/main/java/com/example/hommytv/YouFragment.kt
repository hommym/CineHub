package com.example.hommytv

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.hommytv.databinding.FragmentYouBinding
import com.example.hommytv.roomdatabase.FavTable
import com.example.hommytv.roomdatabase.WatchLaterTable


class YouFragment : Fragment() {

    lateinit var views:FragmentYouBinding
    lateinit var adapterForHistory:AdapterForYouFragment
    lateinit var adapterForFav:AdapterForYouFragment
    lateinit var adapterForWatchLater:AdapterForYouFragment
    val viewModel:TheViewModel by activityViewModels()

    fun toListOfDataHolder(fav:List<FavTable>?=null,
   watchLater:List<WatchLaterTable>?=null,history:List<String>?=null):ArrayList<DataHolder>{
        val listOfDataHolder= arrayListOf<DataHolder>()
        if (fav!=null){

            fav.forEach {
                val dataHolderObj=DataHolder(it.contentTitle,it.imgUrl,it.contentId,it.mediaType)
                listOfDataHolder.add(dataHolderObj)
            }

        }
        else if(watchLater!=null){

            watchLater.forEach {
                val dataHolderObj=DataHolder(it.contentTitle,it.imgUrl,it.contentId,it.mediaType)
                listOfDataHolder.add(dataHolderObj)
            }


        }
        else{

            //            not yet implemented
        }


      return listOfDataHolder
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        views= FragmentYouBinding.inflate(inflater,container, false)
        return views.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapterForWatchLater= AdapterForYouFragment()
        adapterForFav= AdapterForYouFragment()
        adapterForHistory=AdapterForYouFragment()

        adapterForFav.context=requireActivity()
        adapterForWatchLater.context=requireActivity()
        adapterForHistory.context=requireActivity()


//        changing the data type of data in fav table , watch later table and history tables
        viewModel.dataInWatchLaterTable().observe(viewLifecycleOwner, Observer {


            adapterForWatchLater.data=toListOfDataHolder(watchLater = it)
            adapterForWatchLater.notifyDataSetChanged()

        })

        viewModel.dataInFavTable().observe(viewLifecycleOwner, Observer {


            adapterForFav.data=toListOfDataHolder(fav =it )
            adapterForFav.notifyDataSetChanged()
        })


//setting adapters to recycler views
        views.recyclerviewFavorite.layoutManager=StaggeredGridLayoutManager(1,StaggeredGridLayoutManager.HORIZONTAL)
        views.recyclerviewFavorite.adapter=adapterForFav

        views.recyclerviewWatchLater.layoutManager=StaggeredGridLayoutManager(1,StaggeredGridLayoutManager.HORIZONTAL)
        views.recyclerviewWatchLater.adapter=adapterForWatchLater



    }


}