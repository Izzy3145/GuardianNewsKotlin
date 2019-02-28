package com.example.izzystannett.guardiannewskotlin

import android.app.Activity
import android.content.Intent
import android.content.Intent.ACTION_VIEW
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Adapter
import android.widget.ArrayAdapter
import android.widget.TextView
import kotlinx.android.synthetic.main.article_list_item.*


class ArticleAdapter(context: Activity, articles: ArrayList<NewsArticle>) : ArrayAdapter<NewsArticle>(context, 0, articles) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {

        val newsArticle = getItem(position)
        val view: View =
            convertView ?: LayoutInflater.from(parent.context).inflate(R.layout.article_list_item, parent, false)

        val articleSection: TextView = view.findViewById(R.id.article_section) as TextView
        articleSection.text = newsArticle.section
        val articleName: TextView = view.findViewById(R.id.article_name) as TextView
        articleName.text = newsArticle.articleName

        val currentView: View = view.findViewById(R.id.article_list_item) as View
        currentView.setOnClickListener {
            val intent = Intent(ACTION_VIEW)
            intent.data = Uri.parse(newsArticle.articleUrl)
            context.startActivity(intent)
        }

        return view
    }
}