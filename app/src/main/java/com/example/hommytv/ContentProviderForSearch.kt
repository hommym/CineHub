package com.example.hommytv

import android.content.SearchRecentSuggestionsProvider

class ContentProviderForSearch():SearchRecentSuggestionsProvider(){

init {
    setupSuggestions(AUTHORITY,MODE)

}

    companion object{

        const val AUTHORITY="com.example.hommytv.ContentProviderForSearch"

        const val MODE=SearchRecentSuggestionsProvider.DATABASE_MODE_QUERIES

    }

}