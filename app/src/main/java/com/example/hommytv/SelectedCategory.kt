package com.example.hommytv

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.hommytv.databinding.FragmentSelectedCategoryBinding
import kotlinx.coroutines.launch


class SelectedCategory : Fragment() {

    lateinit var views: FragmentSelectedCategoryBinding
    lateinit var adapter:TheAdapter
    val viewModel:TheViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View{
        views= FragmentSelectedCategoryBinding.inflate(inflater, container, false)
        return views.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter= TheAdapter()
       lifecycleScope.launch {

           launch {
               viewModel.selectedCategoryData.collect{

                   adapter.data=it
                   views.categoryRecyclerView.adapter=adapter

//               making recyclerview appear and loadingSpinner dissappear
                   views.loadingSpinnerForSelectedCategory.visibility=View.GONE
                   views.categoryRecyclerView.visibility=View.VISIBLE



               }
           }

           viewModel.hasNetworkRequestFailed.collect{


//          codes showing the views that appear during a network error(not implemented)
               setVisiblity(View.GONE,View.VISIBLE)

           }

       }


        views.refreshButton.setOnClickListener {

//            making loading spinner appear as the views showing network error disappear
            setVisiblity(View.VISIBLE,View.GONE)
            //        making network request
            viewModel.getDataForSelectedCategory(arguments?.getString("MediaType")!!,
                arguments?.getString("Category")!!)

        }


        //        setting adapter context
        adapter.context=requireActivity()

//        setting the text of title bar
        views.categoryTopbar.text=arguments?.getString("Title")

//        setting up recyclerview
        views.categoryRecyclerView.layoutManager=StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL)


//        making network request
        viewModel.getDataForSelectedCategory(arguments?.getString("MediaType")!!,
            arguments?.getString("Category")!!)



    }



    private fun setVisiblity(visibilityModeForSpinner:Int,visibilityModeForOthers:Int){

        views.loadingSpinnerForSelectedCategory.visibility=visibilityModeForSpinner
        views.noInternetIcon.visibility=visibilityModeForOthers
        views.textView13.visibility=visibilityModeForOthers
        views.refreshButton.visibility=visibilityModeForOthers



    }
}