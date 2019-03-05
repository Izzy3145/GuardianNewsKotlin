package com.example.izzystannett.guardiannewskotlin

import android.content.Context
import android.net.ConnectivityManager
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.LoaderManager
import android.support.v4.content.ContextCompat.getSystemService
import android.support.v4.content.Loader
import android.view.View
import android.widget.ProgressBar
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), LoaderManager.LoaderCallbacks<List<NewsArticle>> {


    private val ARTICLE_LOADER_ID = 1
    lateinit var mAdapter:ArticleAdapter
   // lateinit var searchedStr:String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mAdapter = ArticleAdapter(this, ArrayList<NewsArticle>())

        news_list_view.adapter = mAdapter
        news_list_view.emptyView = empty_view

    //    searchedStr = search_box.text.toString()

        val connMgr = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connMgr.activeNetworkInfo

        if (networkInfo != null && networkInfo.isConnected) {
            supportLoaderManager.initLoader(ARTICLE_LOADER_ID, null, this)
        } else {
            empty_view.text = resources.getString(R.string.no_internet_connection)
        }

        update_button.setOnClickListener {
           // searchedStr = search_box.text.toString()
            supportLoaderManager.restartLoader(ARTICLE_LOADER_ID, null, this)
            //supportLoaderManager.initLoader(ARTICLE_LOADER_ID, null, this)
        }
    }

    override fun onCreateLoader(p0: Int, p1: Bundle?): Loader<List<NewsArticle>> {
        return ArticleLoader(this, search_box.text.toString())
    }

    override fun onLoadFinished(p0: Loader<List<NewsArticle>>, p1: List<NewsArticle>?) {
        mAdapter.clear()
        empty_view.text = resources.getString(R.string.no_results)

        //if some articles have been found, inflate the adapter with them
        if (p1 != null && !p1.isEmpty()) {
            mAdapter.addAll(p1)
        }

        val progressBar = findViewById (R.id.progress_bar) as ProgressBar
        progressBar.visibility = View.GONE
    }


    override fun onLoaderReset(p0: Loader<List<NewsArticle>>) {
        mAdapter.clear()
    }
}
