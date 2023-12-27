package com.example.hommytv

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.example.hommytv.databinding.FragmentCategoryBinding


class CategoryFragment : Fragment() {

    lateinit var views:FragmentCategoryBinding
    val viewModel:TheViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment

        views= FragmentCategoryBinding.inflate(inflater, container, false)
        return views.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val bundleObject=Bundle()


        views.topRatedMovieButton.setOnClickListener {

//            moving to Selected Category frag
            movingToNextFrag(bundleObject,"movie","top_rated", titleBarText = "Top Rated Movies")

        }

        views.popularMovieButton.setOnClickListener {

            //            moving to Selected Category frag
            movingToNextFrag(bundleObject,"movie","popular", titleBarText = "Popular Movies")
        }

        views.upcomingMovieButton.setOnClickListener {


            //            moving to Selected Category frag
            movingToNextFrag(bundleObject,"movie", titleBarText = "Upcoming Movies")
        }

        views.inTheatresMovieButton.setOnClickListener {

            //            moving to Selected Category frag
            movingToNextFrag(bundleObject,"movie","now_playing", titleBarText = "Movies In Theatres")
        }


        views.topRatedTvButton.setOnClickListener {

            //            moving to Selected Category frag
            movingToNextFrag(bundleObject,"tv","top_rated", titleBarText = "Top Rated Tv Series")
        }


        views.popularTvButon.setOnClickListener {

            //            moving to Selected Category frag
            movingToNextFrag(bundleObject,"tv","popular", titleBarText = "Popular Tv Series")
        }

        views.currentlyAiringTvButton.setOnClickListener {
            //            moving to Selected Category frag
            movingToNextFrag(bundleObject,"tv","on_the_air", titleBarText = "Airing Tv Series")

        }

        views.upcomingTvSeriesButton.setOnClickListener {


            //            moving to Selected Category frag
            movingToNextFrag(bundleObject,"tv", titleBarText = "Upcoming Tv Series")
        }


    }

    fun movingToNextFrag(bundleObj:Bundle,mediaType:String,category:String="none",titleBarText:String){
        bundleObj.putString("MediaType",mediaType)
        bundleObj.putString("Category",category)
        bundleObj.putString("Title",titleBarText)

        val nextFrag= SelectedCategory()

//        sending collected data to nextFrag
        nextFrag.arguments=bundleObj

//        launching nextFrag

        val ft= parentFragmentManager.beginTransaction()
        ft.setCustomAnimations(R.anim.fade_in,R.anim.fade_out,R.anim.slide_in,R.anim.slide_out)
        ft.replace( R.id.layout_for_sections,nextFrag)
        ft.addToBackStack(null)
        ft.commit()


    }


}