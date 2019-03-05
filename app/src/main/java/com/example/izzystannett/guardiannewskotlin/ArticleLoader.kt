package com.example.izzystannett.guardiannewskotlin

import android.content.Context
import android.support.v4.content.AsyncTaskLoader

class ArticleLoader(context: Context, searchString: String) : AsyncTaskLoader<List<NewsArticle>>(context) {
    var searchStr: String = searchString

    override fun onStartLoading() {
        forceLoad()
    }
    override fun loadInBackground(): List<NewsArticle>? {
        return QueryUtils().fetchNewsArticles(searchStr)
    }
}