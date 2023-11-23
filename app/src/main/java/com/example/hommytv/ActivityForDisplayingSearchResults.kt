package com.example.hommytv

import android.app.SearchManager
import android.content.Intent
import android.content.SearchRecentSuggestionsProvider
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.SearchRecentSuggestions
import android.view.View
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.hommytv.databinding.ActivityForDisplayingSearchResultsBinding

class ActivityForDisplayingSearchResults : AppCompatActivity() {

    lateinit var views:ActivityForDisplayingSearchResultsBinding
    lateinit var adapter:TheAdapterForListBasedRecyclerView

    val viewModel:TheViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        views=ActivityForDisplayingSearchResultsBinding.inflate(layoutInflater)
        setContentView(views.root)

        adapter=TheAdapterForListBasedRecyclerView()

//        setting the context property
        adapter.context=this@ActivityForDisplayingSearchResults


        views.recyclerViewForSearchResults.layoutManager=LinearLayoutManager(this@ActivityForDisplayingSearchResults)



//        verifying the action and getting the data entered into the search box
        if(Intent.ACTION_SEARCH==intent.action){

            intent.getStringExtra(SearchManager.QUERY)?.also { data->

//     perform network request with  the data being returned by data variable in viewModel(Not yet implemented)
                viewModel.gettingSearchResults(data)




//                saving entered data in search history
                SearchRecentSuggestions(this@ActivityForDisplayingSearchResults
                    ,ContentProviderForSearch.AUTHORITY,ContentProviderForSearch.MODE).saveRecentQuery(data,null)


            }

        }



        viewModel.hasSearchRequestFinished.observe(this@ActivityForDisplayingSearchResults, Observer {


            if (it){

                views.loadingSpinnerForSearchResultsAcitvity.visibility= View.GONE
                views.recyclerViewForSearchResults.visibility=View.VISIBLE
//                setting adapter data
                adapter.data=viewModel.searchResults

                views.recyclerViewForSearchResults.adapter=adapter

            }

        })


    }
}