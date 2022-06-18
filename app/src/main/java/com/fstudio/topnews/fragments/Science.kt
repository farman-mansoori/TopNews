package com.fstudio.topnews.fragments

import android.annotation.SuppressLint
import com.fstudio.topnews.model.NewsModel.Article
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import androidx.recyclerview.widget.RecyclerView
import com.fstudio.topnews.adapter.NewsAdapter
import com.facebook.shimmer.ShimmerFrameLayout
import android.view.LayoutInflater
import android.view.ViewGroup
import android.os.Bundle
import android.view.View
import com.fstudio.topnews.R
import androidx.recyclerview.widget.LinearLayoutManager
import com.fstudio.topnews.util.NewsApiUtilities
import com.fstudio.topnews.model.NewsModel
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.fstudio.topnews.MainActivity.Companion.COUNTRY_NAME
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.ArrayList

class Science : Fragment() {
    var modelClassArrayList: ArrayList<Article>? = null
    lateinit var swipeRefreshLayout: SwipeRefreshLayout
    lateinit var rvAll: RecyclerView
    var adapter: NewsAdapter? = null

    //   ProgressBar progressBarNews;
    lateinit var shimmerFrameLayout: ShimmerFrameLayout
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_science, container, false)
        swipeRefreshLayout = view.findViewById(R.id.scienceRefreshLayout)
        swipeRefreshLayout.setOnRefreshListener {
            modelClassArrayList = ArrayList()
            findNews()
        }

        //  progressBarNews = view.findViewById(R.id.progressBarNewsScience);
        //  progressBarNews.setMax(100); //Progress bar
        shimmerFrameLayout = view.findViewById(R.id.shimmerLayoutScience)
        shimmerFrameLayout.startShimmer()
        rvAll = view.findViewById(R.id.rvScience)
        rvAll.layoutManager = LinearLayoutManager(context)
        modelClassArrayList = ArrayList()
        adapter = NewsAdapter(requireContext(), modelClassArrayList!!)
        rvAll.adapter = adapter
        findNews()
        return view
    }

    private fun findNews() {
        NewsApiUtilities.getApiInterface().categoryNews(COUNTRY_NAME, "science")
            .enqueue(object : Callback<NewsModel> {
                @SuppressLint("NotifyDataSetChanged")
                override fun onResponse(call: Call<NewsModel>, response: Response<NewsModel>) {
                    if (response.isSuccessful) {
                        modelClassArrayList!!.addAll(response.body()!!.articles)
                        adapter!!.notifyDataSetChanged()
                        shimmerFrameLayout.stopShimmer()
                        shimmerFrameLayout.visibility = View.GONE
                        rvAll.visibility = View.VISIBLE
                        if (swipeRefreshLayout.isRefreshing) {
                            swipeRefreshLayout.isRefreshing = false
                            Toast.makeText(context, "Refreshed", Toast.LENGTH_SHORT).show()
                        }
                        //   progressBarNews.setVisibility(View.GONE);
                        //  progressBarNews.setProgress(100);
                    }
                }

                override fun onFailure(call: Call<NewsModel>, t: Throwable) {}
            })
    }
}