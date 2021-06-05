package ru.nukdotcom.mynmc.ui.posts

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.CookieManager
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import ru.nukdotcom.mynmc.MainActivity
import ru.nukdotcom.mynmc.R
import ru.nukdotcom.mynmc.helpers.WebViewHelpers

class PostsFragment : Fragment() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val mView = inflater.inflate(R.layout.fragment_posts, container, false)

        val webView = mView.findViewById<WebView>(R.id.webView)

        webView.settings.apply {
            javaScriptEnabled = true
            domStorageEnabled = true
            databaseEnabled = true
            cacheMode = WebSettings.LOAD_DEFAULT
        }

        val url = this.getString(R.string.app_url_schema) + this.getString(R.string.app_domain) + this.getString(R.string.app_dir) + "posts"

        webView.webViewClient = object: WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                return WebViewHelpers.getDomainName(url!!) != getString(R.string.app_domain)
            }
        }

        CookieManager.getInstance().setCookie(url, "mobile=true")
        webView.loadUrl(url)

        return mView
    }

    companion object {
        @JvmStatic
        fun newInstance() = PostsFragment()
    }
}