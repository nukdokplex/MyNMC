package ru.nukdotcom.mynmc.ui.news

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import okhttp3.*
import org.jsoup.Jsoup
import ru.nukdotcom.mynmc.R
import ru.nukdotcom.mynmc.helpers.EndlessRecyclerViewScrollListener
import ru.nukdotcom.mynmc.helpers.JavaUtils.backgroundToast
import ru.nukdotcom.mynmc.helpers.NewsRecyclerViewAdapter
import ru.nukdotcom.mynmc.models.news.NewsElement
import java.io.IOException
import java.lang.Exception

class NewsFragment : Fragment() {

    companion object {
        public val NEWS_PER_PAGE = 12

        @JvmStatic
        fun newInstance() = NewsFragment()
    }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    public lateinit var recyclerView: RecyclerView
    public lateinit var adapter: NewsRecyclerViewAdapter
    public lateinit var srl: SwipeRefreshLayout
    public lateinit var mView: View
    public var currentPage = 0
    private lateinit var client: OkHttpClient


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mView = inflater.inflate(R.layout.fragment_news, container, false)

        recyclerView = mView.findViewById<RecyclerView>(R.id.newsRecyclerView)
        adapter = NewsRecyclerViewAdapter(ArrayList())

        val layoutManager = LinearLayoutManager(requireContext())

        srl = mView.findViewById<SwipeRefreshLayout>(R.id.swipeRefreshLayout)

        client = OkHttpClient()


        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = adapter

        val scrollListener = object : EndlessRecyclerViewScrollListener(layoutManager) {
            override fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView?) {
                addPosts(totalItemsCount)
            }

        }

        srl.setOnRefreshListener {
            adapter.clearItems()
            addPosts(0)
            scrollListener.resetState()
        }
        recyclerView.addOnScrollListener(scrollListener)

        addPosts(0)
        return mView
    }

    private fun addPosts(offset: Int){
        srl.isRefreshing = true
        val request = Request.Builder()
            .url("http://nmt.edu.ru/?limitstart=$offset")
            .header("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64; rv:47.0) Gecko/20100101 Firefox/47.0")
            .addHeader("Accept", "text/html")
            .build()

        client.newCall(request).enqueue(object: Callback {
            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful && !response.isRedirect){
                    if (response.body() == null){
                        backgroundToast(
                            activity,
                            String.format(
                                getString(R.string.error_unexpected_response),
                                response.code(),
                                response.message()
                            ),
                            Toast.LENGTH_LONG)
                    }
                    val doc = Jsoup.parse(response.body()!!.string())
                    val posts = doc.select("#sp-main-body .groupLeading")
                    val news = ArrayList<NewsElement>()
                    posts.forEach {
                        try {
                            val new = NewsElement(
                                it.select(".catItemDateCreated")[0].text(),
                                it.select(".catItemTitle > a")[0].text(),
                                it.select(".catItemIntroText")[0].text(),
                                it.select(".catItemTitle > a")[0].attr("href"),
                            )

                            news.add(new)
                        }
                        catch (e: Exception){
                            news.add(NewsElement())
                        }
                    }

                    Handler(Looper.getMainLooper()).post(object: Runnable{
                        override fun run() {
                            adapter.addItems(news)
                            srl.isRefreshing = false
                        }
                    })
                    currentPage += NEWS_PER_PAGE
                    return
                }
                else {
                    backgroundToast(
                        activity,
                        String.format(
                            getString(R.string.error_response_error),
                            response.code(),
                            response.message()
                        ),
                        Toast.LENGTH_LONG)
                    Log.e("NewsFragment", response.message() + " (" + response.code()+ ") on \"" + call.request().url().toString() + "\"!")
                }
                Handler(Looper.getMainLooper()).post(object: Runnable{
                    override fun run() {
                        srl.isRefreshing = false
                    }
                })
            }

            override fun onFailure(call: Call, e: IOException) {
                backgroundToast(activity, getString(R.string.error_request_failed), Toast.LENGTH_LONG)
                e.printStackTrace()
                Handler(Looper.getMainLooper()).post(object: Runnable{
                    override fun run() {
                        srl.isRefreshing = false
                    }
                })
            }
        })


    }


}