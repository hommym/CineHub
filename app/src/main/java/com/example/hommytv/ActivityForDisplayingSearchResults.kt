package com.example.hommytv

import android.app.SearchManager
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.SearchRecentSuggestions
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.hommytv.databinding.ActivityForDisplayingSearchResultsBinding
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch

class ActivityForDisplayingSearchResults : AppCompatActivity() {

    lateinit var views:ActivityForDisplayingSearchResultsBinding
    lateinit var adapter:TheAdapterForListBasedRecyclerView

    val viewModel:TheViewModel by viewModels()
    val hasAnItemBeingSelected= MutableSharedFlow<Boolean>()
    var numberOfFragInBackStack=0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)









//        setting up the collector for hasAnItemBeingSelected
        lifecycleScope.launch {
            hasAnItemBeingSelected.collect{

                if(it){
                    views.statusBarTitle.visibility=View.INVISIBLE
                    views.recyclerViewForSearchResults.visibility=View.INVISIBLE
                }
                else if(numberOfFragInBackStack==0){
                    views.statusBarTitle.visibility=View.VISIBLE
                    views.recyclerViewForSearchResults.visibility=View.VISIBLE
                }


            }


        }

        views=ActivityForDisplayingSearchResultsBinding.inflate(layoutInflater)
        setContentView(views.root)

        adapter=TheAdapterForListBasedRecyclerView()

//        setting the context property
        adapter.context=this@ActivityForDisplayingSearchResults
        viewModel.context=this@ActivityForDisplayingSearchResults
       viewModel.applicationContext=application



        views.recyclerViewForSearchResults.layoutManager=LinearLayoutManager(this@ActivityForDisplayingSearchResults)



//        verifying the action and getting the data entered into the search box
        if(Intent.ACTION_SEARCH==intent.action){

            intent.getStringExtra(SearchManager.QUERY)?.also { data->

//     perform network request with  the data being returned by data variable in viewModel(Not yet implemented)
                viewModel.gettingSearchResults(data)




//                saving entered data in search history
                SearchRecentSuggestions(this@ActivityForDisplayingSearchResults
                    ,ContentProviderForSearch.AUTHORITY,ContentProviderForSearch.MODE).saveRecentQuery(data,null)



                viewModel.hasSearchRequestFinished.observe(this@ActivityForDisplayingSearchResults, Observer {


                    if (it){

                        views.loadingSpinnerForSearchResultsAcitvity.visibility= View.GONE
                        views.recyclerViewForSearchResults.visibility=View.VISIBLE
//                setting adapter data
                        adapter.dataForSearch=viewModel.searchResults

                        views.recyclerViewForSearchResults.adapter=adapter

                    }

                })
            }

        }
        else{
            adapter.adapterNotForSearchResult=true
            adapter.listTypeInYouFragment=intent.getStringExtra("YouFragmentSection")!!
            views.statusBarTitle.text=intent.getStringExtra("YouFragmentSection")!!
            if(intent.getStringExtra("YouFragmentSection")=="History"){

                val data= viewModel.showHistory().observe(this@ActivityForDisplayingSearchResults,
                    Observer {

                       adapter.dataFromDatabase= YouFragment.toListOfDataHolder(history = it)
                        adapter.notifyDataSetChanged()

                    })
            }
            else if(intent.getStringExtra("YouFragmentSection")=="Favorite"){

                val data= viewModel.dataInFavTable().observe(this@ActivityForDisplayingSearchResults,
                    Observer {

                        adapter.dataFromDatabase= YouFragment.toListOfDataHolder(fav = it)
                        adapter.notifyDataSetChanged()


                    })

            }
            else if(intent.getStringExtra("YouFragmentSection")=="Playlist"){


                viewModel.showPlayListItem().observe(this@ActivityForDisplayingSearchResults,
                    Observer {

                        val data= arrayListOf<DataHolder>()

                        it.forEach {item->
                            if(intent.getStringExtra("PlaylistName")==item.name){

                                    data.add(DataHolder(item.contentTitle,item.imgUrl,item.contentId,item.mediaType))
                            }
                        }

                        adapter.dataFromDatabase=data
                        adapter.notifyDataSetChanged()



                    })


            }

            else{
                val data= viewModel.dataInWatchLaterTable().observe(this@ActivityForDisplayingSearchResults,
                    Observer {

                        adapter.dataFromDatabase= YouFragment.toListOfDataHolder(watchLater = it)
                        adapter.notifyDataSetChanged()


                    })

            }


            views.loadingSpinnerForSearchResultsAcitvity.visibility= View.GONE
            views.recyclerViewForSearchResults.visibility=View.VISIBLE
            views.recyclerViewForSearchResults.adapter=adapter
        }





    }


}