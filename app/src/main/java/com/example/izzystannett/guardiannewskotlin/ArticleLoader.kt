package com.example.izzystannett.guardiannewskotlin

import android.content.Context
import android.support.v4.content.AsyncTaskLoader

class ArticleLoader(context: Context) : AsyncTaskLoader<List<NewsArticle>>(context) {

    override fun onStartLoading() {
        forceLoad()
    }


    override fun loadInBackground(): List<NewsArticle>? {
        return QueryUtils().fetchNewsArticles()
    }
}