package com.fstudio.topnews.fragments


import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.facebook.shimmer.ShimmerFrameLayout
import com.fstudio.topnews.MainActivity.Companion.COUNTRY_NAME
import com.fstudio.topnews.R
import com.fstudio.topnews.adapter.NewsAdapter
import com.fstudio.topnews.model.NewsModel
import com.fstudio.topnews.util.NewsApiUtilities
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.ArrayList

class All : Fragment() {
    var modelClassArrayList: ArrayList<NewsModel.Article>? = null
    lateinit var swipeRefreshLayout: SwipeRefreshLayout
    lateinit var rvAll: RecyclerView
    var adapter: NewsAdapter? = null

    //  ProgressBar progressBarNews;
    lateinit var shimmerFrameLayout: ShimmerFrameLayout
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view: View = inflater.inflate(R.layout.fragment_all, container, false)
        shimmerFrameLayout = view.findViewById(R.id.shimmerLayout)
        shimmerFrameLayout.startShimmer()
        swipeRefreshLayout = view.findViewById(R.id.allRefreshLayout)
        swipeRefreshLayout.setOnRefreshListener {
         //   modelClassArrayList = ArrayList<NewsModel.Article>()
            modelClassArrayList?.clear()
            findNews()
        }
        rvAll = view.findViewById<RecyclerView>(R.id.rvAll)
        rvAll.layoutManager = LinearLayoutManager(this.context)
        modelClassArrayList = ArrayList<NewsModel.Article>()
        adapter = NewsAdapter(requireContext(), modelClassArrayList!!)
        rvAll.adapter = adapter
        findNews()
        return view
    }

    private fun findNews() {
        NewsApiUtilities.getApiInterface().categoryNews(COUNTRY_NAME, "general")
            .enqueue(object : Callback<NewsModel?> {
                @SuppressLint("NotifyDataSetChanged")
                override fun onResponse(call: Call<NewsModel?>, response: Response<NewsModel?>) {
                    if (response.isSuccessful) {
                        response.body()?.let { modelClassArrayList!!.addAll(it.articles) }
                        adapter?.notifyDataSetChanged()
                        shimmerFrameLayout.stopShimmer()
                        shimmerFrameLayout.visibility = View.GONE
                        rvAll.visibility = View.VISIBLE
                        if (swipeRefreshLayout.isRefreshing) {
                            swipeRefreshLayout.isRefreshing = false
                            Toast.makeText(context, "Refreshed", Toast.LENGTH_SHORT).show()
                        }
                    }
                }

                override fun onFailure(call: Call<NewsModel?>, t: Throwable) {
                    if (swipeRefreshLayout.isRefreshing()) {
                        swipeRefreshLayout.setRefreshing(false)
                    }
                }
            })
    }
}