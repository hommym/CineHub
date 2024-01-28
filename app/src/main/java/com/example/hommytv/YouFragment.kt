package com.example.hommytv

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.core.net.toUri
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.hommytv.databinding.FragmentYouBinding
import com.example.hommytv.roomdatabase.FavTable
import com.example.hommytv.roomdatabase.HistoryTable
import com.example.hommytv.roomdatabase.WatchLaterTable
import kotlinx.coroutines.launch


class YouFragment : Fragment() {

    lateinit var views:FragmentYouBinding
    lateinit var adapterForHistory:AdapterForYouFragment
    lateinit var adapterForFav:AdapterForYouFragment
    lateinit var adapterForWatchLater:AdapterForYouFragment
    val viewModel:TheViewModel by activityViewModels()

    companion object{

        fun toListOfDataHolder(fav:List<FavTable>?=null,
         watchLater:List<WatchLaterTable>?=null,history:List<HistoryTable>?=null):ArrayList<DataHolder>{
            val listOfDataHolder= arrayListOf<DataHolder>()
            if (fav!=null){

                fav.reversed().forEach {
                    val dataHolderObj=DataHolder(it.contentTitle,it.imgUrl,it.contentId,it.mediaType)
                    listOfDataHolder.add(dataHolderObj)
                }

            }
            else if(watchLater!=null){

                watchLater.reversed().forEach {
                    val dataHolderObj=DataHolder(it.contentTitle,it.imgUrl,it.contentId,it.mediaType)
                    listOfDataHolder.add(dataHolderObj)
                }


            }
            else{

                history?.reversed()?.forEach {
                    val dataHolderObj=DataHolder(it.contentTitle,it.imgUrl,it.contentId,it.mediaType)
                    listOfDataHolder.add(dataHolderObj)
                }
            }


            return listOfDataHolder
        }
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

        lifecycleScope.launch {
//            setting profile image if it exist
            val dataStorObj= App.objectOFDataStore

            dataStorObj.readData.collect{

                if(it[AppDataStore.isProfileSet]==true){

                    for (file in requireActivity().filesDir.listFiles()!!){
                        if(file.name=="ProfileImage"){

                            views.profileImage.setImageURI(file.absolutePath.toUri())

                        }
                    }



                }

            }


        }

        adapterForWatchLater= AdapterForYouFragment()
        adapterForFav= AdapterForYouFragment()
        adapterForHistory=AdapterForYouFragment()

//        setting up the data to the dataBeingShown
        adapterForFav.dataBeingShown="Fav"
        adapterForWatchLater.dataBeingShown="WatchLater"

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

        viewModel.showHistory().observe(viewLifecycleOwner, Observer {

            adapterForHistory.data=toListOfDataHolder(history = it)
            adapterForHistory.notifyDataSetChanged()
        })


//setting adapters to recycler views
        views.recyclerviewFavorite.layoutManager=StaggeredGridLayoutManager(1,StaggeredGridLayoutManager.HORIZONTAL)
        views.recyclerviewFavorite.adapter=adapterForFav

        views.recyclerviewWatchLater.layoutManager=StaggeredGridLayoutManager(1,StaggeredGridLayoutManager.HORIZONTAL)
        views.recyclerviewWatchLater.adapter=adapterForWatchLater


        views.recyclerviewHistory.layoutManager=StaggeredGridLayoutManager(1,StaggeredGridLayoutManager.HORIZONTAL)
        views.recyclerviewHistory.adapter=adapterForHistory


//        adding click listners to profile
        views.profileSectionContent.setOnClickListener {

//            moving to profile fragment
            nextfrag(ProfileFragment(),R.id.layout_for_sections,true)


        }

        views.switchAccountButton.setOnClickListener {

            parentFragmentManager.popBackStack()
            viewModel.logOut()

        }

//        adding click listeners to all buttons for view all history favorite and watch later content
        addClickListenerToViewAllButtons(views.viewAllButtonHistory,"History")
        addClickListenerToViewAllButtons(views.viewAllButtonWatchLater,"Watch Later")
        addClickListenerToViewAllButtons(views.viewAllButtonFavorite,"Favorite")




    }


    private fun addClickListenerToViewAllButtons(buttonView:Button, buttonFunction:String){

        buttonView.setOnClickListener {
            //            moving to new activity
            val intent= Intent(requireActivity(),ActivityForDisplayingSearchResults::class.java)
            intent.putExtra("YouFragmentSection",buttonFunction)
            startActivity(intent)

        }

    }

   fun nextfrag(fragObject:Fragment,layout:Int,addToStack:Boolean=false){

       val fragManager= parentFragmentManager
       val fragTransactions=fragManager.beginTransaction()

        fragTransactions.replace(layout,fragObject)
       if(addToStack){
           fragTransactions.addToBackStack(null)
       }

       fragTransactions.commit()
   }



}