package com.example.hommytv

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import coil.load
import com.example.hommytv.databinding.FragmentSelectedMoviesOrSeriesBinding
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import okhttp3.internal.format

class SelectedMoviesOrSeriesFragment : Fragment() {



    lateinit var views:FragmentSelectedMoviesOrSeriesBinding
    val viewModel:TheViewModel by activityViewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment

        views= FragmentSelectedMoviesOrSeriesBinding.inflate(inflater, container, false)
        return views.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        setting image in imageview
        val imgUri=arguments?.getString("Poster")!!.toUri().buildUpon().scheme("https").build()
        views.contentPoster.load(imgUri)

//        setting genre
        views.contentGenre.text=arguments?.getString("Genre")


//       setting overview
        views.contentDescription.text=arguments?.getString("Overview")






        lifecycleScope.launch {


            viewModel.movieInfo.collect{





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




//                making loadingspinner dissappear
                views.loadingSpinnerForSelectedContent.visibility=View.GONE

                views.nestedView.visibility=View.VISIBLE
            }
        }

//        sending request to server for movie or series details
        viewModel.getMovieDetails(arguments?.getInt("Movie_Id"),
            arguments?.getString("MediaType")!!
        )


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