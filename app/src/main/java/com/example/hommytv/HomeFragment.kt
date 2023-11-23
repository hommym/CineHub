package com.example.hommytv

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.core.view.ViewCompat
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import coil.load
import com.example.hommytv.databinding.FragmentHomeBinding
import com.example.hommytv.retrofitdataclasses.MoviesList
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class HomeFragment : Fragment() {


    lateinit var views: FragmentHomeBinding
    lateinit var adapterRecentMovies: TheAdapter
    lateinit var adapterTopRatedSeries: TheAdapter
    lateinit var adapterUpcomingMovies: TheAdapter
    lateinit var adapterAiringTvSeries: TheAdapter
    val viewModel:TheViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
         views=FragmentHomeBinding.inflate(inflater,container,false)
        return views.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)





//        setting shared view transition name
        ViewCompat.setTransitionName(views.profileImage,"homefrag_profile_image")




//        inserting image into main image
      val imgUri= viewModel.returnedImages.first().toUri().buildUpon()?.scheme("https")?.build()
       views.homeMainImage1.load(imgUri){
           placeholder(R.drawable.baseline_image_24)
       }

        val imgUri1= viewModel.returnedImages!![1].toUri().buildUpon()?.scheme("https")?.build()
        views.homeMainImage2.load(imgUri1){
            placeholder(R.drawable.baseline_image_24)
        }


//        codes for changing images when it is being swiped automatically
        lifecycleScope.launch {

            while (true){

                var currentImageIndexInList=19
                var imageViewToSetImage=views.homeMainImage1


                while (currentImageIndexInList!=-1){
                    delay(41000L)
                    val imgUriT= viewModel.returnedImages!![currentImageIndexInList].toUri().buildUpon()?.scheme("https")?.build()
                    imageViewToSetImage.load(imgUriT){
                        placeholder(R.drawable.baseline_image_24)
                    }

                    if (imageViewToSetImage==views.homeMainImage1){

                        imageViewToSetImage=views.homeMainImage2
                    }
                    else{

                        imageViewToSetImage=views.homeMainImage1
                    }

                    currentImageIndexInList--
                }


            }


        }





//        adding a click listen to profile icon

        views.profileImage.setOnClickListener {

//            movingInto profile fragement
            val fragT=parentFragmentManager.beginTransaction()
            fragT.addSharedElement(views.profileImage,"homefrag_profile_image")
            fragT.replace(R.id.layout_for_sections,ProfileFragment())
            fragT.addToBackStack(null)
            fragT.commit()
            viewModel.setFragmentIsProfileValue(true)

        }



//      instantiating the adapter class for recent
        adapterRecentMovies= TheAdapter()
//        adding adapter to recycler view
        recyclerViewSetUp(views.recentMovieRecyclerView,adapterRecentMovies,
        viewModel.returnedRecentMovies?.results!!)








//       instantiating the adapter class for top rated
        adapterTopRatedSeries= TheAdapter()
        adapterTopRatedSeries.section="Top Rated"
//        adding adapter to recycler view
        recyclerViewSetUp( views.topRatedSeriesRecyclerView,adapterTopRatedSeries,
        viewModel.returnedTopRatedSeries?.results!!)






//       instantiating the adapter class for upcoming
       adapterUpcomingMovies= TheAdapter()
        adapterUpcomingMovies.section="Upcoming"
//        adding adapter to recycler view
        recyclerViewSetUp(views.upcomingMovieRecyclerView,adapterUpcomingMovies,
        viewModel.returnedUpcomingMovies?.results!!)



// instantiating the adapter class for airing series
        adapterAiringTvSeries= TheAdapter()
        adapterAiringTvSeries.section="Airing"
        recyclerViewSetUp(views.airingSeriesRecyclerView,adapterAiringTvSeries,
        viewModel.returnedCurrentlyAiringSeries?.results!!)







    }


    private fun recyclerViewSetUp(
    view:RecyclerView,
    viewAdapter:TheAdapter,
    data:ArrayList<MoviesList>){

//     passing in the context and data to adapter
viewAdapter.context=requireActivity()
viewAdapter.data=data

//    setting the layout style and adapter for recycler view
view.layoutManager=StaggeredGridLayoutManager(1,StaggeredGridLayoutManager.HORIZONTAL)
view.adapter=viewAdapter

    }


    }
