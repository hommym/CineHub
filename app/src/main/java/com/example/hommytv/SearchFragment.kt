package com.example.hommytv

import android.app.SearchManager
import android.content.ComponentName
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.hommytv.databinding.FragmentSearchBinding


class SearchFragment : Fragment() {


    lateinit var views:FragmentSearchBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        views= FragmentSearchBinding.inflate(inflater, container, false)

        return views.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        setting the search configuration of search view
        val searcMannager= activity?.getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val component=ComponentName(requireActivity(),ActivityForDisplayingSearchResults::class.java)
        views.serchView.setSearchableInfo(searcMannager.getSearchableInfo(component))

//automatically making the search view focus and ready to take in text
        views.serchView.onActionViewExpanded()

    }

}