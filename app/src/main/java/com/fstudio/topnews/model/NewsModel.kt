package com.fstudio.topnews.model

import java.util.*

class NewsModel(val status: String, val totalResults: Int, val articles: ArrayList<Article>) {

    inner class Source(val id: String, val name: String)
    inner class Article(
        val source: Source,
        val author: String,
        val title: String,
        val description: String,
        val url: String,
        val urlToImage: String,
        val publishedAt: Date,
        val content: String

    )

}