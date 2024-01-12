package com.example.hommytv

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.net.toUri
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import coil.load
import com.example.hommytv.databinding.FragmentSelectedMoviesOrSeriesBinding
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import okhttp3.internal.format

class SelectedMoviesOrSeriesFragment : Fragment() {



    lateinit var views:FragmentSelectedMoviesOrSeriesBinding
    val viewModel:TheViewModel by activityViewModels()
    lateinit var adapter:TheAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment

        views= FragmentSelectedMoviesOrSeriesBinding.inflate(inflater, container, false)
        return views.root
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if(context is ActivityForDisplayingSearchResults){
            activity?.onBackPressedDispatcher?.addCallback(this){
                isEnabled=true

                context.numberOfFragInBackStack--

                parentFragmentManager.popBackStack()

               context.lifecycleScope.launch {


                       context.hasAnItemBeingSelected.emit(false)


               }

            }
        }
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val bundleObj=Bundle()



//        setting nested scroll view listner
        views.nestedView.setOnScrollChangeListener { v, scrollX, scrollY, oldScrollX, oldScrollY ->

            if(scrollY>600){
                views.watchTrailerButton.visibility=View.INVISIBLE
                views.watchTrailerFloatingButton.visibility=View.VISIBLE
            }
            else{

                views.watchTrailerFloatingButton.visibility=View.INVISIBLE
                views.watchTrailerButton.visibility=View.VISIBLE
            }

        }




//        setting adapter for recommendation section
        adapter= TheAdapter()
        adapter.context=requireActivity()
        viewModel.dataInFavTable().observe(viewLifecycleOwner, Observer {

            adapter.dataInFavTable=it

        })
        viewModel.dataInWatchLaterTable().observe(viewLifecycleOwner, Observer {
            adapter.dataInWatchLaterTable=it
        })

//        setting up the format for recyclerview
        views.similarContentRecyclerView.layoutManager=StaggeredGridLayoutManager(1,StaggeredGridLayoutManager.HORIZONTAL)
        views.similarContentRecyclerView.adapter=adapter

//        setting image in imageview
        val imgUri=arguments?.getString("Poster")!!.toUri().buildUpon().scheme("https").build()
        views.contentPoster.load(imgUri)

//        setting genre
        views.contentGenre.text=arguments?.getString("Genre")


//       setting overview
        views.contentDescription.text=arguments?.getString("Overview")






        lifecycleScope.launch {


            viewModel.movieInfo.collect{

// seetting the keyword for making serach request on youtube dailogbox
                bundleObj.putString("KeywordForSearch","${if (it?.title!=null){
                    it.title
                }
                else{
                    it?.original_name
                }} Trailer")


//                getting MovieDetails object and setting data to the appropraite views

            views.contentTitle.text=if (it?.title!=null){
            it.title
            }
            else{
            it?.original_name
            }


            views.contentReleaseYear.text= if(it?.release_date!=null){
                it.release_date
            }
                else{
                    it?.first_air_date
            }

            views.contentRatedAge.text=if(it?.adult==true){
                "18+"
            }
                else{
                    "14+"
            }

            views.contentDuration.text= if(arguments?.getString("MediaType")=="movie"){
                minutesToStandardTime(it?.runtime)
            }else{
                "Seasons:${it?.number_of_seasons}"
            }



//                setting up adapter's data
                adapter.data=it!!.recommendations

//                making loadingspinner dissappear
                views.loadingSpinnerForSelectedContent.visibility=View.GONE

                views.nestedView.visibility=View.VISIBLE

                //making trailer button appear only if the trailer floating bar is invinsible
                if(views.watchTrailerFloatingButton.visibility==View.INVISIBLE){
                    views.watchTrailerButton.visibility=View.VISIBLE
                }

            }
        }

//        sending request to server for movie or series details
        viewModel.getMovieDetails(arguments?.getInt("Movie_Id"),
            arguments?.getString("MediaType")!!
        )


        views.watchTrailerButton.setOnClickListener {


//        launching dialog fragment to show the video


            val nextFrag=YouTubeDialogVidoePlayer()
            nextFrag.arguments=bundleObj
            nextFrag.show(parentFragmentManager,"YouTubeDialogVidoePlayer")



        }

        views.watchTrailerFloatingButton.setOnClickListener {

            val nextFrag=YouTubeDialogVidoePlayer()
            nextFrag.arguments=bundleObj
            nextFrag.show(parentFragmentManager,"YouTubeDialogVidoePlayer")
        }


    }


    private fun minutesToStandardTime(minutes:Int?):String{
        val hours=(minutes?.toDouble()?.div(60.0)).toString()
        var hoursToUse=""
        var remainingMinutes=""
        var seconds=""
        var hasDotCome=false
        var counter=0

        for (num in hours){

            if (num!='.' && !hasDotCome){
                hoursToUse += "$num"

            }
            else if (hasDotCome){

                if(counter<2){
                    remainingMinutes+="$num"
                }
                else{
                    if (counter>3){
                        break
                    }
                    seconds+="$num"
                }


                counter++
            }
            else{
                hasDotCome=true
            }

        }


        if(remainingMinutes==""){
            remainingMinutes="0"

        }

        if (seconds==""){
            seconds="0"
        }


        return String.format("%2d:%02d:%02d",hoursToUse.toInt(),remainingMinutes.toInt(),seconds.toInt())

    }


}